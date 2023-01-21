package me.kalmemarq.minicraft.main;

import java.io.File;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.main.optionparser.ArgOption;
import me.kalmemarq.minicraft.main.optionparser.ArgOptionParser;
import me.kalmemarq.minicraft.util.OperatingSystem;

public class Main {
    public static void main(String[] args) {
        ArgOptionParser optionParser = new ArgOptionParser();
        
        ArgOption<Boolean> debug = optionParser.accepts("debug", Boolean.class);
        ArgOption<Boolean> fullscreen = optionParser.accepts("fullscreen", Boolean.class);
        ArgOption<Boolean> maximized = optionParser.accepts("maximized", Boolean.class);
        ArgOption<File> gameDir = optionParser.accepts("gameDir", File.class).defaultsTo(new File(OperatingSystem.getOS().getAppData(), "playminicraft/mods/Minicraft_P"));
        ArgOption<Integer> width = optionParser.accepts("width", Integer.class).alias("w").defaultsTo(Renderer.WIDTH * 3);
        ArgOption<Integer> height = optionParser.accepts("height", Integer.class).alias("h").defaultsTo(Renderer.HEIGHT * 3);
        
        optionParser.parse(args);

        RunArgs runArgs = new RunArgs(
            debug.getValue(),
            maximized.getValue(),
            fullscreen.getValue(),
            gameDir.getValue(),
            width.getValue(),
            height.getValue());

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
            mc.close();
        }

        System.exit(0);
    }
}
