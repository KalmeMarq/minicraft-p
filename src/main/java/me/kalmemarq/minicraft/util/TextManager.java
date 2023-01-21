package me.kalmemarq.minicraft.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import me.kalmemarq.minicraft.util.resource.Resource;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class TextManager extends SyncResourceReloader {
    private static final Logger LOGGER = Util.Logging.getLogger();

    private static final Random RANDOM = new Random();
    private static final Identifier SPLASH = new Identifier("texts/splashes.json");
    private static final Identifier TIPS = new Identifier("texts/loading_tips.json");
    private static final List<String> splashes = new ArrayList<>();
    private static final List<String> tips = new ArrayList<>();

    @Override
    protected void reload(ResourceManager manager) {
        splashes.clear();

        for (Resource res : manager.getResources(SPLASH)) {
            try (BufferedReader reader = res.getAsReader()) {
                JsonNode obj = Util.Json.parse(reader);

                if (Util.Json.getBoolean(obj, "replace", false)) {
                    splashes.clear();
                }

                if (Util.Json.hasArray(obj, "splashes")) {
                    for (JsonNode el : obj.get("splashes")) {
                        splashes.add(el.asText());
                    }
                }
            } catch(IOException e) {
                LOGGER.error("Failed to load splashes.json in resource pack: {}", res.getResourcePackName(), e);
            }
        }

        tips.clear();

        for (Resource res : manager.getResources(TIPS)) {
            try (BufferedReader reader = res.getAsReader()) {
                JsonNode obj = Util.Json.parse(reader);

                if (Util.Json.getBoolean(obj, "replace", false)) {
                    tips.clear();
                }

                if (Util.Json.hasArray(obj, "loading_tips")) {
                    for (JsonNode el : obj.get("loading_tips")) {
                        tips.add(el.asText());
                    }
                }
            } catch(IOException e) {
                LOGGER.error("Failed to load loading_tips.json in resource pack: {}", res.getResourcePackName(), e);
            }
        }
    }

    public String getSplash() {
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

        String str;

        do {
            str = splashes.get(RANDOM.nextInt(splashes.size()));
        } while (!str.equals("Secret Splash!"));

		return str;
    }

    public String getLoadingTip() {
        if (tips.size() == 0) {
            return "If you want some tips... well you won't have any";
        }

        return tips.get(RANDOM.nextInt(tips.size()));
    }
}
