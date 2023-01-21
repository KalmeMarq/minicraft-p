package me.kalmemarq.minicraft.util.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.resource.Resource;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class Language {
    private static final Logger LOGGER = Util.Logging.getLogger();
    private static final Identifier LANGUAGE_METADATA = new Identifier("languages/languages.json");
    private static final LanguageInfo DEFAULT_LANGUAGE = new LanguageInfo("en_us", "en-US", "English", "US");

    public static String code = "en_us";
    public static LanguageInfo language = DEFAULT_LANGUAGE;
    public static final List<LanguageInfo> languages = new ArrayList<>();
    private static final Map<String, String> translations = Maps.newHashMap();

    public static ResourceReloader reloader = new SyncResourceReloader() {
        @Override
        public void reload(ResourceManager manager) {
            translations.clear();

            languages.clear();

            for (Resource res : manager.getResources(LANGUAGE_METADATA)) {
                try (BufferedReader reader = res.getAsReader()) {
                    ObjectNode obj = (ObjectNode)Util.Json.parse(reader);

                    Iterator<Entry<String, JsonNode>> iter = obj.fields();

                    while (iter.hasNext()) {
                       Entry<String, JsonNode> entry = iter.next();
                       String code = entry.getKey();
                       JsonNode value = entry.getValue();

                       JsonNode name = value.get("name");
                       JsonNode region = value.get("region");
                       JsonNode locale = value.get("locale");

                       if (name == null || region == null) continue;
                       
                       String lc = locale == null ? code.replace("_", "-") : locale.asText();
                       languages.add(new LanguageInfo(entry.getKey(), lc, name.asText(), region.asText()));
                    }
                } catch(IOException e) {
                    LOGGER.error("Failed to load languages.json in resource pack: {}", res.getResourcePackName(), e);
                }
            }

            Set<String> codes = Sets.newHashSet("en_us", code);

            for (String cd : codes) {
                for (Resource res : manager.getResources(new Identifier("languages/" + cd + ".json"))) {
                    try (BufferedReader reader = res.getAsReader()) {
                        JsonNode obj = Util.Json.parse(reader);

                        for (Iterator<Entry<String, JsonNode>> iter = obj.fields(); iter.hasNext();) {
                            Entry<String, JsonNode> entry = iter.next();

                            translations.put(entry.getKey(), entry.getValue().asText());
                        }    
                    } catch (IOException e) {
                        LOGGER.error("Failed to load language {} in resource pack: {}", cd, res.getResourcePackName(), e);
                    }
                }
            }
        }
    };

    public static String translate(String key) {
        return translations.getOrDefault(key, key);
    }
}
