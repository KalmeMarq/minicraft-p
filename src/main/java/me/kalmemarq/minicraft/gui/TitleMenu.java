package me.kalmemarq.minicraft.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.util.JsonUtil;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.syncloader.ResourceLoader;

public class TitleMenu extends Menu {
    @Nullable
    private String splash;

    @Override
    protected void init() {
        addEntry(new SelectEntry("minicraft.menu.play", () -> {
            this.mc.setMenu(new PlayMenu(this));
        }));

        addEntry(new SelectEntry("minicraft.menu.options", () -> {
            this.mc.setMenu(new OptionsMenu(this));
        }));

        addEntry(new SelectEntry("minicraft.menu.help", () -> {
        })).setEnabled(false);

        addEntry(new SelectEntry("minicraft.menu.quit", () -> {
            this.mc.queueQuit();
        }));
    }

    @Override
    public void render() {
        if (splash == null) {
            splash = getSplash();
        }

        Renderer.render("title.png", Renderer.WIDTH / 2 - 60, 30);
        
        this.font.render("Version 1.0.0", 1, 1, 0x555555);

        if (splash != null) this.font.renderCentered(splash, Renderer.WIDTH / 2, 54, 0xFFFFFF);

        super.render();
    }

    private static final Random RANDOM = new Random();
    private static final List<String> splashes = new ArrayList<>();

    public static final ResourceLoader splashReloader = new ResourceLoader() {
        public void reload() {
            splashes.clear();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Language.class.getResourceAsStream("/splashes.json")))) {;
                JsonObject obj = JsonUtil.deserialize(reader);
                JsonArray arr = JsonUtil.getArray(obj, "splashes");

                for (JsonElement txt : arr) {
                    splashes.add(txt.getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    };

    @Nullable
    public static String getSplash() {
        if (splashes.size() == 0) {
            return null;
        } else {
            return splashes.get(RANDOM.nextInt(splashes.size()));
        }
    }
}
