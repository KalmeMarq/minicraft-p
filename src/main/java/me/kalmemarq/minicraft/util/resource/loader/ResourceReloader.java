package me.kalmemarq.minicraft.util.resource.loader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import me.kalmemarq.minicraft.util.resource.ResourceManager;

public interface ResourceReloader {
    CompletableFuture<Void> reload(Executor executor, ResourceManager manager);
}
