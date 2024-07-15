package org.example;

import org.example.BeatLeaderWorker.BeatLeaderWorker;
import org.example.DiscordBotWorker.DiscordBotWorker;
import org.example.Logger.Bot;

import java.util.Arrays;

import static org.example.Logger.Logger.LOGGER;

public class Main {
    public static void main(String[] args) {

        BeatLeaderWorker beatLeaderWorker = new BeatLeaderWorker();
        DiscordBotWorker botWorker = new DiscordBotWorker();

        try {
            botWorker.startBot();
        } catch (Exception e) {
            String exception = Arrays.toString(e.getStackTrace());
            LOGGER.warning("Failed to fetch data from BeatLeader API" + exception);
        }
    }
}