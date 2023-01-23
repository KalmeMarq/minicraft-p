package me.kalmemarq.minicraft.client.gui;

import java.util.List;

import me.kalmemarq.minicraft.client.Minicraft;
import me.kalmemarq.minicraft.client.gfx.Font;
import me.kalmemarq.minicraft.client.util.Keybinding;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.sound.SoundEvents;

public class CycleEntry<T> extends Entry {
    private final String label;
    private final List<T> values;
    private int index;
    private T currentValue;
    private final CycleEntryLabelGetter<T> labelGetter;
    private final CycleEntryOnChange<T> onChange;
    private final boolean cycle;

    public CycleEntry(String label, List<T> values, T defaultValue, CycleEntryLabelGetter<T> labelGetter, CycleEntryOnChange<T> onChange) {
        this(label, values, defaultValue, labelGetter, onChange, true);
    }

    public CycleEntry(String label, List<T> values, T defaultValue, CycleEntryLabelGetter<T> labelGetter, CycleEntryOnChange<T> onChange, boolean cycle) {
        this.label = label;
        this.values = values;
        this.currentValue = defaultValue;
        this.index = values.indexOf(defaultValue);
        this.labelGetter = labelGetter;
        this.onChange = onChange;
        this.cycle = cycle;

        if (this.index < 0) {
            this.index = 0;
        }
    }

    public void cycle(int dir) {
        
        if (this.cycle) {
            this.index = Math.floorMod(this.index + dir, this.values.size());
        } else {
            this.index += dir;
            if (this.index < 0) this.index = 0;
            else if (this.index + 1 > this.values.size()) this.index = this.values.size() - 1;
        }

        T value = this.values.get(this.index);
        this.currentValue = value;
        this.onChange.onChanged(value);
    }

    public boolean keyPressed(int code) {
        if (Keybinding.SELECT_LEFT.test(code)) {
            this.cycle(-1);
            Minicraft.getInstance().soundManager.play(SoundEvents.CONFIRM);
            return true;
        } else if (Keybinding.SELECT_RIGHT.test(code)) {
            this.cycle(1);
            Minicraft.getInstance().soundManager.play(SoundEvents.CONFIRM);
            return true;
        }

        return false;
    }

    public int getWidth() {
        return Font.getWidth(this.getText());
    }

    public String getText() {
        return Language.translate(this.label) + ": " + Language.translate(this.labelGetter.get(this.currentValue));
    }

    @FunctionalInterface
    public interface CycleEntryLabelGetter<T> {
        String get(T value);
    }

    @FunctionalInterface
    public interface CycleEntryOnChange<T> {
        void onChanged(T value);
    }
}
