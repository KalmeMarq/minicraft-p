package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class WaterTile extends Tile {
    public WaterTile() {
        super();
    }

    @Override
    public void render(World world, Level level, int x, int y) {
        Renderer.render("tiles.png", x * 16, y * 16, 16, 0, 8, 8);
        Renderer.render("tiles.png", x * 16 + 8, y * 16, 16 + 8, 0, 8, 8);
        Renderer.render("tiles.png", x * 16, y * 16 + 8, 16 + 16, 0, 8, 8);
        Renderer.render("tiles.png", x * 16 + 8, y * 16 + 8, 16 + 16 + 8, 0, 8, 8);
    }
}
