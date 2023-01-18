package me.kalmemarq.minicraft.util.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface ResourceReloader {
    CompletableFuture<Void> reload(Executor executor);
}
