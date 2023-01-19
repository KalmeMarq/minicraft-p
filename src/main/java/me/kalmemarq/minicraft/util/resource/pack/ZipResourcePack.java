package me.kalmemarq.minicraft.util.resource.pack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.util.Identifier;

public class ZipResourcePack extends ResourcePack {
    @Nullable 
    protected ZipFile zipFile;
    protected final File file;
    protected boolean failedToOpen;

    public ZipResourcePack(String name, File file) {
        super(name);
        this.file = file;
    }

    @Nullable
    protected ZipFile getZipFile() {
        if (this.zipFile == null && !this.failedToOpen) {
            try {
                this.zipFile = new ZipFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                this.failedToOpen = true;
            }
        }

        return this.zipFile;
    }

    @Nullable
    @Override
    public InputStream open(Identifier id) {
        ZipFile zip = this.getZipFile();
        
        if (zip != null) {
            String file = "assets/" + id.getNamespace() + "/" + id.getPath();

            ZipEntry entry = zip.getEntry(file);

            if (entry != null && !entry.isDirectory()) {
                try {
                    return zip.getInputStream(entry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public Set<String> getNamespaces() {
        Set<String> set = new HashSet<>();
        ZipFile zip = this.getZipFile();
        
        if (zip != null) {
            Enumeration<? extends ZipEntry> enumeration = zip.entries();

            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();
                String path = entry.getName();

                if (!path.startsWith("assets/")) continue;

                if (entry.isDirectory()) {
                    String namespace = path.substring(path.lastIndexOf('/') + 1);
                    set.add(namespace);
                }
            }
        }
        
        return set;
    }

    public String getDisplayName() {
        return super.getDisplayName().replace(".zip", "");
    }

    @Override
    public void close() {
        if (!this.failedToOpen && this.zipFile != null) {
            try {
                this.zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
