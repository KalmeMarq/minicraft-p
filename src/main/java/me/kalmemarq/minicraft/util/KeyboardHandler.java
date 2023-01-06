package me.kalmemarq.minicraft.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import me.kalmemarq.minicraft.Minicraft;

public class KeyboardHandler {
    private final Minicraft mc;
    private final KeyListener listener;
    private final int[] keys = new int[65556];

    public KeyboardHandler(Minicraft mc) {
        this.mc = mc;

        this.listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                KeyboardHandler.this.onCharTyped(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                KeyboardHandler.this.keys[e.getKeyCode()] = 1;
                KeyboardHandler.this.onKey(e.getKeyCode(), 1);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeyboardHandler.this.keys[e.getKeyCode()] = 0;
                KeyboardHandler.this.onKey(e.getKeyCode(), 0);
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
        }

        if (action == 1 && this.mc.menu != null) {
            this.mc.menu.keyPressed(code);
        }
    }
    
    private void onCharTyped(char chr) {
    }

    public KeyListener getListener() {
        return this.listener;
    }
}
