package com.netty.redis;

import com.netty.api.RedisService;

public class ProxyRedis<K, V> implements RedisService<K, V> {

    private static RedisService redisService;

    static {
        redisService = RedisProxy.create(RedisService.class);
    }

    @Override
    public V get(Object key) {
        return (V) redisService.get(key);
    }

    @Override
    public boolean del(Object key) {
        return redisService.del(key);
    }

    @Override
    public boolean set(Object key, Object value) {
        return redisService.set(key, value);
    }

    @Override
    public boolean set(Object key, Object value, int milliseconds) {
        return redisService.set(key, value, milliseconds);
    }
}
