package me.kalmemarq.minicraft.client.gui;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.client.gfx.Font;
import me.kalmemarq.minicraft.util.Keyboard;
import me.kalmemarq.minicraft.util.Keys;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.math.MathHelper;
import me.kalmemarq.minicraft.util.sound.SoundEvents;

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
    public boolean keyPressed(int code) {
        if (Keyboard.isKeyPressed(Keys.KEY_CONTROL) && code == Keys.KEY_BACKSPACE) {
            int s = Keyboard.isKeyPressed(Keys.KEY_SHIFT) ? 0 : this.value.lastIndexOf(' ');
            if (s < 0) s = 0;
            
            this.value = this.value.substring(0, s);
            
            if (this.listener != null) {
                this.listener.onChange(this.value);
            }

            Minicraft.getInstance().soundManager.play(SoundEvents.SELECT);

            return true;
        } else if (code == Keys.KEY_BACKSPACE) {
            int s = 0;
            int e = this.value.length() - 1;
            if (e < 0) e = 0;
            this.value = this.value.substring(s, e);

            if (this.listener != null) {
                this.listener.onChange(this.value);
            }

            Minicraft.getInstance().soundManager.play(SoundEvents.SELECT);

            return true;
        }

        return false;
    }

    public void setMaxLength(int maxLength) {
      this.maxLength = MathHelper.clamp(maxLength, 0, Integer.MAX_VALUE);
    }

    @Override
    public int getWidth() {
        return Font.getWidth(getText());
    }

    @Override
    public boolean charTyped(char chr) {
        if (chr >= '0' && chr <= '9' || chr >= 'A' && chr <= 'Z'  || chr >= 'a' && chr <= 'z' || chr == ' ') {
            if (this.value.length() + 1 <= this.maxLength) {
                this.value += chr;

                if (this.listener != null) {
                    this.listener.onChange(this.value);
                }

                Minicraft.getInstance().soundManager.play(SoundEvents.SELECT);

                return true;
            }
        }

        return false;
    }

    @Override
    public String getText() {
        return Language.translate(this.label) + ": " + this.value;
    }

    @FunctionalInterface
    public interface ValueListener {
        void onChange(String text);
    }
}
