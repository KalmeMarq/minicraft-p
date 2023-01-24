package me.kalmemarq.minicraft.world;

import me.kalmemarq.bso.BSOList;
import me.kalmemarq.bso.BSOMap;
import me.kalmemarq.bso.writer.StringBSOWriter;
import me.kalmemarq.minicraft.entity.Entity;
import me.kalmemarq.minicraft.entity.EntityType;
import me.kalmemarq.minicraft.entity.PlayerEntity;

public class World {
    private final Level[] levels = new Level[LevelDepth.values().length];
    private final WorldProperties properties;

    private LevelDepth currentDepth = LevelDepth.SURFACE;

    public PlayerEntity playerEntity;

    private final String name;

    public World(String name, WorldProperties properties) {
        this.properties = properties;
        this.name = name;

        for (int i = 1; i < 2; i++) {
            this.levels[i] = new Level(this, LevelDepth.byId(i), 128, 128);
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

    public void saveWorld() {
        BSOMap map = new BSOMap();
        map.put("spawnX", this.properties.getSpawnX());
        map.put("spawnY", this.properties.getSpawnY());
        map.put("time", this.properties.getTime());

        map.put("player", playerEntity.writeBSO());

        BSOList list = new BSOList();

        for (Entity e : this.getCurrentLevel().entities) {
            if (!(e instanceof PlayerEntity)) {
                list.add(e.writeBSO());
            }
        }

        map.put("entities", list);

        StringBSOWriter writer = new StringBSOWriter();
        System.out.println(writer.apply(map));
    }
}
