package me.kalmemarq.minicraft.util;

public class Vec2i {
    public static final Vec2i ZERO = new Vec2i(0, 0);

    private int x;
    private int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
