package me.kalmemarq.minicraft.util.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import me.kalmemarq.minicraft.util.resource.pack.ResourcePack;

public class Resource {
    private final String packName;
    private final InputStream stream;

    public Resource(ResourcePack pack, InputStream stream) {
        this.packName = pack.getName();
        this.stream = stream;
    }

    /**
     * Returns as an input stream. Remember to close it when you are finished with it.
     * @return As an {@link InputStream}
     */
    public InputStream getAsInputStream() {
      return this.stream;
    }

    /**
     * Returns as an reader. Remember to close it when you are finished with it.
     * @return As an {@link BufferedReader}
     */
    public BufferedReader getAsReader() {
        return new BufferedReader(new InputStreamReader(this.stream, StandardCharsets.UTF_8));
    }

    /**
     * Returns as a string. If it failed to read it will return an empty string.
     * @return As a string. If it failed to read it will return an empty string
     */
    public String getAsString() {
        try (BufferedReader reader = this.getAsReader()) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Returns the name of the resource pack from where this resource was originated.
     * @return The name of the resource pack from where this resource was originated.
     */
    public String getResourcePackName() {
      return this.packName;
    }
}
