package me.kalmemarq.minicraft.util.math;

public class MathHelper {
    public static int clamp(int value, int min, int max) {
        if (value > max) return max;
        return Math.max(value, min);
    }
    
    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
}
