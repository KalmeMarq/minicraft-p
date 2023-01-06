package me.kalmemarq.minicraft.util;

public class CollisionBox {
    private final int width;
    private final int height;

    public CollisionBox(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
