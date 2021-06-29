package com.netty.api;

public interface RedisService<K, V> extends Redis<K, V> {
    public V get(K key);

    public boolean del(K key);

    public boolean set(K key, V value);

    public boolean set(K key, V value, int second);
}
