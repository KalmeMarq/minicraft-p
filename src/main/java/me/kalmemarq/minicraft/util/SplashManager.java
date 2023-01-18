package me.kalmemarq.minicraft.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.loader.SyncResourceReloader;

public class SplashManager {
    private static final Random RANDOM = new Random();
    private static final List<String> splashes = new ArrayList<>();

    private static final ResourceReloader reloader = new SyncResourceReloader() {
        @Override
        protected void reload() {
            splashes.clear();
    
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Language.class.getResourceAsStream("/splashes.json")))) {;
                JsonObject obj = JsonUtil.deserialize(reader);
                JsonArray arr = JsonUtil.getArray(obj, "splashes");
    
                for (JsonElement txt : arr) {
                    splashes.add(txt.getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static ResourceReloader getReloader() {
      return reloader;
    }

    public static String getSplash() {
        LocalDateTime time = LocalDateTime.now();

        if (time.getMonth() == Month.JANUARY && time.getDayOfMonth() == 1) {
            return "Happy New Year!";
        } else if (time.getMonth() == Month.DECEMBER) {
			if (time.getDayOfMonth() == 19) return "Happy Birthday Minicraft!";
			if (time.getDayOfMonth() == 25) return "Happy Chirstmas!";
        }

        if (splashes.size() == 0) {
            return "Sadge";
        }

		return splashes.get(RANDOM.nextInt(splashes.size()));
    }
}
