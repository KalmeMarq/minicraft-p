package me.kalmemarq.minicraft.util.language;

import java.util.Locale;

public class LanguageInfo {
    private final Locale locale;
    private final String code;
    private final String name;
    private final String region;

    public LanguageInfo(String code, String name, String region) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.locale = Locale.forLanguageTag(code.replace("_", "-"));
    }

    public String getCode() {
      return code;
    }

    public Locale getLocale() {
      return locale;
    }

    public String getName() {
      return name;
    }

    public String getRegion() {
      return region;
    }
}
