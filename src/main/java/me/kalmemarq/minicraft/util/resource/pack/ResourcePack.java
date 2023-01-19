package me.kalmemarq.minicraft.util.resource.pack;

import java.io.InputStream;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.util.Identifier;

public abstract class ResourcePack implements AutoCloseable {
    private final String name;
    
    public ResourcePack(String name) {
        this.name = name;
    }

    /**
     * Opens an input stream from the path given, if found.
     * @param id Identifier of the file
     * @return {@link InputStream} of the file or {@code null} if path does not exist
     */
    @Nullable
    public abstract InputStream open(Identifier id);

    /**
     * Returns the resource pack name.
     * @return The resource pack name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns all the namespaces in resource pack.
     * @return All the namespaces in resource pack
     */
    public abstract Set<String> getNamespaces();

    /**
     * Returns name for display
     * @return Name for display
     */
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public void close() {
    }
}
