package me.kalmemarq.minicraft.util.math;

public class AABB {
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public AABB(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public int minX() {
        return this.minX;
    }
    
    public int minY() {
        return this.minY;
    }

    public int maxX() {
        return this.maxX;
    }
    
    public int maxY() {
        return this.maxY;
    }

    @Override
    public String toString() {
        return String.format("AABB[minX=%d,minY=%d,maxX=%d,maxY=%d]", this.minX, this.minY, this.maxX, this.maxY);
    }
}
