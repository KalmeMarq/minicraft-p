package me.kalmemarq.minicraft.client.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import me.kalmemarq.minicraft.client.Minicraft;
import me.kalmemarq.minicraft.client.gui.PauseMenu;
import me.kalmemarq.minicraft.util.Keys;

public class Keyboard {
    private final Minicraft mc;
    private final KeyListener listener;
    protected final static int[] keys = new int[65556];

    public Keyboard(Minicraft mc) {
        this.mc = mc;

        this.listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                Keyboard.this.onCharTyped(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Keyboard.keys[e.getKeyCode()] = 1;
                Keyboard.this.onKey(e.getKeyCode(), 1);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Keyboard.keys[e.getKeyCode()] = 0;
                Keyboard.this.onKey(e.getKeyCode(), 0);
            }
        };
    }

    public static boolean isKeyPressed(int code) {
        return keys[code] == 1;
    }

    private void onKey(int code, int action) {
        if (action == 1) {
            if (Keybinding.FULLSCREEN.test(code)) {
                mc.getWindow().toggleFullscreen();
                return;
            }

            if (code == Keys.KEY_F5) {
                this.mc.reloadResources(isKeyPressed(Keys.KEY_SHIFT));
                return;
            }

            if (code == Keys.KEY_ESCAPE && this.mc.menu == null) {
                this.mc.setMenu(new PauseMenu());
                return;
            }

            if (code == Keys.KEY_F4) {
                this.mc.options.showDebugFPS.opposite();
            }
        }

        if (action == 1 && this.mc.menu != null) {
            this.mc.menu.keyPressed(code);
        }
    }
    
    private void onCharTyped(char chr) {
        if (this.mc.menu != null) {
            this.mc.menu.charTyped(chr);
        }
    }

    public KeyListener getListener() {
        return this.listener;
    }
}
