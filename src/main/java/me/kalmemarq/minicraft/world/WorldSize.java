package me.kalmemarq.minicraft.world;

public enum WorldSize {
    INFINITE(-1, "minicraft.menu.world_size.infinite"),
    SMALL(128, "minicraft.menu.world_size.small"),
    NORMAL(256, "minicraft.menu.world_size.normal"),
    LARGE(512, "minicraft.menu.world_size.large");

    private final int size;
    private final String name;

    private WorldSize(int size, String name) {
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
      return size;
    }
}
