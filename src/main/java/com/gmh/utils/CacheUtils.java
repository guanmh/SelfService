package com.gmh.utils;

import com.gmh.config.SpringContextUtil;
import com.gmh.config.redis.KryoListRedisSerializer;
import com.gmh.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
　* @description:缓存工具类
　* @param
　* @return
　* @author GMH
　* @date 2019/11/7 0:16
　*/
@Component
public class CacheUtils {

    private static final String REDIS_CACHE_KEY = "cache:value:";
    private static StringRedisTemplate stringRedisTemplate;
    private static RedisTemplate<String, Object> template;
    private static RedisTemplate<String, Object> templateList;

    /**
     * 推送通道数据
     *
     * @param chance
     * @param value
     */
    public static void convertAndSend(String chance, String value) {
        getStringRedisTemplate().convertAndSend(chance, value);
    }

    /**
     * 获取缓存的字符串:putString
     *
     * @param key
     * @return
     */
    public static String getString(String key, String id) {
        return getString(key + ":" + id);
    }

    public static String getString(String key) {
        return getStringRedisTemplate().opsForValue().get(key);
    }

    /**
     * 放入缓存
     *
     * @param data
     */
    public static void put(String key, Object data) {
        put(key, data, getTimeOutDefault());
    }

    /**
     * 字符串的
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        getStringRedisTemplate().opsForValue().set(key, value, 3, TimeUnit.HOURS);
    }

    public static void putString(String key,String id, String value) {
        getStringRedisTemplate().opsForValue().set(key + ":" + id, value, 3, TimeUnit.HOURS);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void remove(String key) {
        Set<String> keys = getTemplate().keys(key + ":*");
        if (CollectionUtils.isNotEmpty(keys)) {
            getTemplate().delete(keys);
        }
    }

    /**
     * 防止重复提交
     *
     * @param token
     * @return
     */
    public static boolean repeatSubmit(CsrfToken token) {
        String value = get(token.getToken(), String.class);
        boolean haveKey = StringUtils.isNotEmpty(value);
        if (haveKey) {
            return true;
        }
        /** 提交间隔3秒 */
        put(token.getToken(), token.getToken(), 3L);
        return false;
    }

    public static <T> T get(String cacheKey, Class<T> tClass) {
        RedisSerializer keySerializer = getTemplate().getKeySerializer();
        Object execute =
                getTemplate()
                        .execute(
                                (RedisCallback<byte[]>)
                                        con -> con.get(keySerializer.serialize(REDIS_CACHE_KEY + cacheKey)));
        if (execute instanceof byte[]) {
            return (T) getTemplate().getValueSerializer().deserialize((byte[]) execute);
        }
        return null;
    }

    /**
     * @return 返回序列号，根据公司区号返回当日序列号(年月日+分公司行政区号+当日流水号)
     */
    public static String sequence(String companyCode, Class<? extends BaseEntity> business) {
        String ymdNoSplit = DateUtils.getYmdNoSplit();
        StringBuilder sequence = new StringBuilder(ymdNoSplit + companyCode);
        String key = "SEQ:" + business.getSimpleName() + ":" + ymdNoSplit + ":" + companyCode;
        sequence.append(
                StringUtils.substring(Objects.toString(incr(key, getTimeOutDefault()) + 101), 1));
        return sequence.toString();
    }

    private static <T> List<T> getList(String cacheKey, Class<T> tClass) {
        RedisSerializer keySerializer = getTemplateList().getKeySerializer();
        Object execute =
                getTemplate()
                        .execute(
                                (RedisCallback<byte[]>)
                                        con -> con.get(keySerializer.serialize(REDIS_CACHE_KEY + cacheKey)));
        if (execute instanceof byte[]) {
            KryoListRedisSerializer valueSerializer =
                    (KryoListRedisSerializer) getTemplateList().getValueSerializer();
            valueSerializer.setClazz(tClass);
            return (List<T>) valueSerializer.deserialize((byte[]) execute);
        }
        return null;
    }

    private static StringRedisTemplate getStringRedisTemplate() {
        if (Objects.isNull(stringRedisTemplate)) {
            FutureTask<StringRedisTemplate> futureTask =
                    new FutureTask<>(() -> SpringContextUtil.getBean(StringRedisTemplate.class));
            try {
                futureTask.run();
                stringRedisTemplate = futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return stringRedisTemplate;
    }

    private static RedisTemplate getTemplate() {
        if (Objects.isNull(template)) {
            FutureTask<RedisTemplate<String, Object>> futureTask =
                    new FutureTask<>(
                            () -> (RedisTemplate<String, Object>) SpringContextUtil.getBean(
                                    "redisTemplate"));
            try {
                futureTask.run();
                template = futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return template;
    }

    private static RedisTemplate getTemplateList() {
        if (Objects.isNull(templateList)) {
            FutureTask<RedisTemplate<String, Object>> futureTask =
                    new FutureTask<>(
                            () ->
                                    (RedisTemplate<String, Object>)
                                            SpringContextUtil.getBean("redisTemplateKryoList"));
            try {
                futureTask.run();
                templateList = futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return templateList;
    }

    /**
     * 缓存的默认时间（当日凌晨清理缓存）
     *
     * @return
     */
    private static long getTimeOutDefault() {
        LocalDateTime now = DateUtils.getNow();
        return DateUtils.betweenSeconds(now, DateUtils.getMax(now));
    }

    /**
     * redis自增长
     *
     * @param key
     * @param liveTime
     * @return
     */
    private static Long incr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter =
                new RedisAtomicLong(key, getTemplate().getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        // 初始设置过期时间
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }

        return increment;
    }

    private static void put(String cacheKey, Object data, Long timeOutSeconds) {
        RedisSerializer keySerializer = getTemplate().getKeySerializer();
        RedisSerializer valueSerializer;
        if (List.class.isAssignableFrom(data.getClass())) {
            valueSerializer = getTemplateList().getValueSerializer();
        } else {
            valueSerializer = getTemplate().getValueSerializer();
        }
        getTemplate()
                .execute(
                        (RedisCallback<Void>)
                                con -> {
                                    byte[] key =
                                            keySerializer.serialize(REDIS_CACHE_KEY + cacheKey);
                                    con.set(key, valueSerializer.serialize(data));
                                    con.expire(key, timeOutSeconds);
                                    return null;
                                });
    }
}
