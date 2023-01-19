package me.kalmemarq.minicraft.util.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.resource.loader.ResourceLoader;
import me.kalmemarq.minicraft.util.resource.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.resource.pack.ResourcePack;

public class ReloadableResourceManager implements ResourceManager, AutoCloseable {
    private List<ResourcePack> packs = new ArrayList<>();
    private final List<ResourceReloader> reloaders = new ArrayList<>();
    private final Map<String, NamespaceResourceManager> namespaceManagers = new HashMap<>();

    public void addReloader(ResourceReloader reloader) {
        this.reloaders.add(reloader);
    }

    public void removeReloader(ResourceReloader reloader) {
        this.reloaders.remove(reloader);
    }

    private void updateNamespaces() {
        namespaceManagers.clear();
    
        for (ResourcePack pack : this.packs) {
            Set<String> namespaces = pack.getNamespaces();

            for (String namespace : namespaces) {
                if (!this.namespaceManagers.containsKey(namespace)) {
                    NamespaceResourceManager manager = new NamespaceResourceManager(namespace);
                    manager.addPack(pack);
                    this.namespaceManagers.put(namespace, manager);
                } else {
                    this.namespaceManagers.get(namespace).addPack(pack);
                }
            }
        }
    }

    /**
     * Reloads with given resource packs and returns a resource loader.
     * @param packs List of the resource packs to the reloading with
     * @return {@link ResourceLoader}.
     */
    public ResourceLoader reload(List<ResourcePack> packs) {
        this.closePacks();
        this.packs = List.copyOf(packs);
        this.updateNamespaces();

        return new ResourceLoader(Minicraft.WORKER, this, this.reloaders, () -> {});
    }

    /**
     * Reloads with the current resource packs and returns a resource loader.
     * @return {@link ResourceLoader}.
     */
    public ResourceLoader reload() {
        this.updateNamespaces();

        return new ResourceLoader(Minicraft.WORKER, this, this.reloaders, () -> {});
    }

    private void closePacks() {
        this.packs.forEach(ResourcePack::close);
    }

    @Nullable
    @Override
    public Resource getResource(Identifier id) {
        ResourceManager manager = this.namespaceManagers.get(id.getNamespace());

        if (manager != null) {
            return manager.getResource(id);
        }

        return null;
    }

    @Override
    public List<Resource> getResources(Identifier id) {
        ResourceManager manager = this.namespaceManagers.get(id.getNamespace());

        if (manager != null) {
            return manager.getResources(id);
        }

        return List.of();
    }

    @Override
    public Map<String, Resource> findResources(String startingPath, Predicate<Identifier> pathPredicate) {
        return null;
    }

    @Override
    public Map<String, Resource> findAllResources(String startingPath, Predicate<Identifier> pathPredicate) {
        return null;
    }

    @Override
    public void close() throws Exception {
        this.closePacks();
    }
}
