package me.kalmemarq.minicraft.world;

public enum WorldSize {
    INFINITE(-1, "Infinite"),
    SMALL(128, "Small"),
    NORMAL(256, "Normal"),
    LARGE(512, "Large");

    private final int size;
    private final String name;

    private WorldSize(int size, String name) {
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
