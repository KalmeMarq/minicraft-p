package me.kalmemarq.minicraft.util.resource;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

public interface ResourceManager {
    @Nullable
    Resource getResource(String path);

    List<Resource> getResources(String path);

    Map<String, Resource> findResources(String startingPath, Predicate<String> pathPredicate);
    
    Map<String, Resource> findAllResources(String startingPath, Predicate<String> pathPredicate);
}
