package com.shensu.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 版权：(C) 版权所有 2019-2022  苏州沈苏自动化有限公司
 * <简述>redis数据库 常用操作  继承 Redis数据基础操作
 * <详细描述>
 *
 * @author gelongyu
 * @version $Id$
 * @see
 * @since
 */
@Repository
public class RedisOperates extends RedisOperate {

    /**
     * @Field redisTemplate ： redis连接模板工具
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *〈简述〉设置value格式
     *〈详细描述〉对象、列表、集合
     * @author gelongyu
     * @param <T> 泛型
     * @param clazz java类型
     */
    /*private <T> void setValueSerializer(Class<T> clazz) {
        JavaType javaType = constructType(clazz);
        setValueSerializer(javaType);
    }*/

    /**
     *〈简述〉设置value格式
     *〈详细描述〉对象、列表、集合
     * @author gelongyu
     * @param <T> 泛型
     * @param javaType java类型
     */
    /*private <T> void setValueSerializer(JavaType javaType) {
        RedisSerializer<T> redisSerializer = redisSeralizer(javaType);
        redisTemplate.setValueSerializer(redisSerializer);
    }*/

    /**
     *〈简述〉创建redis序列化
     *〈详细描述〉
     * @author gelongyu
     * @param <T> 泛型
     * @param clazz java类型
     * @return 序列化格式
     */
    /*private <T> RedisSerializer<T> redisSeralizer(Class<T> clazz) {
        return redisSeralizer(constructType(clazz));
    }*/

    /**
     * 〈简述〉创建redis序列化
     * 〈详细描述〉
     *
     * @param <T>   泛型
     * @param clazz java类型
     * @return 序列化格式
     * @author gelongyu
     */
    private <T> JavaType constructType(Class<T> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    /**
     *〈简述〉创建redis序列化
     *〈详细描述〉
     * @author gelongyu
     * @param <T> 泛型
     * @param javaType java类型
     * @return 序列化格式
     */
    /*private <T> RedisSerializer<T> redisSeralizer(JavaType javaType) {
        Jackson2JsonRedisSerializer<T> redisSerializer = new Jackson2JsonRedisSerializer<>(javaType);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        redisSerializer.setObjectMapper(objectMapper);
        return redisSerializer;
    }*/

    /**
     * 〈简述〉添加字符串
     * 〈详细描述〉附加过期时间
     *
     * @param key     String redis的key
     * @param value   String 字符串
     * @param timeout long 过期时间
     * @param unit    TimeUnit 时间格式
     * @author gelongyu
     */
    public synchronized void set(String key, String value, long timeout, TimeUnit unit) {
        //setValueSerializer(String.class);
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value, timeout, unit);
    }

    /**
     * 〈简述〉添加对象
     * 〈详细描述〉附加过期时间
     *
     * @param key     String redis的key
     * @param obj     Object 对象
     * @param timeout long 过期时间
     * @param unit    TimeUnit 时间格式
     * @author gelongyu
     */
    public synchronized void set(String key, Object obj, long timeout, TimeUnit unit) {
        //setValueSerializer(String.class);
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, obj, timeout, unit);
    }
}
