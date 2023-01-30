package com.evilsymphony;

import java.util.*;

public class Game {
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";

    private static final String LOCATION_FILE = "src/main/resources/location.json";
    private static final String NPC_FILE = "src/main/resources/npc.json";
    private static final String COMMAND_FILE = "src/main/resources/commands.json";

    private static final String STARTING_LOCATION = "MUSIC HALL";

    // commands
    private static final String TALK = "TALK";
    private static final String HELP = "HELP";
    private static final String EXAMINE = "EXAMINE";
    private static final String GO = "GO";


    private final TextParser parser = new TextParser();

    /**
     * Starting point of the application.
     */
    public void run() {
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);

        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        String userInput = parser.promptAndCheckForQuit(
                "What would you like to do?\nPlay\n",
                "(?i)(PLAY)",
                Color.RED.setFontColor("\nInvalid Command. Please enter Play or Quit\n"))
                .toUpperCase();

        if (userInput.equals(TextParser.QUIT)) {
            System.out.println("Good bye!");
        } else if (userInput.equals("PLAY"))
            startGame();

    }

    /**
     * Initializes main game loop.
     */
    private void startGame() {

        Map<String, Location> locations = Location.loadLocations(LOCATION_FILE);
        Map<String, NPC> allNPCs = NPC.loadNpcs(NPC_FILE);
        List<PlayerCommand> commandList = PlayerCommand.loadCommands(COMMAND_FILE);

        Location currentLocation = locations.get(STARTING_LOCATION);

        String userInput = "";
        while (!userInput.equals(TextParser.QUIT)) {

            // Prompt user for a command
            userInput = parser.promptAndCheckForQuit(
                    currentLocation.getDescription(),
                    "(?i)(GO|TALK|EXAMINE) ([\\w\\s]+)|HELP",
                    Color.RED.setFontColor("Invalid Command. To view list of valid commands, type HELP"))
                    .toUpperCase();

            // Parse the command entered by the user.
            String[] commandParts = parser.parseCommand(userInput);

            String command = commandParts[0];
            String noun = commandParts[1];

            // Process the command entered by the user.
            if (HELP.equalsIgnoreCase(command)) {
                displayHelpMenu(commandList);
            } else if(GO.equalsIgnoreCase(command) && currentLocation.getDirections().contains(noun)) {
                currentLocation = locations.get(noun);
            } else if (TALK.equalsIgnoreCase(command) && currentLocation.getNPCs().contains(noun)) {
                NPC selectedNPC = allNPCs.get(noun);
                System.out.println(selectedNPC.getDialog().get("default"));
            }
            else if (EXAMINE.equalsIgnoreCase(command) && currentLocation.getItems().contains(noun)) {
                System.out.println("Trying to examine an item");
            } else if (commandList.stream().noneMatch(cmd -> cmd.getName().equalsIgnoreCase(command))){
                System.out.println(Color.RED.setFontColor(String.format("%s is invalid\n", noun)));
                parser.promptContinue();
            }
        }
        handleQuit();
    }

    private void displayHelpMenu(List<PlayerCommand> cList) {

        System.out.println("\n-----------HELP MENU-------------");
        for (PlayerCommand command : cList) {
            System.out.printf("%s \n\n",command.getDescription());
        }
        System.out.println();
    }

    /**
     * Performs any necessary cleanup and or closes files.
     */
    private void handleQuit() {
        System.out.println("Thanks for playing!");
    }
}