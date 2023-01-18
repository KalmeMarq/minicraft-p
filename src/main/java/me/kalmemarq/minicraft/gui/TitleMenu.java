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
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.loader.SyncResourceReloader;

public class TitleMenu extends Menu {
    @Nullable
    private String splash;

    private int ticks;

    @Override
    protected void init() {
        addEntry(new SelectEntry("minicraft.menu.play", () -> {
            this.mc.setMenu(new PlayMenu(this));
        }), 20, 0);

        addEntry(new SelectEntry("minicraft.menu.options", () -> {
            this.mc.setMenu(new OptionsMenu(this));
        }));

        addEntry(new SelectEntry("minicraft.menu.help", () -> {
            this.mc.setMenu(new HelpMenu(this));
        }));

        addEntry(new SelectEntry("minicraft.menu.quit", () -> {
            this.mc.queueQuit();
        }));
    }

    @Override
    public void keyPressed(int code) {
        if (code == Keys.KEY_R) {
            this.splash = getSplash();
        } else {
            super.keyPressed(code);
        }
    }

    @Override
    public void tick() {
        super.tick();

        ++this.ticks;
    }

    @Override
    public void render() {
        if (splash == null) {
            splash = getSplash();
        }

        Renderer.render("title.png", Renderer.WIDTH / 2 - 60, 30);
        
        this.font.render("Version 1.0.0", 1, 1, 0x555555);

        if (splash != null) {
            int bc = this.ticks / 200 % 2 == 0 ? 5 : 3;
            int r = 50 * bc;
            int g = 50 * bc;
            int b = 25 * bc;
            this.font.renderCentered(splash, Renderer.WIDTH / 2, 64, r << 16 | g << 8 | b);
        }

        super.render();

        this.font.renderCentered("(Up, Down to Select)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9 - 10 - 10, 0xFF444444);
        this.font.renderCentered("(Enter to Accept)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9 - 10, 0xFF444444);
        this.font.renderCentered("(Escape to Return)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9, 0xFF444444);
    }

    private static final Random RANDOM = new Random();
    private static final List<String> splashes = new ArrayList<>();

    public static final ResourceReloader splashReloader = new SyncResourceReloader() {
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
