package me.kalmemarq.minicraft.util.resource.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import me.kalmemarq.minicraft.util.resource.ResourceManager;

public abstract class SyncResourceReloader implements ResourceReloader {
    @Override
    public final CompletableFuture<Void> reload(Executor executor, ResourceManager manager) {
        return CompletableFuture.runAsync(() -> this.reload(manager), executor);
    }

    abstract protected void reload(ResourceManager manager);
}
