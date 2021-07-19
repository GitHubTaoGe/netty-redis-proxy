package com.netty.redis;

import com.netty.api.Redis;
import com.netty.api.RedisService;
import com.netty.redis.proxy.RedisProxy;

public class ProxyRedis<K, V> implements Redis<K, V>, RedisService<K, V> {


    private RedisService redisService;

    public ProxyRedis(String clientId) {
        this.redisService = RedisProxy.create(clientId, RedisService.class);
    }


    @Override
    public V get(Object key) {
        return (V) redisService.get(key);
    }

    @Override
    public boolean del(K key) {
        return redisService.del(key);
    }

    @Override
    public boolean set(K key, V value) {

        return redisService.set(key, value);
    }

    @Override
    public boolean set(K key, V value, int milliseconds) {
        return redisService.set(key, value, milliseconds);
    }
}
