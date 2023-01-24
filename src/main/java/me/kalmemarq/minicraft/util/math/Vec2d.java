package me.kalmemarq.minicraft.util.math;

public class Vec2d {
    public static final Vec2d ZERO = new Vec2d(0d, 0d);

    private double x;
    private double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void add(Vec2d vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public Vec2d addVector(Vec2d vec) {
        return new Vec2d(this.x + vec.x, this.y + vec.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Vec2i[x=" + this.x + ",y=" + this.y + "]";
    }
}
