package me.kalmemarq.minicraft.util.resource;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.common.io.Resources;

import me.kalmemarq.minicraft.util.Identifier;

public interface ResourceManager {
    /**
     * Returns a resource or {@code null} if it was not found.
     * @param id Identifier of the resource
     * @return A {@link Resource} or {@code null} if it was not found.
     */
    @Nullable
    Resource getResource(Identifier id);

    /**
     * Returns a list of resources. The list is reversed so the bottommost are first.
     * @param id Identifier of the resource
     * @return A list of {@link Resources}
     */
    List<Resource> getResources(Identifier id);

    Map<String, Resource> findResources(String startingPath, Predicate<Identifier> pathPredicate);
    
    Map<String, Resource> findAllResources(String startingPath, Predicate<Identifier> pathPredicate);
}
