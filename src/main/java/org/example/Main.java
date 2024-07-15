package org.example;

import org.example.BeatSaberStats.BeatLeaderWorker;
import org.example.DiscordBotWorker.DiscordBotWorker;

import java.util.Arrays;

import static org.example.Logger.Logger.LOGGER;

public class Main {
    public static void main(String[] args) {

        DiscordBotWorker botWorker = new DiscordBotWorker();

        try {
            botWorker.startBot();
        } catch (Exception e) {
            String exception = Arrays.toString(e.getStackTrace());
            LOGGER.warning("Failed to fetch data from BeatLeader API" + exception);
        }
    }
}