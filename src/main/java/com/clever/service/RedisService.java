package com.clever.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务类
 * 提供了对Redis的各种操作，包括对字符串、列表、集合、哈希的基本操作，以及对键的过期时间和存在性的操作。
 *
 * @Author xixi
 * @Date 2023-12-18 10:39
 **/
@Component
public class RedisService {
    private final static Logger log = LoggerFactory.getLogger(RedisService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取Redis模板
     *
     * @return Redis模板
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 从Redis中获取字符串值，并将其反序列化为Java对象
     *
     * @param key 键
     * @return 泛型
     */
    public <T> T getString(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 从Redis中获取字符串值，并将其反序列化为Java对象
     *
     * @param key 键
     * @return 泛型
     */
    public <T> T getString(String key, T defaultValue) {
        T value = (T) redisTemplate.opsForValue().get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 将Java对象序列化为字符串，并存储到Redis中
     *
     * @param key 键
     * @param t   值
     */
    public <T> void setString(String key, T t) {
        redisTemplate.opsForValue().set(key, t);
    }

    /**
     * 将Java对象序列化为字符串，并存储到Redis中，同时设置过期时间
     *
     * @param key  键
     * @param t    值
     * @param time 过期时间
     */
    public <T> void setString(String key, T t, long time) {
        setString(key, t, time, TimeUnit.SECONDS);
    }

    /**
     * 将Java对象序列化为字符串，并存储到Redis中，同时设置过期时间
     *
     * @param key      键
     * @param t        值
     * @param time     过期时间
     * @param timeUnit 时间单位
     */
    public <T> void setString(String key, T t, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, t, time, timeUnit);
    }

    /**
     * 往list的右边插入多个元素
     *
     * @param key key
     * @param es  集合元素
     * @param <E> 泛型
     */
    public <E> void pushList(String key, List<E> es) {
        redisTemplate.opsForList().rightPushAll(key, es);
    }

    /**
     * 往list的右边插入一个元素
     *
     * @param key key
     * @param e   集合元素
     * @param <E> 泛型
     */
    public <E> void pushList(String key, E e) {
        redisTemplate.opsForList().rightPush(key, e);
    }

    /**
     * 获取list的长度
     *
     * @param key key
     * @return size
     */
    public Long getListLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 根据范围获取list的元素
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @param <E>   泛型
     * @return list的元素
     */
    public <E> List<E> rangeList(String key, long start, long end) {
        return (List<E>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     */
    public int listSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        if (size == null) {
            return 0;
        }
        return size.intValue();
    }

    /**
     * 往set集合设置元素
     *
     * @param key key
     * @param es  元素集合
     * @param <E> 泛型
     */
    public <E> void addSet(String key, List<E> es) {
        redisTemplate.opsForSet().add(key, es);
    }

    /**
     * 往set集合设置元素
     *
     * @param key key
     * @param e   元素集合
     * @param <E> 泛型
     */
    public <E> void addSet(String key, E e) {
        redisTemplate.opsForSet().add(key, e);
    }

    public <E> void removeSet(String key, E e) {
        redisTemplate.opsForSet().remove(key, e);
    }

    /**
     * 获取set中的所有元素
     *
     * @param key key
     * @param <E> e
     * @return es
     */
    public <E> List<E> getSet(String key) {
        return (List<E>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取set的长度
     *
     * @param key key
     * @return 长度
     */
    public Long cardSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 存储一个map集合到hash结构中
     *
     * @param key key
     * @param map map
     * @param <K> key
     * @param <V> value
     */
    public <K, V> void putAllHash(String key, Map<K, V> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 存储一个键值对到hash结构中
     *
     * @param key key
     * @param k   field
     * @param e   e
     * @param <K> key
     * @param <V> e
     */
    public <K, V> void putHash(String key, K k, V e) {
        redisTemplate.opsForHash().put(key, k, e);
    }

    /**
     * 获取hash类型的所有键值
     *
     * @param key key
     * @param <K> k
     * @param <V> v
     * @return map
     */
    public <K, V> Map<K, V> getHash(String key) {
        Set<Object> hashKeys = redisTemplate.opsForHash().keys(key);
        HashMap<K, V> map = new HashMap<>(hashKeys.size());
        for (Object hashKey : hashKeys) {
            map.put((K) hashKey, (V) redisTemplate.opsForHash().get(key, hashKey));
        }
        return map;
    }

    /**
     * 获取一个hashKey中的某个字段的值
     *
     * @param key     key
     * @param hashKey hash中的字段
     * @param <V>     hash字段中的值
     * @return v
     */
    public <V> V getHashFieldValue(String key, Object hashKey) {
        return (V) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 判断一个field是否在hash中
     *
     * @param key key
     * @param k   field
     * @param <K> k
     */
    public <K> boolean fieldIsExistsHash(String key, K k) {
        return redisTemplate.opsForHash().hasKey(key, k);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 是否成功
     */
    public boolean setKeyExpire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置key={}过期失败", key, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒)
     */
    public Long getKeyExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    public void delKeys(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(Arrays.asList(keys));
            }
        }
    }

    /**
     * 格式化缓存key
     *
     * @param object 键名
     * @return key
     */
    public String formatKey(Object... object) {
        StringBuilder builder = new StringBuilder();
        for (Object o : object) {
            builder.append(o.toString());
            builder.append(":");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
