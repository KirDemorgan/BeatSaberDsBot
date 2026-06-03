package com.beatsaber.bot.platform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BeatLeaderClient {

    private static final String BASE_URL = "https://api.beatleader.xyz";

    private final HttpClient http;
    private final ObjectMapper mapper;

    public BeatLeaderClient() {
        this.http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.mapper = new ObjectMapper();
    }

    public double getUserPP(String discordId) throws Exception {
        var request = HttpRequest.newBuilder(URI.create(BASE_URL + "/player/discord/" + discordId))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();

        var response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("BeatLeader API error: HTTP " + response.statusCode());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("lastWeekPp").asDouble();
    }
}
