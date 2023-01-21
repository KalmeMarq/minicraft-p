package me.kalmemarq.minicraft.util.language;

import java.util.Locale;

public class LanguageInfo {
    private final Locale locale;
    private final String code;
    private final String name;
    private final String region;

    public LanguageInfo(String code, String locale, String name, String region) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.locale = Locale.forLanguageTag(locale);
    }

    public String getCode() {
      return this.code;
    }

    public Locale getLocale() {
      return this.locale;
    }

    public String getName() {
      return this.name;
    }

    public String getRegion() {
      return this.region;
    }
}
