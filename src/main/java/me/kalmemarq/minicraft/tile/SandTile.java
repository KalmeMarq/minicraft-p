package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class SandTile extends Tile {
    public SandTile() {
        super();
    }

    @Override
    public void render(World world, Level level, int x, int y) {
        Renderer.render("tiles.png", x * 16, y * 16, 72, 48, 16, 16);
    }
}
