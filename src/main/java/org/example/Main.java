package org.example;

import org.example.BeatLeaderWorker.BeatLeaderWorker;

public class Main {
    public static void main(String[] args) {
        BeatLeaderWorker beatLeaderWorker = new BeatLeaderWorker();

        try {
            BeatLeaderWorker.getUserPP("373095505711857665");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}