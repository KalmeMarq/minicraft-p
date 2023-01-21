package me.kalmemarq.minicraft.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class Registry<K, T> {
    private final Map<K, T> idToEntry = new HashMap<>();
    private final Map<T, K> entryToId = new HashMap<>();

    public void add(K id, T value) {
        this.idToEntry.put(id, value);
        this.entryToId.put(value, id);
    }

    public K getId(T value) {
        return this.entryToId.get(value);
    }
    
    @Nullable
    public T get(K id) {
        return this.idToEntry.get(id);
    }

    public boolean containsId(K id) {
        return this.idToEntry.containsKey(id);
    }

    public boolean contains(T value) {
        return this.idToEntry.containsValue(value);
    }

    public Collection<T> values() {
        return this.idToEntry.values();
    }
}