package me.kalmemarq.minicraft.util;

public enum Direction {
    NORTH,
    WEST,
    EAST,
    SOUTH;

    public final static Direction UP = Direction.NORTH;
    public final static Direction LEFT = Direction.WEST;
    public final static Direction RIGHT = Direction.EAST;
    public final static Direction DOWN = Direction.SOUTH;

    public static Direction get(int x, int y) {
        if (Math.abs(x) >= Math.abs(y)) {
            return x < 0 ? WEST : EAST;
        }

        return y < 0 ? NORTH : SOUTH;
    }
}
