package me.kalmemarq.minicraft.world;

import me.kalmemarq.minicraft.entity.EntityType;
import me.kalmemarq.minicraft.entity.PlayerEntity;

public class World {
    private final Level[] levels = new Level[LevelDepth.values().length];
    private final WorldProperties properties;

    private LevelDepth currentDepth = LevelDepth.SURFACE;

    public PlayerEntity playerEntity;

    public World(WorldProperties properties) {
        this.properties = properties;

        for (int i = 1; i < 2; i++) {
            this.levels[i] = new Level(this, 128, 128);
        }

        playerEntity = EntityType.PLAYER.create(this, getCurrentLevel());
        this.getCurrentLevel().addEntity(playerEntity);
    }

    public PlayerEntity getPlayerEntity() {
      return playerEntity;
    }

    protected int playerX = 0;
    protected int playerY = 0;

    protected boolean up;
    protected boolean down;
    protected boolean right;
    protected boolean left;

    public void tick() {
        Level currentLevel = this.getCurrentLevel();
        currentLevel.tick();
    }

    public void render() {
        Level currentLevel = this.getCurrentLevel();

        currentLevel.render();
    }

    public LevelDepth getCurrentDepth() {
        return currentDepth;
    }

    public Level getCurrentLevel() {
        return this.levels[this.currentDepth.getId()];
    }
}
