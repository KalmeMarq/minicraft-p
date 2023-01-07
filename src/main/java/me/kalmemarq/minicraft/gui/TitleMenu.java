package me.kalmemarq.minicraft.gui;

import me.kalmemarq.minicraft.gfx.Renderer;

public class TitleMenu extends Menu {
    @Override
    protected void init() {
        addEntry(new SelectEntry("Play", () -> {
            this.mc.setMenu(new PlayMenu(this));
        }));

        addEntry(new SelectEntry("Options", () -> {
            this.mc.setMenu(new OptionsMenu(this));
        }));

        addEntry(new SelectEntry("Quit", () -> {
            this.mc.queueQuit();
        }));
    }

    @Override
    public void render() {
        Renderer.render("title.png", Renderer.WIDTH / 2 - 60, 30);

        this.font.render("Version 1.0.0", 1, 1, 0x555555);

        super.render();
    }
}
