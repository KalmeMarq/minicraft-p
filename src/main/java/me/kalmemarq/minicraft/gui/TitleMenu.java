package me.kalmemarq.minicraft.gui;

import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.Sound;
import me.kalmemarq.minicraft.world.World;
import me.kalmemarq.minicraft.world.WorldProperties;

public class TitleMenu extends Menu {
    private int selected = 0;

    private String[] entries = {
        "Play",
//        "Options",
        "Quit"
    };

    @Override
    public void keyPressed(int code) {
        if (code == Keys.KEY_ARROW_DOWN) {
            selected += 1;
            Sound.SELECT.play();
        
            if (selected >= entries.length) {
                selected = 0;
            }
        } else if (code == Keys.KEY_ARROW_UP) {
            selected -= 1;
            Sound.SELECT.play();
        
            if (selected < 0) {
                selected = entries.length - 1;
            }
        } else if (code == Keys.KEY_ENTER) {
            if (selected == 0) {
                this.mc.world = new World(new WorldProperties(0, 0));

                this.mc.setMenu(null);
            } else if (selected == 1) {
                this.mc.queueQuit();
            }

            Sound.CONFIRM.play();
        }
    }

    @Override
    public void render() {

        // Renderer.enableScissor();
        // Renderer.scissor(0, 0, Renderer.WIDTH / 2, 42);
//        Renderer.enableBlend();
//        Renderer.defaultBlendFunc();
        // Renderer.fill(255 << 24 | 255 << 16 | 0 << 8 | 255);

        // Renderer.enableTexture();
        // Renderer.setGTexture(Renderer.getImage("title.png"));
        // Renderer.renderTexturedQuad(0, 0, 120, 16);
        // Renderer.renderTexturedQuad(120, 30, 120, 16);
        // Renderer.disableTexture();
        Renderer.render("title.png", Renderer.WIDTH / 2 - 60, 30);
        // Renderer.enableDepthTest();
//        Renderer.renderGradientQuad(20, 20, 0, 120, 100, 0xFF_FF_00_FF, 0xFF_00_00_FF);
        
        // Renderer.renderColoredQuad(0, 0, 20, 20, 0xFF_FF_00_00);
        
        // Renderer.blendFuncSeparate(BlendFactor.ONE_MINUS_DST_COLOR, BlendFactor.ONE_MINUS_SRC_COLOR, BlendFactor.ONE, BlendFactor.ZERO);

        // Renderer.renderColoredQuad(10, 10, 20, 20, 0xFF_00_FF_FF);
        
        // Renderer.disableDepthTest();

//        Renderer.disableBlend();
        // Renderer.disableScissor();

        this.font.render("Version 1.0.0", 1, 1, 0x555555);

        for (int i = 0; i < entries.length; i++) {
            int c = i == selected ? 0xFFFFFF : 0x888888;

            if (i == selected) {
                this.font.renderCentered("> " + entries[i] + " <", Renderer.WIDTH / 2, 70 + i * 10, c);
            } else {
                this.font.renderCentered(entries[i], Renderer.WIDTH / 2, 70 + i * 10, c);
            }
        }
    }
}
