package me.kalmemarq.minicraft.tile;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.world.Level;
import me.kalmemarq.minicraft.world.World;

public class GrassTile extends Tile {
    public GrassTile() {
        super();
    }

    @Override
    public boolean connectsToGrass() {
        return true;
    }

    private int dirtC = 0xFF533b25;

    @Override
    public void render(World world, Level level, int x, int y) {
        boolean top = level.getTileState(x, y - 1).getTile().connectsToGrass();
        boolean bottom = level.getTileState(x, y + 1).getTile().connectsToGrass();
        boolean left = level.getTileState(x - 1, y).getTile().connectsToGrass();
        boolean right = level.getTileState(x + 1, y).getTile().connectsToGrass();
        boolean bottomLeft = level.getTileState(x - 1, y + 1).getTile().connectsToGrass();
        boolean bottomRight = level.getTileState(x + 1, y + 1).getTile().connectsToGrass();
        boolean topLeft = level.getTileState(x - 1, y - 1).getTile().connectsToGrass();
        boolean topRight = level.getTileState(x + 1, y - 1).getTile().connectsToGrass();

        int xx = x * 16;
        int yy = y * 16;

        if (!left && !top) {
            Renderer.render("tiles.png", xx, yy, 0, 48, 8, 8, 0xFF533b25);
        } else {
            Renderer.render("tiles.png", xx, yy, 24, 48, 8, 8);
        }

        if (!top && !right) {
            Renderer.render("tiles.png", xx + 8, yy, 16, 48, 8, 8, 0xFF533b25);
        } else {
            Renderer.render("tiles.png", xx + 8, yy, 24 + 8, 48, 8, 8);
        }

        // if (!bottomLeft && !bottom && !bottomRight) {
        //     Renderer.render("tiles.png", xx, yy + 8, 8, 48 + 16, 8, 8, 0xFF533b25);
        //     Renderer.render("tiles.png", xx + 8, yy + 8, 8, 48 + 16, 8, 8, 0xFF533b25);
        // } else {
        if ((!left && !bottom) || (!bottomLeft && !bottom && !left)) {
            Renderer.render("tiles.png", xx, yy + 8, 0, 48 + 16, 8, 8, 0xFF533b25);
        } else if (!bottom && !left) {
            Renderer.render("tiles.png", xx, yy + 8, 8, 48 + 16, 8, 8, 0xFF533b25);
        } else {
            Renderer.render("tiles.png", xx, yy + 8, 24, 48 + 8, 8, 8);
        }

        if ((!bottom && !right) || (!bottomRight && !bottom && !right)) {
            Renderer.render("tiles.png", xx + 8, yy + 8, 16, 48 + 16, 8, 8, 0xFF533b25);
        } else if (!bottom && !right) {
            Renderer.render("tiles.png", xx + 8, yy + 8, 8, 48 + 16, 8, 8, 0xFF533b25);
        } else {
            Renderer.render("tiles.png", xx + 8, yy + 8, 24 + 8, 48 + 8, 8, 8);
        }
        // }
    }
}
