package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class EntityType<T extends Entity> {
    public static final Registry<EntityType<?>> ENTITY_TYPE_REGISTRY = new Registry<>();

    public static final EntityType<PlayerEntity> PLAYER = register("player", PlayerEntity::new);

    public static <T extends Entity> EntityType<T> register(String id, EntityFactory<T> factory) {
        return register(new Identifier(id), factory);
    }

    public static <T extends Entity> EntityType<T> register(Identifier id, EntityFactory<T> factory) {
        EntityType<T> type = new EntityType<>(factory);
        ENTITY_TYPE_REGISTRY.add(id, type);
        return type;
    }

    private final EntityFactory<T> factory;

    private EntityType(EntityFactory<T> factory) {
        this.factory = factory;
    }

    public T create(World world, Level level) {
        return this.factory.create(world, level);
    }

    public interface EntityFactory<T extends Entity> {
        T create(World world, Level level);
    }
}
