package me.kalmemarq.minicraft.world;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.util.Keybinding;

public class World {
    private final Level[] levels = new Level[LevelDepth.values().length];
    private final WorldProperties properties;

    private int currentDepth = LevelDepth.SURFACE.getId();

    public World(WorldProperties properties) {
        this.properties = properties;

        for (int i = 1; i < 2; i++) {
            this.levels[i] = new Level(this, 128, 128);
        }
    }

    protected int playerX = 0;
    protected int playerY = 0;

    protected boolean up;
    protected boolean down;
    protected boolean right;
    protected boolean left;

    public void wtf(int code, int action) {
        if (Keybinding.MOVE_DOWN.test(code)) {
            down = action == 1;
            playerY += 2;
        }
        
        if (Keybinding.MOVE_UP.test(code)) {
            up = action == 1;
            playerY -= 2;
        }
        
        if (Keybinding.MOVE_LEFT.test(code)) {
            left = action == 1;
            playerX -= 2;
        }

        if (Keybinding.MOVE_RIGHT.test(code)) {
            right = action == 1;
            playerX += 2;
        }
    }
    
    public void tick() {
        // if (down) {
        //     ++playerY;
        // }
        
        // if (up) {
        //     --playerY;
        // }

        // if (left) {
        //     --playerX;
        // }

        // if (right) {
        //     ++playerX;
        // }
    }

    public void render() {
        Level currentLevel = this.getCurrentLevel();

        Renderer.pushCamera();

        Renderer.camera.setTx(playerX + 64);
        Renderer.camera.setTy(playerY + 64);

        // System.out.println(playerX + "," + playerY);
        // Renderer.camera.centerOn(playerX + 64, playerX + 64, 0, 0, Level.toPixel(128), Level.toPixel(128));

        currentLevel.render();

        Renderer.popCamera();
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public Level getCurrentLevel() {
        return this.levels[this.currentDepth];
    }
}
