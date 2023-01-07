package me.kalmemarq.minicraft.world;

public enum GameMode {
    SURVIVAL("Survival"),
    CREATIVE("Creative"),
    HARDCORE("Hardcore"),
    SCORE("Score");

    private final String name;

    private GameMode(String name) {
        this.name = name;
    }

    public String getName() {
      return this.name;
    }
}
