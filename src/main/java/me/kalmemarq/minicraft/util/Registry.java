package me.kalmemarq.minicraft.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Registry<T> {
    private final Map<Identifier, T> idToEntry = new HashMap<>();
    private final Map<T, Identifier> entryToId = new HashMap<>();

    public void add(Identifier id, T value) {
        this.idToEntry.put(id, value);
        this.entryToId.put(value, id);
    }

    public Identifier getId(T value) {
        return this.entryToId.get(value);
    }
    
    @Nullable
    public T get(Identifier id) {
        return this.idToEntry.get(id);
    }

    public boolean containsId(Identifier id) {
        return this.idToEntry.containsKey(id);
    }

    public boolean contains(T value) {
        return this.idToEntry.containsValue(value);
    }

    public Collection<T> values() {
        return this.idToEntry.values();
    }
}