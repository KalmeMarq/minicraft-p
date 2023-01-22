package me.kalmemarq.minicraft.server;

import me.kalmemarq.optionparser.ArgOption;

public record ServerRunArgs(int port) {
    public static ServerRunArgs fromArgs(ArgOption<Integer> serverPort) {
        return new ServerRunArgs(serverPort.getValue());
    }
}
