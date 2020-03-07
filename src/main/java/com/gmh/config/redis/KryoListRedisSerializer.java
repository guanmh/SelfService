package com.gmh.config.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.gmh.utils.CollectionUtils;
import com.gmh.utils.WebUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
　* @description:自定义Redis序列化
　* @param
　* @return
　* @author GMH
　* @date 2019/11/7 0:21
　*/
@Slf4j
public class KryoListRedisSerializer<T> implements RedisSerializer<T> {
    /**
     * 缓存
     */
    private static final Cache<Kryo, CollectionSerializer> COLLECTION_SERIALIZER_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();
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
    /**
     * 当前转换的list的类型
     */
    private static final Class<ArrayList> LIST_CLASS = ArrayList.class;

    private Class<T> clazz;

    KryoListRedisSerializer(Class<T> clazz) {
        super();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return (T) deserialize(bytes, clazz);
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }
        Class<?> aClass = CollectionUtils.get(t, 0).getClass();
        Kryo kryo = getKryo(aClass);
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            try (Output output = new Output(byteOutputStream)) {
                kryo.writeObject(output, t);
            }
            byte[] bytes = byteOutputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error(WebUtils.getTimerLog(), e);
        }
        return EMPTY_BYTE_ARRAY;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    private <T> ArrayList<T> deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        Kryo kryo = getKryo(clazz);
        CollectionSerializer serializer = getCollectionSerializer(kryo, clazz);

        try (Input input = new Input(bytes)) {
            ArrayList arrayList = kryo.readObject(input, LIST_CLASS, serializer);
            return arrayList;
        } catch (Exception e) {
            log.error(WebUtils.getTimerLog() + clazz + bytes.length, e);
        }

        return null;
    }

    private CollectionSerializer getCollectionSerializer(Kryo kryo, Class<?> clazz) {
        return getCollectionSerializer(kryo, clazz, null);
    }

    private CollectionSerializer getCollectionSerializer(
            Kryo kryo, Class<?> clazz, Serializer registerSerializer) {
        Assert.notNull(kryo, WebUtils.getTimerLog() + "没有找到需要序列化的kryo");
        Assert.notNull(clazz, WebUtils.getTimerLog() + "没有找到需要序列化的Class");
        CollectionSerializer collectionSerializer = COLLECTION_SERIALIZER_CACHE.getIfPresent(kryo);
        if (collectionSerializer == null) {
            synchronized (kryo) {
                collectionSerializer = new CollectionSerializer();
                if (registerSerializer == null) {
                    collectionSerializer.setElementClass(
                            clazz, KryoRedisSerializer.getElementClassSerializer(kryo, clazz));
                } else {
                    collectionSerializer.setElementClass(clazz, registerSerializer);
                }
                collectionSerializer.setElementsCanBeNull(false);
                COLLECTION_SERIALIZER_CACHE.put(kryo, collectionSerializer);
            }
        }
        return collectionSerializer;
    }

    private Kryo getKryo(Class<?> clazz) {
        Assert.notNull(clazz, WebUtils.getTimerLog() + "没有找到需要序列化的Class");
        Kryo kryo = KRYO_CACHE.getIfPresent(clazz);
        if (kryo == null) {
            synchronized (clazz) {
                kryo = KRYO.get();
                kryo.setReferences(false);
                kryo.setRegistrationRequired(true);
                Serializer registerSerializer = KryoRedisSerializer.getElementClassSerializer(kryo, clazz);
                kryo.register(clazz, registerSerializer);
                kryo.register(LIST_CLASS, getCollectionSerializer(kryo, clazz, registerSerializer));

                KRYO_CACHE.put(clazz, kryo);
            }
        }
        return kryo;
    }
}
