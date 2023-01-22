package me.kalmemarq.minicraft.main;

import java.io.File;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.client.ClientRunArgs;
import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.util.OperatingSystem;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.optionparser.ArgOption;
import me.kalmemarq.optionparser.ArgOptionParser;

public class Main {
    public static void main(String[] args) {
        Util.Logging.setupCustomOutputs();
        ArgOptionParser optionParser = new ArgOptionParser();
        ArgOption<Boolean> debug = optionParser.accepts("debug", Boolean.class);
        ArgOption<Boolean> fullscreen = optionParser.accepts("fullscreen", Boolean.class);
        ArgOption<Boolean> maximized = optionParser.accepts("maximized", Boolean.class);
        ArgOption<File> gameDir = optionParser.accepts("gameDir", File.class).defaultsTo(new File(OperatingSystem.getOS().getAppData(), "playminicraft/mods/Minicraft_P"));
        ArgOption<Integer> width = optionParser.accepts("width", Integer.class).alias("w").defaultsTo(Renderer.WIDTH * 3);
        ArgOption<Integer> height = optionParser.accepts("height", Integer.class).alias("h").defaultsTo(Renderer.HEIGHT * 3);
        ArgOption<String> serverIp = optionParser.accepts("serverIp", String.class);
        ArgOption<Integer> serverPort = optionParser.accepts("serverPort", Integer.class).defaultsTo(25565);

        optionParser.parse(args);

        ClientRunArgs runArgs = ClientRunArgs.fromArgs(debug, maximized, fullscreen, gameDir, width, height, serverIp, serverPort);

        Minicraft mc;

        try {
            mc = new Minicraft(runArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            mc.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mc.stop();
        }

        System.exit(0);
    }
}
