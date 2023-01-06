package me.kalmemarq.minicraft.gui;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.util.Keybinding;
import org.jetbrains.annotations.Nullable;

public class Menu {
    protected Minicraft mc;
    protected Font font;

    @Nullable
    protected Menu parentMenu;

    public Menu() {
    }

    public Menu(@Nullable Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void init(Minicraft mc) {
        this.mc = mc;
        this.font = mc.font;
        this.init();
    }

    protected void init() {
    }

    public void render() {
    }

    public void keyPressed(int code) {
        if (Keybinding.EXIT.test(code)) {
            this.mc.setMenu(this.parentMenu);
        }
    }
    
    public void tick() {}
}
