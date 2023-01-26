package com.evilsymphony.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class TextParser {
    public static final String QUIT = "QUIT";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Loads text from a file and returns it.
     */
    public String loadText(String filename) {

        String text = "";
        try {
            text = Files.readString(Path.of(filename));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Displays a message, and waits for their response.
     *
     * Returns the user's input, in upper case, only if it matches the regex.
     */
    public String prompt(String message, String regex) {

        while (true) {
            System.out.print(message);
            String userInput = scanner.next().strip().toUpperCase();
            if (userInput.matches(regex))
                return userInput;
        }
    }

    /**
     * Same as prompt(), but also listens for QUIT.
     */
    public String promptAndCheckForQuit(String message, String regex) {
        message = String.format("%s%s\n>", message, QUIT);
        regex = String.format("%s|(%s)", regex, QUIT);
        return prompt(message, regex);
    }
}
