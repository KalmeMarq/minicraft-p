package me.kalmemarq.minicraft.world;

import me.kalmemarq.minicraft.tile.Tile.TileState;
import me.kalmemarq.minicraft.tile.Tiles;

import java.util.Random;

public class Level {
    private final Random random = new Random();

    private final World world;

    private final int width;
    private final int height;

    private final TileState[] tiles;

    public Level(World world, int width, int height) {
        this.world = world;

        this.tiles = new TileState[width * height];

        this.width = width;
        this.height = height;

        for (int i = 0; i < tiles.length; i++) {
            if (this.random.nextInt(10) < 2) {
                this.tiles[i] = new TileState(Tiles.SAND);
            } else {
                this.tiles[i] = new TileState(Tiles.GRASS);
            }
        }
    }

    public void render() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.tiles[y * this.width + x].getTile().render(this.world, this, x, y);
            }
        }
    }
}
