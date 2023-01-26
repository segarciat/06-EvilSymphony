package com.evilsymphony.main;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";

    private final TextParser parser = new TextParser();

    public void run() {
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);

        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        String userInput = parser.prompt("What would you like to do?\nPlay\nQuit\n>", "(?i)(PLAY|QUIT)");

        if ("QUIT".equals(userInput)) {
            System.out.println("Good bye!");
        } else if ("PLAY".equals(userInput))
            startGame();

    }

    private void startGame() {
        System.out.println("The game has started");
        String userInput = "";
        while (!userInput.equals("QUIT")) {
            userInput = parser.prompt("What would you like to do?\nQuit\n>", "(?i)(QUIT)");
        }
        System.out.println("Thanks for playing!");
    }
}
