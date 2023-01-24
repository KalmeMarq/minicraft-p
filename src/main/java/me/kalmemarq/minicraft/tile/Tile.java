package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.util.CollisionBox;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class Tile {
    public CollisionBox collisionBox = new CollisionBox(16, 16);

    private final int health;

    public Tile() {
        this(0);
    }

    public Tile(int health) {
        this.health = health;
    }

    public boolean connectsToGrass() {
        return false;
    }

    public TileState getDefaultState() {
        return new TileState(this);
    }

    public int getLightPower() {
        return 0;
    }

    public boolean interact(World world, Level level) {
        return false;
    }

    public boolean isSolid() {
        return false;
    }

    public void render(World world, Level level, int x, int y) {
    }

    public int getHealth() {
        return this.health;
    }

    public static class TileState {
        public Tile tile;
        public int health;

        public TileState(Tile tile) {
            this.tile = tile;
            this.health = tile.getHealth();
        }

        public Tile getTile() {
          return tile;
        }
    }
}
