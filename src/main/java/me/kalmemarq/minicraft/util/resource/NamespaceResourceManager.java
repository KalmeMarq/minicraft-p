package me.kalmemarq.minicraft.util.resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.resource.pack.ResourcePack;

public class NamespaceResourceManager implements ResourceManager {
    private final String namespace;
    private final List<ResourcePack> packs = new ArrayList<>();

    public NamespaceResourceManager(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
      return this.namespace;
    }

    public void addPack(ResourcePack pack) {
        this.packs.add(pack);
    }

    @Nullable
    @Override
    public Resource getResource(Identifier id) {
        for (int i = 0; i < this.packs.size(); i++) {
            ResourcePack pack = this.packs.get(i);

            InputStream stream;
            if ((stream = pack.open(id)) != null) {
                return new Resource(pack, stream);
            }
        }

        return null;
    }

    @Override
    public List<Resource> getResources(Identifier id) {
        List<Resource> list = new ArrayList<>();

        for (int i = 0; i < this.packs.size(); i++) {
            ResourcePack pack = this.packs.get(i);

            InputStream stream;
            if ((stream = pack.open(id)) != null) {
                list.add(new Resource(pack, stream));
            }
        }

        return Lists.reverse(list);
    }

    @Override
    public Map<String, Resource> findResources(String startingPath, Predicate<Identifier> pathPredicate) {
        return null;
    }

    @Override
    public Map<String, Resource> findAllResources(String startingPath, Predicate<Identifier> pathPredicate) {
        return null;
    }
}
