package me.kalmemarq.minicraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.client.Minicraft;
import me.kalmemarq.minicraft.client.gfx.Font;
import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.client.util.Keybinding;
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.sound.SoundEvents;

public class Menu {
    protected Minicraft mc;
    protected Font font;

    @Nullable
    protected Menu parentMenu;

    private int selected = 0;
    protected final List<MenuElement<?>> entries = new ArrayList<>();
    protected int entryGap = 2;
    protected MenuAlign menuAlign = MenuAlign.CENTER;
    protected int entriesHeight = 0;

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

    protected void calcEntriesHeight() {
        entriesHeight = 0;
        
        for (MenuElement<?> el : entries) {
            entriesHeight += el.getHeight();
        }

        if (entries.size() > 0) {
            entriesHeight += (entries.size() - 1) * entryGap;
        }
    }

    public void update() {}

    public void render() {
        int y = 20;

        if (entriesHeight == 0) {
           calcEntriesHeight();
        }

        if (menuAlign == MenuAlign.LEFT_MIDDLE || menuAlign == MenuAlign.CENTER) {
            y = (Renderer.HEIGHT - (entriesHeight)) / 2;
        } else if (menuAlign == MenuAlign.BOTTOM_LEFT || menuAlign == MenuAlign.BOTTOM_MIDDLE) {
            y = (Renderer.HEIGHT - (entriesHeight));
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
        if (entries.size() > 0 && this.entries.get(selected).getEntry().keyPressed(code)) {
            return;
        } else if (Keybinding.SELECT_DOWN.test(code, entries.size() > 0 && this.entries.get(selected).entry instanceof InputEntry ? Keys.KEY_S : 0)) {
            do {
                cycleSelection(1);
            } while (!entries.get(selected).getEntry().enabled);

            this.mc.soundManager.play(SoundEvents.SELECT);
        } else if (Keybinding.SELECT_UP.test(code, entries.size() > 0 && entries.get(selected).entry instanceof InputEntry ? Keys.KEY_W : 0)) {
            do {
                cycleSelection(-1);
            } while (!entries.get(selected).getEntry().enabled);

            this.mc.soundManager.play(SoundEvents.SELECT);
        } else if (Keybinding.EXIT.test(code) && !(this instanceof TitleMenu)) {
            this.mc.soundManager.play(SoundEvents.CONFIRM);
            this.mc.setMenu(this.parentMenu);
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
        private int paddingUp = 0;
        private int paddingDown = 0;

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
