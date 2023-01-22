package me.kalmemarq.minicraft.client.gfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.resource.Resource;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class Font {
    private static final Identifier FONT_METADATA = new Identifier("font.json"); 
    private static final Identifier FONT = new Identifier("textures/default_font.png");

    private static final Map<Integer, GlyphInfo> glyphInfos = Maps.newHashMap();

    public static final ResourceReloader reloader = new SyncResourceReloader() {
        @Override
        protected void reload(ResourceManager manager) {
            Resource res = manager.getResource(FONT_METADATA);

            if (res != null) {
                glyphInfos.clear();

                try (BufferedReader reader = res.getAsReader()) {
                    JsonNode obj = Util.Json.parse(reader);
                    
                    if (Util.Json.hasArray(obj, "chars")) {
                        
                        int r = 0;
                        for (JsonNode el : obj.get("chars")) {
                            int[] codePoints = el.asText().codePoints().toArray();
                        
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

        if ((color >> 24 & 0xFF) == 0) {
            color |= 0xFF << 24;
        }

        int xx = x;

        int[] chrs = text.codePoints().toArray();

        for (int i = 0; i < chrs.length; i++) {
            int chr = chrs[i];

            if (!Character.isWhitespace(chr)) {
                GlyphInfo ii = glyphInfos.get(chr);

                if (ii != null) {
                    Renderer.renderColoredTexturedQuad(FONT, xx, y, 8, 8, ii.u, ii.v, color);
                }
            }

            xx += 8;
        }
    }

    public int width(String text) {
        return text.length() * 8;
    }

    public static int getWidth(String text) {
        return text.length() * 8;
    }

    private String toUpperCase(String text) {
        return text.toUpperCase(Language.language.getLocale());
    }

    static class GlyphInfo {
        int u;
        int v;
    }
}
