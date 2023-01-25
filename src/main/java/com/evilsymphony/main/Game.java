package com.evilsymphony.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";

    private String splashText;

    public void run() {
        loadSplashText();

        System.out.println(splashText);
    }

    private void loadSplashText() {
        try {
            splashText = Files.readString(Path.of(SPLASH_FILE));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
