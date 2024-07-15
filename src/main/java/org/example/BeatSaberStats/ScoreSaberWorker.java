package org.example.BeatSaberStats;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

import static org.example.Logger.Logger.LOGGER;

public class ScoreSaberWorker {
    public static String getUserPPScoreSaber(String userID) {
        try {
            System.out.println("ScoreSaberWorker started - Fetching data for userID: " + userID);
            LOGGER.info("Fetching data from ScoreSaber API for userID: " + userID);

            URL scoreSaberURL = new URL("https://scoresaber.com/api/player/" + userID + "/full");
            URLConnection yc = scoreSaberURL.openConnection();
            yc.setConnectTimeout(5000);
            yc.setReadTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
            String response = responseBuilder.toString();

            LOGGER.info("Raw response: " + response);

            JSONObject obj = new JSONObject(response);
            BigDecimal playerScore = obj.getBigDecimal("pp");
            System.out.println(userID + "'s pp: " + playerScore);

            in.close();
            return playerScore.toString();
        } catch (Exception e) {
            LOGGER.severe("Failed to fetch or parse ScoreSaber data for userID: " + userID + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}