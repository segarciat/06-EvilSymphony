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

        String userInput = parser.promptAndCheckForQuit("What would you like to do?\nPlay\n", "(?i)(PLAY)");


        if (userInput.equals(TextParser.QUIT)) {
            System.out.println("Good bye!");
        } else if (userInput.equals("PLAY"))
            startGame();

    }

    private void startGame() {
        System.out.println("The game has started");
        String userInput = "";
        while (!userInput.equals(TextParser.QUIT)) {
            userInput = parser.promptAndCheckForQuit("What would you like to do?\n", "\\d");
            System.out.println("Input matched.");
        }
        System.out.println("Thanks for playing!");
    }
}
