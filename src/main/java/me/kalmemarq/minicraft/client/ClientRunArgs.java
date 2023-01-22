package me.kalmemarq.minicraft.client;

import java.io.File;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.optionparser.ArgOption;

public record ClientRunArgs(boolean debug, boolean maximized, boolean fullscreen, File gameDir, int width, int height, @Nullable String serverIp, int serverPort) {
    public static ClientRunArgs fromArgs(ArgOption<Boolean> debug, ArgOption<Boolean> maximized, ArgOption<Boolean> fullscreen, ArgOption<File> gameDir, ArgOption<Integer> width, ArgOption<Integer> height, ArgOption<String> serverIp, ArgOption<Integer> serverPort) {
        return new ClientRunArgs(
            debug.getValue(),
            maximized.getValue(),
            fullscreen.getValue(),
            gameDir.getValue(),
            width.getValue(),
            height.getValue(),
            serverIp.getValue(),
            serverPort.getValue());
    }
}
