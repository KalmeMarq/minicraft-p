package me.kalmemarq.minicraft.gfx;

public class Font {
    private static final String charInfo = "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345" + "6789.,!?'\"-+=/\\%()<>:;"; 
    
    public Font() {
    }

    public void render(String text, int x, int y) {
        render(text, x, y, 0xFF_FF_FF);
    } 

    public void renderCentered(String text, int x, int y, int color) {
        render(text, x - width(text) / 2, y, color);
    }

    public void render(String text, int x, int y, int color) {
        text = toUpperCase(text);


        int xx = x;
        for (int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);

            if (!Character.isWhitespace(chr)) {
                int ii = charInfo.indexOf(chr);

                int u = ii % 32;
                int v = ii / 32;

                Renderer.render("font.png", xx, y, u * 8, v * 8, 8, 8, color);
            }

            xx += 8;
        }
    }

    public int width(String text) {
        return text.length() * 8;
    }

    // hm
    public static int getWidth(String text) {
        return text.length() * 8;
    }

    private String toUpperCase(String text) {
        char[] chrs = text.toCharArray();

        for (int i = 0; i < chrs.length; i++) {
            chrs[i] = Character.toUpperCase(chrs[i]);
        }

        return text.toUpperCase();
    }
}
