package me.kalmemarq.minicraft.util.sound;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;

public class SoundEvents {
    public static final Registry<Identifier, Identifier> REGISTRY = new Registry<>();

    public static Identifier register(String id) {
        return register(new Identifier(id));
    }

    public static Identifier register(Identifier id) {
        REGISTRY.add(id, id);
        return id;
    }

    public static final Identifier BOSS_DEATH = SoundEvents.register("entity.boss_death");
    public static final Identifier SELECT = SoundEvents.register("ui.select");
    public static final Identifier CONFIRM = SoundEvents.register("ui.confirm");
    public static final Identifier CRAFT = SoundEvents.register("ui.craft");
    public static final Identifier DEATH = SoundEvents.register("entity.death");
    public static final Identifier EXPLODE = SoundEvents.register("random.explode");
    public static final Identifier FUSE = SoundEvents.register("random.fuse");
    public static final Identifier MONSTER_HURT = SoundEvents.register("entity.monster_hurt");
    public static final Identifier PICKUP = SoundEvents.register("random.pickup");
    public static final Identifier PLAYER_HURT = SoundEvents.register("entity.player_hurt");
}
