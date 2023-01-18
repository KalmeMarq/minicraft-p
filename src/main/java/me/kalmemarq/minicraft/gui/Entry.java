package me.kalmemarq.minicraft.gui;

public abstract class Entry {
    public boolean enabled = true;

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    abstract public boolean keyPressed(int code);
    abstract public int getWidth();
    abstract public String getText();

    public boolean charTyped(char chr) {
        return false;
    }
}
