package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Registry;

public class Tiles {
    public static Registry<Tile> TILE_REGISTRY = new Registry<>();

    public static Tile AIR = register("air", new Tile());
    public static Tile GRASS = register("grass", new GrassTile());
    public static Tile SAND = register("sand", new SandTile());
    public static Tile ROCK = register("rock", new RockTile());
    public static Tile WATER = register("water", new WaterTile());
    public static Tile FLOWER = register("flower", new FlowerTile());
    public static Tile CACTUS = register("cactus", new CactusTile());

    public static Tile register(String id, Tile tile) {
        return register(new Identifier(id), tile);
    }

    public static Tile register(Identifier id, Tile tile) {
        TILE_REGISTRY.add(id, tile);
        return tile;
    }
}
