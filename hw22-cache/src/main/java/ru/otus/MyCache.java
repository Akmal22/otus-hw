package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final String PUT_ACTION = "put";
    private static final String GET_ACTION = "get";
    private static final String REMOVE_ACTION = "remove";
    private final WeakHashMap<K, V> cache;
    private final List<HwListener<K, V>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        listeners.forEach(l -> l.notify(key, value, PUT_ACTION));
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        listeners.forEach(l -> l.notify(key, null, REMOVE_ACTION));
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        listeners.forEach(l -> l.notify(key, null, GET_ACTION));
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
