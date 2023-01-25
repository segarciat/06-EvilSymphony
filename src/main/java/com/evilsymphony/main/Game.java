package com.evilsymphony.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";

    public void run() {
        String splashText = loadText(SPLASH_FILE);
        String gameSummary = loadText(GAME_SUMMARY_FILE);

        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        String userInput = prompt("What would you like to do?\nNew Game", "(?i)(PLAY|QUIT|P|Q)");

        if ("QUIT".equals(userInput)) {
            System.out.println("Good bye!");
        } else if ("PLAY".equals(userInput))
            startGame();

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

    private String prompt(String message, String regex) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(message);
            String userInput = scanner.next().strip().toUpperCase();
            if (userInput.matches(regex))
                return userInput;
        }
    }

    private void startGame() {
        System.out.println("The game has started");
    }
}
