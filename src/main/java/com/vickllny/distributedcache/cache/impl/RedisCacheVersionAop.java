package com.vickllny.distributedcache.cache.impl;

import com.vickllny.distributedcache.cache.CacheConfig;
import com.vickllny.distributedcache.cache.ICacheAOP;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;

@Aspect
public class RedisCacheVersionAop implements ICacheAOP, Ordered {

    private final RedisTemplate<String, Object> redisTemplate;
    private int order = 10;

    public RedisCacheVersionAop(final RedisTemplate<String, Object> redisTemplate, final int order) {
        this.redisTemplate = redisTemplate;
        this.order = order;
    }

    public RedisCacheVersionAop(final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Around(value = "@annotation(com.vickllny.distributedcache.cache.CacheConfig)")
    public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
        return ICacheAOP.super.doAround(pjp);
    }



    @Override
    public String getKey(final ProceedingJoinPoint pjp, final String namespace, final CacheConfig cacheConfig) {
        final String fmtKey = fmtKey(namespace);
        final Object version = redisTemplate.opsForValue().get(fmtKey);
        if(version == null){
            Boolean result;
            if((result = redisTemplate.opsForValue().setIfAbsent(fmtKey, 0)) != null && result){
                return "_0" +  ICacheAOP.super.getKey(pjp, namespace, cacheConfig);
            }
            return "_" + redisTemplate.opsForValue().get(fmtKey) + ICacheAOP.super.getKey(pjp, namespace, cacheConfig);
        }
        return "_" + version + ICacheAOP.super.getKey(pjp, namespace, cacheConfig);
    }

    @Override
    public Object getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(final String key, final Object value, final CacheConfig cacheConfig) {
        redisTemplate.opsForValue().setIfAbsent(key, value, cacheConfig.expired(), cacheConfig.timeUnit());
    }

    @Override
    public void updateCache(final String namespace) {
        increaseVersion(fmtKey(namespace));
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    String fmtKey(final String namespace){
        return namespace + "_v";
    }

    void increaseVersion(final String key){
        redisTemplate.opsForValue().increment(key, 1);
    }
}
