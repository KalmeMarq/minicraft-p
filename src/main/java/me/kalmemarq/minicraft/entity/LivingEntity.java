package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public abstract class LivingEntity extends Entity {
    private int health;
    private int stamina;

    public LivingEntity(World world, Level level) {
        super(world, level);
        this.health = this.getMaxHealth();
        this.stamina = this.getMaxStamina();
    }

    abstract public int getMaxHealth();
    abstract public int getMaxStamina();
}
