package me.kalmemarq.minicraft.util.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.kalmemarq.minicraft.util.JsonUtil;
import me.kalmemarq.minicraft.util.syncloader.ResourceLoader;

public class Language {
    public static String code = "pt_pt";
    public static LanguageInfo language = new LanguageInfo("pt_pt", "English", "US");
    public static final List<LanguageInfo> languages = new ArrayList<>();
    private static final Map<String, String> translations = Maps.newHashMap();

    public static ResourceLoader reloader = new ResourceLoader() {
        public void reload() {
            translations.clear();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Language.class.getResourceAsStream("/languages.json")))) {;
                JsonObject obj = JsonUtil.deserialize(reader);

                languages.clear();

                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    JsonObject obj1 = entry.getValue().getAsJsonObject();
                    languages.add(new LanguageInfo(entry.getKey(), JsonUtil.getString(obj1, "name"), JsonUtil.getString(obj1, "region")));
                }

                Set<String> codes = Sets.newHashSet("en_us", code);
            
                for (String cd : codes) {
                    try(BufferedReader reader2 = new BufferedReader(new InputStreamReader(Language.class.getResourceAsStream("/lang/" + cd + ".json")))) {
                        JsonObject obj2 = JsonUtil.deserialize(reader2);

                        for (Map.Entry<String, JsonElement> entry : obj2.entrySet()) {
                            translations.put(entry.getKey(), entry.getValue().getAsString());
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        };
    };

    public static String translate(String key) {
        return translations.getOrDefault(key, key);
    }
}
