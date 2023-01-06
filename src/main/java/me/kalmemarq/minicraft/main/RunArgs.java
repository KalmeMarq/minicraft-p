package me.kalmemarq.minicraft.main;

import java.io.File;

public record RunArgs(boolean debug, boolean maximized, boolean fullscreen, File gameDir, int width, int height) {
}
