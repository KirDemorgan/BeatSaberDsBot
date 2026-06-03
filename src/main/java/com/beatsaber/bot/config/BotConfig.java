package com.beatsaber.bot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {

    private final String token;
    private final long guildId;

    private BotConfig(String token, long guildId) {
        this.token = token;
        this.guildId = guildId;
    }

    static BotConfig of(String token, String guildId) {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("DISCORD_TOKEN is not configured");
        }
        if (guildId == null || guildId.isBlank()) {
            throw new IllegalStateException("GUILD_ID is not configured");
        }
        return new BotConfig(token.trim(), Long.parseLong(guildId.trim()));
    }

    public static BotConfig load() throws IOException {
        String token = System.getenv("DISCORD_TOKEN");
        String guildIdStr = System.getenv("GUILD_ID");

        if (token == null || guildIdStr == null) {
            Properties props = loadProperties();
            if (token == null) token = props.getProperty("token");
            if (guildIdStr == null) guildIdStr = props.getProperty("guildId");
        }

        return of(token, guildIdStr);
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream is = BotConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) props.load(is);
        }
        return props;
    }

    public String getToken() {
        return token;
    }

    public long getGuildId() {
        return guildId;
    }
}
