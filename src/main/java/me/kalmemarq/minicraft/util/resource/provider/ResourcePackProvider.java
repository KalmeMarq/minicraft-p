package me.kalmemarq.minicraft.util.resource.provider;

import java.util.function.Consumer;

import me.kalmemarq.minicraft.util.resource.ResourcePackManager.ResourcePackItem;

public interface ResourcePackProvider {
    void provide(Consumer<ResourcePackItem> consumer);
}
