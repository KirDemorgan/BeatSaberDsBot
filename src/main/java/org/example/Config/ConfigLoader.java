package org.example.Config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties configProps = new Properties();

    public ConfigLoader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            configProps.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getToken() {
        return configProps.getProperty("token");
    }

    public String getGuildId() {
        return configProps.getProperty("guildId");
    }

    public String getChanel() {
        return configProps.getProperty("chanelId");
    }
}
