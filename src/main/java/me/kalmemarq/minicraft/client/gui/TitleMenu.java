package me.kalmemarq.minicraft.client.gui;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Keys;

public class TitleMenu extends Menu {
    private static final Identifier TITLE = new Identifier("textures/title.png");

    @Nullable
    private String splash;
    private int splashColor;

    private int ticks;

    @Override
    protected void init() {
        addEntry(new SelectEntry("minicraft.menu.play", () -> {
            this.mc.setMenu(new PlayMenu(this));
        }), 20, 0);

        addEntry(new SelectEntry("Multiplayer", () -> {})).setEnabled(false);;

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
            this.splash = this.mc.textManager.getSplash();
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
    public void update() {
        if (this.splash == null) {
            this.splash = this.mc.textManager.getSplash();
        }

        if (this.splash != null) {
            int bc = this.ticks / 8 % 2 == 0 ? 5 : 3;
            int r = 50 * bc;
            int g = 50 * bc;
            int b = 25 * bc;
            this.splashColor = r << 16 | g << 8 | b;
        }
    }

    @Override
    public void render() {
        Renderer.enableTexture();
        Renderer.renderTexturedQuad(TITLE, Renderer.WIDTH / 2 - 60, 30, 120, 16);
        Renderer.disableTexture();

        this.font.render("Version 1.0.0", 1, 1, 0x555555);

        if (this.splash != null) {
            this.font.renderCentered(splash, Renderer.WIDTH / 2, 64, this.splashColor);
        }

        super.render();

        this.font.renderCentered("(Up, Down to Select)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9 - 10 - 10, 0xFF444444);
        this.font.renderCentered("(Enter to Accept)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9 - 10, 0xFF444444);
        this.font.renderCentered("(Escape to Return)", Renderer.WIDTH / 2, Renderer.HEIGHT - 9, 0xFF444444);
    }
}
