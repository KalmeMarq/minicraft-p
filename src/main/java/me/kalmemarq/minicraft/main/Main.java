package me.kalmemarq.minicraft.main;

import java.io.File;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.main.optionparser.ArgOption;
import me.kalmemarq.minicraft.main.optionparser.ArgOptionParser;
import me.kalmemarq.minicraft.util.OperatingSystem;

public class Main {
    public static void main(String[] args) {
        // int colorSrc = 153 << 24 | 0 << 16 | 255 << 8 | 0;
        // int colorDst = 255 << 24 | 255 << 16 | 0 << 8 | 0;

        // int color = BlendEquation.FUNC_ADD.calc(colorSrc, colorDst, BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

        // System.out.print("Color Src: ");
        // System.out.print("(");
        // System.out.print(colorSrc >> 16 & 0xFF);
        // System.out.print(",");
        // System.out.print(colorSrc >> 8 & 0xFF);
        // System.out.print(",");
        // System.out.print(colorSrc & 0xFF);
        // System.out.print(",");
        // System.out.print(colorSrc >> 24 & 0xFF);
        // System.out.print(") ");

        // System.out.print("(");
        // System.out.print((colorSrc >> 16 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorSrc >> 8 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorSrc & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorSrc >> 24 & 0xFF) / 255.0f);
        // System.out.println(")");
       
        // System.out.print("Color Dst: ");
        // System.out.print("(");
        // System.out.print(colorDst >> 16 & 0xFF);
        // System.out.print(",");
        // System.out.print(colorDst >> 8 & 0xFF);
        // System.out.print(",");
        // System.out.print(colorDst & 0xFF);
        // System.out.print(",");
        // System.out.print(colorDst >> 24 & 0xFF);
        // System.out.print(") ");

        // System.out.print("(");
        // System.out.print((colorDst >> 16 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorDst >> 8 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorDst & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((colorDst >> 24 & 0xFF) / 255.0f);
        // System.out.println(")");

        // System.out.print("Color Result: ");
        // System.out.print("(");
        // System.out.print(color >> 16 & 0xFF);
        // System.out.print(",");
        // System.out.print(color >> 8 & 0xFF);
        // System.out.print(",");
        // System.out.print(color & 0xFF);
        // System.out.print(",");
        // System.out.print(color >> 24 & 0xFF);
        // System.out.print(") ");

        // System.out.print("(");
        // System.out.print((color >> 16 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((color >> 8 & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((color & 0xFF) / 255.0f);
        // System.out.print(",");
        // System.out.print((color >> 24 & 0xFF) / 255.0f);
        // System.out.println(")");

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
