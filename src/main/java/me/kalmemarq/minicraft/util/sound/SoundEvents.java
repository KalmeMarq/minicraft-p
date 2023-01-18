package me.kalmemarq.minicraft.util.sound;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;

public class SoundEvents {
    public static final Registry<String> REGISTRY = new Registry<>();

    public static final String BOSS_DEATH = register("bossdeath", "sounds/bossdeath.wav");
    public static final String SELECT = register("select", "sounds/select.wav");
    public static final String CONFIRM = register("confirm", "sounds/confirm.wav");
    public static final String CRAFT = register("craft", "sounds/craft.wav");
    public static final String DEATH = register("death", "sounds/death.wav");
    public static final String EXPLODE = register("explode", "sounds/explode.wav");
    public static final String FUSE = register("fuse", "sounds/fuse.wav");
    public static final String MONSTER_HURT = register("monster_hurt", "sounds/monsterhurt.wav");
    public static final String PICKUP = register("pickup", "sounds/pickup.wav");
    public static final String PLAYER_HURT = register("player_hurt", "sounds/playerhurt.wav");

    public static String register(String id, String path) {
        return register(new Identifier(id), path);
    }

    public static String register(Identifier id, String path) {
        REGISTRY.add(id, path);
        return path;
    }
}
