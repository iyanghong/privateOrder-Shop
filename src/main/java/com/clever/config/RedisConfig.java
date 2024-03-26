package com.clever.config;

import com.clever.constant.CacheConstant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置类
 *
 * @Author xixi
 * @Date 2023-12-18 10:32
 **/
@Configuration
@EnableCaching
public class RedisConfig {
    // 缓存键前缀
    private static final String KEY_PREFIX = "%s:%s:";

    /**
     * 缓存管理器配置
     *
     * @param environment   环境配置
     * @param redisTemplate Redis模板
     * @return 缓存管理器
     */
    @Bean
    @Primary
    @DependsOn("customizationRedisTemplate")
    public CacheManager cacheManager(ConfigurableEnvironment environment, RedisTemplate<String, Object> redisTemplate) {
        // 获取应用名称
        String name = environment.getProperty("spring.application.name");
        CacheConstant.APP_NAME = name;
        // 创建并返回一个RedisCacheManager对象
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                .cacheDefaults(cacheConfiguration(name)
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer())))
                // 配置同步修改或删除 put/evict
                .transactionAware().build();

    }

    /**
     * 缓存配置
     *
     * @param name 应用名称
     * @return Redis缓存配置
     */
    private RedisCacheConfiguration cacheConfiguration(String name) {
        // 创建并返回一个RedisCacheConfiguration对象
        return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues().entryTtl(Duration.ofHours(24))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                .computePrefixWith(cacheName -> String.format(KEY_PREFIX, name, cacheName));
    }

    /**
     * 自定义的缓存key的生成策略
     * 若想使用这个key  只需要讲注解上keyGenerator的值设置为keyGenerator即可</br>
     *
     * @return 自定义策略生成的key
     */
    @Bean
    public KeyGenerator keyGenerator() {
        // 返回一个KeyGenerator对象，该对象生成的key是由目标类名、方法名和参数值组成的字符串
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * RedisTemplate配置
     *
     * @param factory 连接配置工厂
     * @return 自定义的RedisTemplate
     */
    @Bean("customizationRedisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建一个RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}