package me.kalmemarq.minicraft.client.gfx;

import java.util.ArrayList;
import java.util.List;

public class Color {
    public static final Color BLACK = new Color('0', 0x00_00_00);
    public static final Color WHITE = new Color('f', 0xFF_FF_FF);
    public static final Color RED = new Color('1', 0xFF_00_00);

    private final char code;
    private final int color;

    private Color(char code, int color) {
        this.code = code;
        this.color = color;
    }

    public char getCode() {
      return code;
    }

    public int getColor() {
      return color;
    }

    public static final List<Color> Colors = new ArrayList<>(); 
    
    public static Color byCode(char code) {
        for (int i = 0; i < Colors.size(); i++) {
            if (Colors.get(i).getCode() == code) {
                return Colors.get(i);
            } 
        }

        return Color.WHITE;
    }
}
