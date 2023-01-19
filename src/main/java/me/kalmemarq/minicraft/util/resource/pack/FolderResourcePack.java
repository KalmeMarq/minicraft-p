package me.kalmemarq.minicraft.util.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.util.Identifier;

public class FolderResourcePack extends ResourcePack {
    private final Path root;
    
    public FolderResourcePack(String name, Path root) {
        super(name);
        this.root = root;
    }

    @Nullable
    @Override
    public InputStream open(Identifier id) {
        Path file = this.root.resolve("assets/" + id.getNamespace() + "/" + id.getPath());

        if (Files.exists(file, new LinkOption[0])) {
            try {
                return Files.newInputStream(file, new OpenOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Set<String> getNamespaces() {
        Set<String> set = new HashSet<>();

        Path assets = this.root.resolve("assets");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(assets)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    set.add(path.getFileName().toString());
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return set;
    }
}
