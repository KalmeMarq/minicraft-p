package me.kalmemarq.minicraft.client.gui;

import me.kalmemarq.minicraft.client.gfx.Renderer;

public class PauseMenu extends Menu {
    public PauseMenu() {
        super();
    }
    
    @Override
    protected void init() {
        addEntry(new SelectEntry("minicraft.menu.return_to_game", () -> {
            this.mc.setMenu(null);
        }));

        addEntry(new SelectEntry("minicraft.menu.options", () -> {
            this.mc.setMenu(new OptionsMenu(this));
        }));

        addEntry(new SelectEntry("minicraft.menu.return_to_title", () -> {
            this.mc.world = null;
            this.mc.setMenu(new TitleMenu());
        }));
    }

    @Override
    public void render() {
        if (entriesHeight == 0) {
            calcEntriesHeight();
        }

        int panelH = entriesHeight + 16;
        if (panelH % 8 != 0) {
            do {
                ++panelH;
            } while (panelH % 8 != 0);
        }

        // What the fuck is happening with the render panel
        Renderer.renderColoredQuad(Renderer.WIDTH / 2 - 112, (Renderer.HEIGHT - panelH) / 2, 224, panelH, 0xFF_00_00_88);

        super.render();
    }
}
