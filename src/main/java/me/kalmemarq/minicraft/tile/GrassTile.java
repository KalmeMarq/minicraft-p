package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class GrassTile extends Tile {
    public GrassTile() {
        super();
    }

    @Override
    public void render(World world, Level level, int x, int y) {
        Renderer.render("tiles.png", x * 16, y * 16, 24, 48, 16, 16);
    }
}
