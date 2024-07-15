package org.example.Logger;

import org.example.BeatSaberStats.BeatLeaderWorker;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Logger {
    public static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(BeatLeaderWorker.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("BeatLeaderWorker.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
