package me.kalmemarq.minicraft.client.gfx;

import me.kalmemarq.minicraft.util.math.MathHelper;

public class Camera {
    private int tx;
    private int ty;

    public void centerOn(int x, int y, int minX, int minY, int maxX, int maxY) {
        this.tx = MathHelper.clamp(x - (Renderer.WIDTH / 2), minX, maxX - Renderer.WIDTH);
        this.ty = MathHelper.clamp(y - (Renderer.HEIGHT / 2), minY, maxY - Renderer.HEIGHT);
    }

    public int tx() {
        return tx;
    }

    public int ty() {
        return ty;
    }

    public void setTx(int x) {
        tx = x;
    }

    public void setTy(int y) {
        ty = y;
    }

    @Override
    public String toString() {
        return "Camera[tx=" + this.tx + ",ty=" + this.ty + "]";
    }
}
