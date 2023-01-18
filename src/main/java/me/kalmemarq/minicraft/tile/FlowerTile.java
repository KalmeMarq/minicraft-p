package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class FlowerTile extends Tile {
    public FlowerTile() {
        super();
    }

    @Override
    public void render(World world, Level level, int x, int y) {
        Renderer.render("tiles.png", x * 16, y * 16, 24, 48, 16, 16);
        Renderer.render("tiles.png", x * 16, y * 16, 24, 48 + 16, 8, 8);
    }
}
