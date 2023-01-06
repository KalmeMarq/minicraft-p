package me.kalmemarq.minicraft.world;

public class World {
    private final Level[] levels = new Level[LevelDepth.values().length];
    private final WorldProperties properties;

    private int currentDepth = LevelDepth.SURFACE.getId();

    public World(WorldProperties properties) {
        this.properties = properties;

        for (int i = 0; i < this.levels.length; i++) {
            this.levels[i] = new Level(this, 16, 16);
        }
    }

    public void tick() {}

    public void render() {
        Level currentLevel = this.getCurrentLevel();
        currentLevel.render();
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public Level getCurrentLevel() {
        return this.levels[this.currentDepth];
    }
}
