package com.beatsaber.bot;

import com.beatsaber.bot.config.BotConfig;
import com.beatsaber.bot.discord.DiscordClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeatSaberBot {

    private static final Logger log = LoggerFactory.getLogger(BeatSaberBot.class);

    public static void main(String[] args) {
        try {
            BotConfig config = BotConfig.load();
            new DiscordClient(config).start();
        } catch (Exception e) {
            log.error("Failed to start bot", e);
            System.exit(1);
        }
    }
}
