package com.evilsymphony.util;

import com.evilsymphony.PlayerCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextParser {

    private final Scanner scanner = new Scanner(System.in);
    public final String allCommandsRegex = getCommandsRegex(PlayerCommand.values());

    /**
     * Loads text from a file and returns it.
     */
    public String loadText(String filename) {

        // Use try-with-resources block to automatically close resources
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename))))) {

            // Get the line separator to account for os defaults
            String separator = System.lineSeparator();

            // Generate a stream of lines from the file
            return Stream.generate(()->{

                        // Read the next line from the file
                        try {
                            return reader.readLine();
                        } catch (IOException e) {
                            // Throw a runtime exception if there's an IOException while reading the file
                            throw new RuntimeException(e);
                        }
                    })
                    // Continue reading the file while there's still more lines
                    .takeWhile(Objects::nonNull)
                    // Join the lines into a single string separated by the line separator
                    .collect(Collectors.joining(separator));
        } catch (IOException e) {
            // Throw a runtime exception if there's an IOException while opening the file
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays a message, and waits for their response.
     *
     * Returns the user's input, in upper case, only if it matches the regex.
     */
    public String prompt(String message, String regex, String helpText) {

        while (true) {
            System.out.print(message);
            String userInput = scanner.nextLine().strip();
            if (userInput.matches(regex))
                return userInput;
            else {
                System.out.println(helpText);

            }
        }
    }

    /**
     * Converts the user input into a two-part command array.
     */
    public List<String> parseCommand(String userInput) {
        Pattern pattern = Pattern.compile(allCommandsRegex);
        Matcher matcher = pattern.matcher(userInput);
        if (!matcher.matches())
            return null;

        return List.of(matcher.group(1), matcher.group(2));
    }

    public List<String> parseNPCTradeNouns(String noun, List<String> npcNames) {
        String npcRegex = String.format("(?i)(%s)\\s+(.+)", String.join("|", npcNames));
        Pattern pattern = Pattern.compile(npcRegex);
        Matcher matcher = pattern.matcher(noun);
        if (!matcher.matches())
            return null;

        return List.of(matcher.group(1), matcher.group(2));
    }

    public String getCommandsRegex(PlayerCommand... commands) {
        return String.format("(?i)(%s)\\s*(.*)",
                Arrays.stream(commands)
                        .flatMap(cmd -> cmd.getAliases().stream())
                        .collect(Collectors.joining("|")));
    }

    public String getCommandsRegex() {
        return allCommandsRegex;
    }
}
