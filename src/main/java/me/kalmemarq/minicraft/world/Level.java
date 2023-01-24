package me.kalmemarq.minicraft.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.entity.Entity;
import me.kalmemarq.minicraft.tile.Tile.TileState;
import me.kalmemarq.minicraft.tile.Tiles;
import me.kalmemarq.minicraft.util.NoiseGenerator;
import me.kalmemarq.minicraft.util.math.AABB;

public class Level {
    private final Random random = new Random();

    private final World world;
    private final LevelDepth depth;

    private final int width;
    private final int height;

    private final TileState[] tiles;

    protected final List<Entity> entities = new ArrayList<>();
    private final List<Entity> entitiesAddQueue = new ArrayList<>(); 

    public Level(World world, LevelDepth depth, int width, int height) {
        this.world = world;
        this.depth = depth;

        this.tiles = new TileState[width * height];

        this.width = width;
        this.height = height;

        NoiseGenerator gen = new NoiseGenerator();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double s = gen.noise(x, y);

                if (s < -0.3) {
                    this.setTileState(x, y, Tiles.WATER.getDefaultState());
                } else if (s < -0.1) {
                    this.setTileState(x, y, Tiles.SAND.getDefaultState());
                } else if (s < 0.3) {
                    this.setTileState(x, y, Tiles.GRASS.getDefaultState());
                } else {
                    this.setTileState(x, y, Tiles.ROCK.getDefaultState());
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (this.getTileState(x, y).getTile() == Tiles.GRASS && random.nextInt(7) == 4) {
                    this.setTileState(x, y, Tiles.FLOWER.getDefaultState());
                } else if (this.getTileState(x, y).getTile() == Tiles.SAND && random.nextInt(7) == 4) {
                    this.setTileState(x, y, Tiles.CACTUS.getDefaultState());
                }
            }
        }
    }

    public void setTileState(int x, int y, TileState state) {
        this.tiles[y * width + x] = state;
    }

    public TileState getTileState(int x, int y) {
        if (y * width + x > this.tiles.length || y * width + x < 0) return Tiles.AIR.getDefaultState();
        TileState state = this.tiles[y * width + x];
        return state == null ? Tiles.AIR.getDefaultState() : state;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    public void tick() {
        for (Entity e : this.entitiesAddQueue) {
            this.entities.add(e);
        }

        this.entitiesAddQueue.clear();

        for (Entity e : this.entities) {
            e.tick();
        }
    }

    public void update() {}

    // TODO: Optimize this shit
    public void render() {
        Renderer.pushCamera();

        world.playerEntity.updateCamera();

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

        for (Entity e : this.entities) {
            e.render();
        }

        Renderer.popCamera();
    }

    public void addEntity(Entity e) {
        entitiesAddQueue.add(e);
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    public static int toTile(int pixel) {
        return pixel / 16;
    }

    public static int toPixel(int tile) {
        return tile * 16;
    }
}
