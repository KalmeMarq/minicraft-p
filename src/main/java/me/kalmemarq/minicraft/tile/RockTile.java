package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class RockTile extends Tile {
    public RockTile() {
        super();
    }

    @Override
    public void render(World world, Level level, int x, int y) {
        Renderer.render("tiles.png", x * 16, y * 16, 152 + 16, 48, 16, 16);
    }
}
