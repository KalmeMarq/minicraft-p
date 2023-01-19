package me.kalmemarq.minicraft.util.resource.loader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import me.kalmemarq.minicraft.util.resource.ResourceManager;

public class ResourceLoader {
    private final AtomicInteger completed;
    private int total;

    private CompletableFuture<Void> future;

    public ResourceLoader(Executor executor, ResourceManager resourceManager, List<ResourceReloader> reloaders, OnFinish onFinish) {
        this.completed = new AtomicInteger();
        this.total = reloaders.size();

        CompletableFuture<?>[] futures = new CompletableFuture[reloaders.size()];

        for (int i = 0; i < reloaders.size(); i++) {
            CompletableFuture<Void> f = reloaders.get(i).reload(executor, resourceManager).whenComplete((_a, _b) -> {
                this.completed.incrementAndGet();
            });

            futures[i] = f;
        }

        this.future = CompletableFuture.allOf(futures).whenComplete((_a, _b) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onFinish.finish();
        });
    }

    public CompletableFuture<?> whenPrepared() {
        return this.future;
    }

    public float getProgress() {
        return this.completed.get() / (float)this.total;
    }

    public boolean isCompleted() {
        return this.future.isDone();
    }

    @FunctionalInterface
    public interface OnFinish {
        void finish();
    }
}
