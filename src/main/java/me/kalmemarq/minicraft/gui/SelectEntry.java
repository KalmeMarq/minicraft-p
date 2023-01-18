package me.kalmemarq.minicraft.gui;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.util.Keybinding;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.sound.SoundEvents;

public class SelectEntry extends Entry {
    private final SelectAction onSelect;
    private String text;

    public SelectEntry(String text, SelectAction onSelect) {
        this.text = text;
        this.onSelect = onSelect;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return Language.translate(text);
    }

    public boolean keyPressed(int code) {
        if (enabled && Keybinding.SELECT.test(code)) {
            this.onSelect.act();
            Minicraft.getInstance().soundManager.play(SoundEvents.CONFIRM);
            return true;
        }

        return false;
    }

    public int getWidth() {
        return Font.getWidth(text);
    }

    @FunctionalInterface
    public interface SelectAction {
        void act();
    }
}
