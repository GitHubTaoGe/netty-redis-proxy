package com.netty.api;

public interface Redis<K, V> {
    public V get(K key);

    public boolean del(K key);

    public boolean set(K key, V value);

    public boolean set(K key, V value, int milliseconds);
}
