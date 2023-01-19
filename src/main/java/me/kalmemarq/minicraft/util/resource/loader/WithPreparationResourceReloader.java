package me.kalmemarq.minicraft.util.resource.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import me.kalmemarq.minicraft.util.resource.ResourceManager;

public abstract class WithPreparationResourceReloader<T> implements ResourceReloader {
    @Override
    public final CompletableFuture<Void> reload(Executor executor, ResourceManager manager) {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager), executor).thenAcceptAsync((v) -> this.apply(v, manager), executor);
    }

    /**
     * Prepares the data that will then be used in the apply stage.
     * @param manager The resource manager
     * @return The result value
     */
    protected abstract T prepare(ResourceManager manager);
    
    /**
     * Receives the result from the prepare stage.
     * @param result The result value
     * @param manager The resource manager
     */
    protected abstract void apply(T result, ResourceManager manager);
}
