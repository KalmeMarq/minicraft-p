package me.kalmemarq.minicraft.util.resource.provider;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.function.Consumer;

import me.kalmemarq.minicraft.util.resource.ResourcePackManager.ResourcePackItem;
import me.kalmemarq.minicraft.util.resource.pack.FolderResourcePack;
import me.kalmemarq.minicraft.util.resource.pack.VanillaResourcePack;
import me.kalmemarq.minicraft.util.resource.pack.ZipResourcePack;

public class DefaultResourcePackProvider implements ResourcePackProvider {
    private final VanillaResourcePack defaultResourcePack;

    public DefaultResourcePackProvider(VanillaResourcePack defaultResourcePack) {
        this.defaultResourcePack = defaultResourcePack;
    }

    @Override
    public void provide(Consumer<ResourcePackItem> consumer) {
        consumer.accept(new ResourcePackItem(this.defaultResourcePack.getName(), () -> this.defaultResourcePack));

        this.defaultResourcePack.getSubBuiltinResourcePacks().forEach(path -> {
            String filename = path.getFileName().toString();

            if (Files.isDirectory(path) && Files.isRegularFile(path.resolve("pack_manifest.json"), new LinkOption[0])) {
                consumer.accept(new ResourcePackItem(filename, () -> new FolderResourcePack(filename, path)));
            } else if (Files.isRegularFile(path, new LinkOption[0]) && filename.endsWith(".zip")) {
                consumer.accept(new ResourcePackItem(filename, () -> new ZipResourcePack(filename, path.toFile())));
            }
        });;
    }
}
