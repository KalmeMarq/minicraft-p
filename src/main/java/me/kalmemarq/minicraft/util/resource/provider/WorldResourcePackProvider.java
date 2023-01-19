package me.kalmemarq.minicraft.util.resource.provider;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

import me.kalmemarq.minicraft.util.resource.ResourcePackManager.ResourcePackItem;
import me.kalmemarq.minicraft.util.resource.pack.ZipResourcePack;

public class WorldResourcePackProvider implements ResourcePackProvider {
    private final File folder;

    public WorldResourcePackProvider(Path worldPath) {
        this.folder = worldPath.resolve("resourcepacks").toFile();
    }

    @Override
    public void provide(Consumer<ResourcePackItem> consumer) {
        if (this.folder.exists()) {
            for (File file : this.folder.listFiles((d, p) -> p.endsWith(".zip"))) {
                String name = file.getName();
                consumer.accept(new ResourcePackItem(name, () -> new ZipResourcePack(name, folder)));
            }
        }
    }
}
