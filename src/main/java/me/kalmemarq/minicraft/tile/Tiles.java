package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;

public class Tiles {
    public static Registry<Identifier, Tile> TILE_REGISTRY = new Registry<>();
    
    public static Tile register(String id, Tile tile) {
        return register(new Identifier(id), tile);
    }

    public static Tile register(Identifier id, Tile tile) {
        TILE_REGISTRY.add(id, tile);
        return tile;
    }

    public static Tile AIR = Tiles.register("air", new Tile());
    public static Tile GRASS = Tiles.register("grass", new GrassTile());
    public static Tile SAND = Tiles.register("sand", new SandTile());
    public static Tile ROCK = Tiles.register("rock", new RockTile());
    public static Tile WATER = Tiles.register("water", new WaterTile());
    public static Tile FLOWER = Tiles.register("flower", new FlowerTile());
    public static Tile CACTUS = Tiles.register("cactus", new CactusTile());
}
