package com.gmh.config.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

/**
　* @description:自定义Redis序列化
　* @param
　* @return
　* @author GMH
　* @date 2019/11/7 0:24
　*/
@Slf4j
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    /**
     * 数据为null时返回
     */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(Kryo::new);
    /**
     * 缓存
     */
    private static final Cache<Class, Kryo> KRYO_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();

    private Class<T> clazz;

    KryoRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    static Serializer getElementClassSerializer(Kryo kryo, Class clazz) {
        return new FieldSerializer(kryo, clazz);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return deserialize(bytes, clazz);
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }

        Kryo kryo = getKryo(clazz);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (Output output = new Output(baos)) {
                kryo.writeClassAndObject(output, t);
            }
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return EMPTY_BYTE_ARRAY;
    }

    private T deserialize(byte[] bytes, Class clazz) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        Kryo kryo = getKryo(clazz);

        try (Input input = new Input(bytes)) {
            return (T) kryo.readClassAndObject(input);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private Kryo getKryo(Class<T> clazz) {
        Kryo kryo = KRYO_CACHE.getIfPresent(clazz);
        if (kryo == null) {
            kryo = KRYO.get();
            kryo.setReferences(false);
            kryo.register(clazz);
            KRYO_CACHE.put(clazz, kryo);
        }
        return kryo;
    }
}
