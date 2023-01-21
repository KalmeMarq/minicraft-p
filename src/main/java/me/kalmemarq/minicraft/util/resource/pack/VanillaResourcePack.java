package me.kalmemarq.minicraft.util.resource.pack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.util.Identifier;

public class VanillaResourcePack extends ResourcePack {
    private final Path root;
    
    public VanillaResourcePack() {
        super("vanilla");
        this.root = this.getRoot();

        if (this.root == null) {
            Minicraft.LOGGER.error("The vanilla resource pack was unable to build so yeah... we're all fucked");
        }
    }

    @Nullable
    @Override
    public InputStream open(Identifier id) {
        if (this.root == null) return null;
        
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

    @Nullable
    private Path getRoot() {
        URL rootURL = VanillaResourcePack.class.getResource("/.root");

        try {
            URI rootURI = rootURL.toURI();
            
            try {
                return Paths.get(rootURI).getParent();
            } catch (FileSystemNotFoundException e) {
            }

            try {
                FileSystems.newFileSystem(rootURI, Collections.emptyMap());
            } catch (FileSystemAlreadyExistsException | IOException e) {
                e.printStackTrace();
            }

            return Paths.get(rootURI).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Path> getSubBuiltinResourcePacks() {
        List<Path> list = new ArrayList<>();

        if (this.root == null) return list;

        Path path = this.root.resolve("assets/minicraft/resourcepacks");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (entry.endsWith(".zip")) {
                    list.add(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    } 

    @Override
    public Set<String> getNamespaces() {
        return ImmutableSet.of("minicraft");
    }

    @Override
    public String getDisplayName() {
        return "Vanilla";
    }
}
