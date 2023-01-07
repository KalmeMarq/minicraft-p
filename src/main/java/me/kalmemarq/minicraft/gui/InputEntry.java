package me.kalmemarq.minicraft.gui;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.MathHelper;

public class InputEntry extends Entry {
    private final String label;
    private String value;
    private int maxLength = 32;
    @Nullable
    private ValueListener listener;

    public InputEntry(String label) {
        this.label = label;
        this.value = "";
    }

    public void setListener(ValueListener listener) {
      this.listener = listener;
    }

    @Override
    public void keyPressed(int code) {
        if (code == Keys.KEY_BACKSPACE) {
            int s = 0;
            int e = this.value.length() - 1;
            if (e < 0) e = 0;
            this.value = this.value.substring(s, e);

            if (this.listener != null) {
                this.listener.onChange(this.value);
            }
        }
    }

    public void setMaxLength(int maxLength) {
      this.maxLength = MathHelper.clamp(maxLength, 0, Integer.MAX_VALUE);
    }

    @Override
    public int getWidth() {
        return Font.getWidth(getText());
    }

    @Override
    public void charTyped(char chr) {
        if (chr >= '0' && chr <= '9' || chr >= 'A' && chr <= 'Z'  || chr >= 'a' && chr <= 'z' || chr == ' ') {
            if (this.value.length() + 1 <= this.maxLength) {
                this.value += chr;

                if (this.listener != null) {
                    this.listener.onChange(this.value);
                }
            }
        }
    }

    @Override
    public String getText() {
        return this.label + ": " + this.value;
    }

    @FunctionalInterface
    public interface ValueListener {
        void onChange(String text);
    }
}
