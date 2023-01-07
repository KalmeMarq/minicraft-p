package me.kalmemarq.minicraft.world;

public enum GameMode {
    SURVIVAL("minicraft.menu.gamemode.survival"),
    CREATIVE("minicraft.menu.gamemode.creative"),
    HARDCORE("minicraft.menu.gamemode.hardcore"),
    SCORE("minicraft.menu.gamemode.score");

    private final String name;

    private GameMode(String name) {
        this.name = name;
    }

    public String getName() {
      return this.name;
    }
}
