package me.kalmemarq.minicraft.util.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class SyncResourceReloader implements ResourceReloader {
    @Override
    public final CompletableFuture<Void> reload(Executor executor) {
        return CompletableFuture.runAsync(() -> this.reload(), executor);
    }

    abstract protected void reload();
}
