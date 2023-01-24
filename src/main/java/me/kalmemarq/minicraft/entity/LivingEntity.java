package me.kalmemarq.minicraft.entity;

import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public abstract class LivingEntity extends Entity {
    protected int health;
    protected int stamina;

    public LivingEntity(World world, Level level) {
        super(world, level);
        this.health = this.getMaxHealth();
        this.stamina = this.getMaxStamina();
    }

    public int getHealth() {
      return health;
    }

    public int getStamina() {
      return stamina;
    }

    abstract public int getMaxHealth();
    abstract public int getMaxStamina();
}
