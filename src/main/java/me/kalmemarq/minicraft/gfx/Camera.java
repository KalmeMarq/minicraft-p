package me.kalmemarq.minicraft.gfx;

import me.kalmemarq.minicraft.util.MathHelper;

public class Camera {
    private int tx;
    private int ty;

    public void centerOn(int x, int y, int minX, int minY, int maxX, int maxY) {
        this.tx = MathHelper.clamp(x - (Renderer.WIDTH / 2), minX, maxX - Renderer.WIDTH);
        this.ty = MathHelper.clamp(y - (Renderer.HEIGHT / 2), minY, maxY - Renderer.HEIGHT);
    }

    public int getTx() {
        return tx;
    }

    public int getTy() {
        return ty;
    }
}
