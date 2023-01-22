package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class CactusTile extends Tile {
    public CactusTile() {
        super();
    }
    
    @Override
    public void render(World world, Level level, int x, int y) {
        Tiles.SAND.render(world, level, x, y);
        Renderer.render("tiles.png", x * 16, y * 16, 48, 0, 16, 16);
    }
}
