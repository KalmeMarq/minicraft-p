package me.kalmemarq.minicraft.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gui.PauseMenu;

public class Keyboard {
    private final Minicraft mc;
    private final KeyListener listener;
    private final int[] keys = new int[65556];

    public Keyboard(Minicraft mc) {
        this.mc = mc;

        this.listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                Keyboard.this.onCharTyped(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Keyboard.this.keys[e.getKeyCode()] = 1;
                Keyboard.this.onKey(e.getKeyCode(), 1);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Keyboard.this.keys[e.getKeyCode()] = 0;
                Keyboard.this.onKey(e.getKeyCode(), 0);
            }
        };
    }

    public boolean isKeyPressed(int code) {
        return keys[code] == 1;
    }

    private void onKey(int code, int action) {
        if (action == 1) {
            if (Keybinding.FULLSCREEN.test(code)) {
                mc.getWindow().toggleFullscreen();
            }

            if (code == Keys.KEY_F5) {
                this.mc.requestSyncReload();
            }

            if (code == Keys.KEY_ESCAPE && this.mc.menu == null) {
                this.mc.setMenu(new PauseMenu());
                return;
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
