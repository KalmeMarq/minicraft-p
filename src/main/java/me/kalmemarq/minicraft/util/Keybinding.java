package me.kalmemarq.minicraft.util;

public class Keybinding {
    public static final Keybinding MOVE_UP = new Keybinding(Keys.KEY_W, Keys.KEY_ARROW_UP);
    public static final Keybinding MOVE_DOWN = new Keybinding(Keys.KEY_S, Keys.KEY_ARROW_DOWN);
    public static final Keybinding SELECT_UP = new Keybinding(Keys.KEY_S, Keys.KEY_ARROW_DOWN);
    public static final Keybinding SELECT_DOWN = new Keybinding(Keys.KEY_S, Keys.KEY_ARROW_DOWN);
    public static final Keybinding SELECT_LEFT = new Keybinding(Keys.KEY_A, Keys.KEY_ARROW_LEFT);
    public static final Keybinding SELECT_RIGHT = new Keybinding(Keys.KEY_D, Keys.KEY_ARROW_RIGHT);
    public static final Keybinding SELECT = new Keybinding(Keys.KEY_ENTER);
    public static final Keybinding EXIT = new Keybinding(Keys.KEY_ESCAPE);
    public static final Keybinding ATTACK = new Keybinding(Keys.KEY_C, Keys.KEY_SPACE, Keys.KEY_ENTER);
    public static final Keybinding FULLSCREEN = new Keybinding(Keys.KEY_F11);

    public static final Keybinding[] KEYBINDINGS = { SELECT_UP, SELECT_DOWN, MOVE_DOWN, MOVE_UP, EXIT, ATTACK, FULLSCREEN, SELECT };

    private final int[] codes;
    
    public Keybinding(int ...codes) {
        this.codes = codes;
    }

    public boolean test(int code) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] == code) return true;
        }

        return false;
    }
}
