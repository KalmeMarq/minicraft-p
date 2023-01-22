package me.kalmemarq.minicraft.client.gui;

import me.kalmemarq.minicraft.client.gfx.Renderer;

public class HelpMenu extends Menu {
    public HelpMenu(Menu parentMenu) {
        super(parentMenu);
    }

    @Override
    protected void init() {
        this.addEntry(new SelectEntry("Instructions", () -> {}));
        this.addEntry(new SelectEntry("Storyline Guide", () -> {}));
        this.addEntry(new SelectEntry("About", () -> {}));
        this.addEntry(new SelectEntry("Credits", () -> {}));
    }

    @Override
    public void render() {
        this.font.renderCentered("Help", Renderer.WIDTH / 2, Renderer.HEIGHT / 2 - 36, 0xFF_FFFFFF);
        
        super.render();
    }
}
