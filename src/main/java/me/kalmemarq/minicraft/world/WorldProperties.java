package me.kalmemarq.minicraft.world;

public class WorldProperties {
    private int spawnX;
    private int spawnY;
    private int time;
    private int timeOfDay;

    public WorldProperties(int spawnX, int spawnY) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }
    
    public int getTime() {
        return this.time;
    }
}
