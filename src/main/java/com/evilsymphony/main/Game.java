package com.evilsymphony.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";

    public void run() {
        String splashText = loadText(SPLASH_FILE);
        String gameSummary = loadText(GAME_SUMMARY_FILE);

        System.out.printf("%s\n\n", splashText);
        System.out.println(gameSummary);
    }

    private String loadText(String filename) {
        String text = "";
        try {
            text = Files.readString(Path.of(filename));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
