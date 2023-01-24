package me.kalmemarq.minicraft.world;

public enum LevelDepth {
    SKY(0, "Sky"),
    SURFACE(1, "Surface"),
    IRON(2, "Iron"),
    GOLD(3, "Gold"),
    LAVA(4, "Lava"),
    DUNGEON(5, "Dungeon");

    private final int id;
    private final String name;

    private LevelDepth(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static LevelDepth byId(int id) {
        for (LevelDepth depth : LevelDepth.values()) {
            if (id == depth.id) {
                return depth;
            }
        }

        return null;
    }
}
