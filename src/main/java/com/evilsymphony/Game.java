package com.evilsymphony;

import java.util.*;

public class Game {
    private static final String GAME_SUMMARY_FILE = "game_summary.txt";
    private static final String SPLASH_FILE = "splash.txt";

    private static final String LOCATION_FILE = "location.json";
    private static final String NPC_FILE = "npc.json";
    private static final String STARTING_LOCATION = "MUSIC HALL";

    // commands



    private final TextParser parser = new TextParser();

    /**
     * Starting point of the application.
     */
    public void run() {
        clearScreen();              //Game start clear
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);

        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        String userInput = parser.promptAndCheckForQuit(
                        "What would you like to do?\nPlay\n",
                        "(?i)(PLAY)",
                        Color.RED.setFontColor("\nInvalid Command. Please enter Play or Quit\n"))
                .toUpperCase();

        if (userInput.equals(PlayerCommand.QUIT.toString())) {
            System.out.println("Good bye!");
        } else if (userInput.equals(PlayerCommand.PLAY.toString())) {
            clearScreen();
            startGame();
        }
    }

    /**
     * Initializes main game loop.
     */
    private void startGame() {

        Map<String, Location> locations = Location.loadLocations(LOCATION_FILE);
        Map<String, NPC> allNPCs = NPC.loadNPCs(NPC_FILE);

        Location currentLocation = locations.get(STARTING_LOCATION);


        while (true) {
            clearScreen();
            // Prompt user for a command

            String userInput = parser.promptAndCheckForQuit(
                            currentLocation.getDescription(),
                            "(?i)(GO|TALK|EXAMINE) ([\\w\\s]+)|HELP",
                            Color.RED.setFontColor("Invalid Command. To view list of valid commands, type HELP"))
                    .toUpperCase();

            // Parse the command entered by the user.
            String[] commandParts = parser.parseCommand(userInput);

            String command = commandParts[0];
            String noun = commandParts[1];
            if(PlayerCommand.QUIT.toString().equalsIgnoreCase(command)){
                break;
            }

            // Process the command entered by the user.
            if (PlayerCommand.HELP.toString().equalsIgnoreCase(command)) {
                System.out.println(PlayerCommand.getHelpMenu());
            } else if (PlayerCommand.GO.toString().equalsIgnoreCase(command) && currentLocation.containsLocation(noun)) {
                currentLocation = locations.get(noun);
            } else if (PlayerCommand.TALK.toString().equalsIgnoreCase(command) && currentLocation.containsNpc(noun)) {
                NPC selectedNPC = allNPCs.get(noun);
                System.out.println(selectedNPC.getDialogue());
            } else if (PlayerCommand.EXAMINE.toString().equalsIgnoreCase(command) && currentLocation.getItems().contains(noun)) {
                System.out.println("Trying to examine an item");
            } else {
                System.out.println(Color.RED.setFontColor(String.format("%s is invalid\n", noun)));
                parser.promptContinue();
            }
        }
        handleQuit();
    }

    /**
     * Performs any necessary cleanup and or closes files.
     */
    private void handleQuit() {
        clearScreen();
        // System.out.print(String.format("%c[%d;%df",escCode,row,column));
        System.out.println("Thanks for playing!");
    }

    /**
     * Performs clear console screen
     */
    private void clearScreen(){
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows"))
                //  cmd: Starts a new instance of the Windows command interpreter
                //  /c: Carries out the command specified by the string and terminates
                //  cls: clear screen
                // for more information, from a command prompt, type cmd /?
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
