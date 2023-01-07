package me.kalmemarq.minicraft.world;

import java.util.Random;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.tile.Tile.TileState;
import me.kalmemarq.minicraft.tile.Tiles;
import me.kalmemarq.minicraft.util.AABB;

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

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    // TODO: Optimize this shit
    public void render() {
        AABB frustum = Renderer.getAABB();

        int tx0 = Level.toTile(frustum.minX()) - 1;
        int ty0 = Level.toTile(frustum.minY()) - 1;
        int tx1 = Level.toTile(frustum.maxX()) + 1;
        int ty1 = Level.toTile(frustum.maxY()) + 1;

        for (int x = tx0; x <= tx1; x++) {
            for (int y = ty0; y <= ty1; y++) {
                if (!this.inBounds(x, y)) continue;

                this.tiles[y * this.width + x].getTile().render(this.world, this, x, y);
            }
        }
    }

    public static int toTile(int pixel) {
        return pixel / 16;
    }

    public static int toPixel(int tile) {
        return tile * 16;
    }
}
