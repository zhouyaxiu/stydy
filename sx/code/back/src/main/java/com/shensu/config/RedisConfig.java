package com.shensu.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName RedisConfig.java
 * @Description Redis序列化及缓存配置
 * @createTime 2021年06月23日 10:33
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    //配置redis的过期时间
    private Duration timeToLive = Duration.ZERO;

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * 统一管理spting的cache
     *
     * @param factory redis的连接工厂
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //新建一个Jackson2JsonRedis的redis存储的序列化方式
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //解决查询缓存转换异常的问题
        // 配置序列化（解决乱码的问题）
        //entryTtl设置过期时间
        //serializeValuesWith设置redis存储的序列化方式
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))
                .disableCachingNullValues();
        //定义要返回的redis缓存管理对象
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }

    /**
     * redis序列化通用配置
     * @param factory RedisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        //免疫平台无法支持Jackson2JsonRedisSerializer，此处使用GenericJackson2JsonRedisSerializer做序列化
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //RedisTemplate配置
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean("redisTempxxx")
    public RedisTemplate getRedisTemp(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(factory);
        return template; 
    }
    

}
