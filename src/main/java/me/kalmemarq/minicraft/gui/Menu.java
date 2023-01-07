package me.kalmemarq.minicraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.util.Keybinding;
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.Sound;

public class Menu {
    protected Minicraft mc;
    protected Font font;

    @Nullable
    protected Menu parentMenu;

    private int selected = 0;
    protected final List<MenuElement<?>> entries = new ArrayList<>();
    protected int entryGap = 2;
    protected MenuAlign menuAlign = MenuAlign.CENTER;
    private int entriesHeight = 0;

    public Menu() {
    }

    public Menu(@Nullable Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void init(Minicraft mc) {
        this.mc = mc;
        this.font = mc.font;
        entries.clear();
        this.init();
    }

    protected void init() {
    }

    public <T extends Entry> T addEntry(T entry) {
        this.entries.add(new MenuElement<>(entry));
        return entry;
    }

    public <T extends Entry> T addEntry(T entry, int paddingUp, int paddingDown) {
        this.entries.add(new MenuElement<>(entry).padding(paddingUp, paddingDown));
        return entry;
    } 

    public void render() {
        int y = 20;

        if (entriesHeight == 0) {
            for (MenuElement<?> el : entries) {
                entriesHeight += el.getHeight();
            }
        }

        if (menuAlign == MenuAlign.LEFT_MIDDLE || menuAlign == MenuAlign.CENTER) {
            y = (Renderer.HEIGHT - (entriesHeight + (entries.size() - 1) * entryGap)) / 2;
        } else if (menuAlign == MenuAlign.BOTTOM_LEFT || menuAlign == MenuAlign.BOTTOM_MIDDLE) {
            y = (Renderer.HEIGHT - (entriesHeight + (entries.size() - 1) * entryGap));
        }

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i).getEntry();
            int c = !entry.enabled ? 0x555555 : i == selected ? 0xFFFFFF : 0x888888;

            String txt = entry.getText();
            if (i == selected) txt = "> " + txt + " <"; 

            int lx = i == selected ? 20 - Font.getWidth("> ") : 20;

            int yy = y + entries.get(i).getPaddingUp();

            switch (menuAlign) {
                case TOP_LEFT:
                    this.font.render(txt, lx, yy, c);
                    break;
                case TOP_MIDDLE:
                    this.font.renderCentered(txt, Renderer.WIDTH / 2, yy, c);
                    break;
                case LEFT_MIDDLE:
                    this.font.render(txt, lx, yy, c);
                    break;
                case CENTER:
                    this.font.renderCentered(txt, Renderer.WIDTH / 2, yy, c);
                    break;
                case BOTTOM_LEFT:
                    this.font.render(txt, lx, y, c);
                    break;
                case BOTTOM_MIDDLE:
                    if (i == selected) this.font.renderCentered(txt, Renderer.WIDTH / 2, yy, c);
                    break;
            }

            y += entryGap + entries.get(i).getHeight();
        }
    }

    public void keyPressed(int code) {
        if (code == Keys.KEY_ARROW_DOWN) {
            do {
                cycleSelection(1);
            } while (!entries.get(selected).getEntry().enabled);

            Sound.play(Sound.SELECT);
        } else if (code == Keys.KEY_ARROW_UP) {
            do {
                cycleSelection(-1);
            } while (!entries.get(selected).getEntry().enabled);

            Sound.play(Sound.SELECT);
        } else if (Keybinding.EXIT.test(code) && !(this instanceof TitleMenu)) {
            this.mc.setMenu(this.parentMenu);
        } else {
            if (entries.size() == 0) return;
            this.entries.get(selected).getEntry().keyPressed(code);
        }
    }

    private void cycleSelection(int dir) {
        selected = Math.floorMod(selected + dir, this.entries.size());
    }

    public void charTyped(char chr) {
        if (entries.size() == 0) return;

        this.entries.get(selected).getEntry().charTyped(chr);
    }
    
    public void tick() {}

    public enum MenuAlign {
        TOP_LEFT,
        TOP_MIDDLE,
        LEFT_MIDDLE,
        CENTER,
        BOTTOM_LEFT,
        BOTTOM_MIDDLE;
    }

    public static class MenuElement<T extends Entry> {
        private final T entry;
        private int paddingUp;
        private int paddingDown;

        public MenuElement(T entry) {
            this.entry = entry;
        }

        public T getEntry() {
          return this.entry;
        }

        public int getPaddingUp() {
          return paddingUp;
        }

        public int getHeight() {
            return 8 + paddingUp + paddingDown;
        }

        public MenuElement<T> padding(int up, int down) {
            this.paddingUp = up;
            this.paddingDown = down;
            return this;
        }
    }
}
