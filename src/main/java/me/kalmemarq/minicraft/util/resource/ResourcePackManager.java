package me.kalmemarq.minicraft.util.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import me.kalmemarq.minicraft.util.resource.pack.ResourcePack;
import me.kalmemarq.minicraft.util.resource.provider.ResourcePackProvider;

public class ResourcePackManager {
    private final Set<ResourcePackProvider> providers = new HashSet<>();
    private Map<String, ResourcePackItem> items = new HashMap<>();
    private List<ResourcePackItem> enabled = new ArrayList<>();

    public void addProvider(ResourcePackProvider provider) {
        this.providers.add(provider);
    }

    public void removeProvider(ResourcePackProvider provider) {
        this.providers.remove(provider);
    }

    public void findPacks() {
        this.items.clear();
        this.enabled.clear();

        for (ResourcePackProvider provider : this.providers) {
            provider.provide((item) -> {
                this.items.put(item.getName(), item);
                this.enabled.add(item);
            });
        }
    }

    public void setEnabledPacks(Set<String> names) {
        // TODO: Order should matter
    }

    public List<ResourcePack> createResourcePacks() {
        return this.enabled.stream().map(ResourcePackItem::getPack).collect(Collectors.toList());
    }

    public static class ResourcePackItem {
        private final String name;
        private final PackSupplier packSupplier;

        public ResourcePackItem(String name, PackSupplier packSupplier) {
            this.name = name;
            this.packSupplier = packSupplier;
        }

        public String getName() {
          return this.name;
        }

        public ResourcePack getPack() {
            return this.packSupplier.get();
        }
        
        @FunctionalInterface
        public interface PackSupplier {
            ResourcePack get();
        }
    }
}
