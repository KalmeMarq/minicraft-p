package me.kalmemarq.minicraft.util.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class WithPreparationResourceReloader<T> implements ResourceReloader {
    @Override
    public final CompletableFuture<Void> reload(Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.prepare(), executor).thenAcceptAsync((v) -> this.apply(v), executor);
    }

    protected abstract T prepare();
    
    protected abstract void apply(T result);
}
