package org.example.BeatLeaderWorker;
import java.math.BigDecimal;
import java.net.*;
import java.io.*;
import org.json.JSONObject;

import static org.example.Logger.Logger.LOGGER;

public class BeatLeaderWorker {
    public static void getUserPP(String userDiscordID) throws Exception {
        System.out.println("BeatLeaderWorker started");
        System.out.println("Fetching data from BeatLeader API");

        LOGGER.info("BeatLeaderWorker started\n" +
                "Fetching data from BeatLeader API");


        URL beatLeader = new URL("https://api.beatleader.xyz/player/discord/" + userDiscordID);
        URLConnection yc = beatLeader.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));

        JSONObject obj = new JSONObject(in.readLine());

        BigDecimal playerScore = obj.getBigDecimal("lastWeekPp");
        String playerName = obj.getString("alias");

        System.out.println("\n" + playerName + "'s last week pp: " + playerScore);

        in.close();
    }
}
