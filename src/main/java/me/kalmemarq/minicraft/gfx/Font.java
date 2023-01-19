package me.kalmemarq.minicraft.gfx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.minicraft.util.JsonUtil;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class Font {
    // private static final String charInfo = "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345"+
    // "6789.,!?'\"-+=/\\%()<>:;^@ÁÉÍÓÚÑ¿¡"+
    // "ÃÊÇÔÕĞÇÜİÖŞÆØÅŰŐ[]#|{}_АБВГДЕЁЖЗ"+
    // "ИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯÀÂÄÈÎÌÏÒ"+
    // "ÙÛÝ*«»£$&€§ªº"; 

    static class GlyphInfo {
        public int u;
        public int v;
    }

    private static final Map<Integer, GlyphInfo> glyphInfos = Maps.newHashMap();

    public static final ResourceReloader reloader = new SyncResourceReloader() {
        @Override
        protected void reload(ResourceManager manager) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Language.class.getResourceAsStream("/font.json")))) {;
                JsonObject obj = JsonUtil.deserialize(reader);
                JsonArray arr = JsonUtil.getArray(obj, "chars");
    
                glyphInfos.clear();

                int r = 0;
                for (JsonElement txt : arr) {
                    int[] codePoints = txt.getAsString().codePoints().toArray();
                    
                    int c = 0;
                    for (int o : codePoints) {
                        if (o != 0) {
                            GlyphInfo info = new GlyphInfo();
                            info.u = c * 8;
                            info.v = r * 8; 
                        
                            glyphInfos.put(o, info);
                        }

                        ++c;
                    }

                    ++r;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String txt = "IHATEYOUÁ";
            int[] c = txt.codePoints().toArray();

            for (int cc : c) {
                GlyphInfo info = glyphInfos.get(cc);

                if (info != null) {
                    System.out.println(Character.toString(cc) + ": " + info.u + "," + info.v);
                }
            }

        }
    };
    
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

        int[] chrs = text.codePoints().toArray();

        for (int i = 0; i < chrs.length; i++) {
            int chr = chrs[i];

            if (!Character.isWhitespace(chr)) {
                GlyphInfo ii = glyphInfos.get(chr);

                if (ii != null) {
                    Renderer.render("default_font.png", xx, y, ii.u, ii.v, 8, 8, color);
                }
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
        return text.toUpperCase(Language.language.getLocale());
    }
}
