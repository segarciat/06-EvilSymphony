package com.evilsymphony;

import java.util.*;

public class Game {

    private static final String GAME_SUMMARY_FILE = "game_summary.txt";
    private static final String SPLASH_FILE = "splash.txt";
    private static final String MAP_FILE = "map.txt";

    private static final String LOCATION_FILE = "location.json";
    private static final String NPC_FILE = "npc.json";
    private static final String ITEM_FILE = "items.json";

    private static final String PLAY_OR_QUIT_PROMPT_MESSAGE = "What would you like to do?\nPlay\tQuit\n";
    private static final String INVALID_COMMAND_ENTER_PLAY_OR_QUIT = "\nInvalid Command. Please enter Play or Quit\n";
    private static final String ENTER_COMMAND_PROMPT = "Please enter a command >";
    private static final String INVALID_COMMAND_TYPE_HELP = "Invalid Command. To view list of valid commands, type HELP";

    private static final String STARTING_LOCATION = "MAIN HALL";

    private final TextParser parser = new TextParser();
    private final String MAP_LAYOUT = parser.loadText(MAP_FILE);

    /**
     * Starting point of the application.
     */
    public void run() {
        clearScreen();              //Game start clear
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);
        System.out.printf(PlayerCommand.getCommandsRegex());
        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        String userInput = parser.prompt(
                        PLAY_OR_QUIT_PROMPT_MESSAGE,
                        PlayerCommand.getCommandsRegex(PlayerCommand.PLAY, PlayerCommand.QUIT),
                        Color.RED.setFontColor(INVALID_COMMAND_ENTER_PLAY_OR_QUIT)
                ).toUpperCase();

        if (userInput.equals(PlayerCommand.QUIT.toString())) {
            handleQuit();
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
        Map<String, Item> items = Item.loadItems(ITEM_FILE);
        Inventory inventory = new Inventory();

        Location currentLocation = locations.get(STARTING_LOCATION);

        while (true) {

            // Prompt user for a command
            displayPlayerInfo(inventory, currentLocation);
            String userInput = parser.prompt(
                            ENTER_COMMAND_PROMPT,
                            PlayerCommand.getCommandsRegex(),
                            Color.RED.setFontColor(INVALID_COMMAND_TYPE_HELP))
                    .toUpperCase();

            // Parse the command entered by the user.
            String[] commandParts = parser.parseCommand(userInput);

            String command = commandParts[0];
            String noun = commandParts[1];

            clearScreen();
            // Process the command entered by the user.
            if(PlayerCommand.QUIT.isAliasOf(command)){
                break;
            }
            else if (PlayerCommand.HELP.isAliasOf(command)) {
                System.out.println(PlayerCommand.getHelpMenu());
            } else if (PlayerCommand.DESCRIBE.isAliasOf(command)) {
                System.out.println(currentLocation.getDescription());
            }
            else if (PlayerCommand.MAP.isAliasOf(command)){
                displayMap(currentLocation);
            } else if (PlayerCommand.GO.isAliasOf(command) && currentLocation.containsLocation(noun)) {
                currentLocation = locations.get(noun);
                System.out.println(currentLocation.getDescription());
            } else if (PlayerCommand.TALK.isAliasOf(command) && currentLocation.containsNpc(noun)) {
                NPC selectedNPC = allNPCs.get(noun);
                System.out.println(selectedNPC.getDialogue());
            } else if (PlayerCommand.LOOK.isAliasOf(command) && currentLocation.containsItem(noun)) {
                Item item = items.get(noun);
                System.out.println(item.getDescription());
            } else if (PlayerCommand.GET.isAliasOf(command) && currentLocation.containsItem(noun)){
                Item item = items.get(noun);
                inventory.addItem(item);
                currentLocation.removeItem(noun);
            }
            else {
                System.out.println(Arrays.toString(commandParts));
                System.out.println("WE are here!");
                System.out.println(Color.RED.setFontColor(INVALID_COMMAND_TYPE_HELP));
            }

        }
        handleQuit();
    }

    private void displayPlayerInfo(Inventory inventory, Location currentLocation) {
        inventory.displayContents();
        System.out.printf("You are in: %s%s", currentLocation.getName(), System.lineSeparator());
    }

    private void displayMap(Location currentLocation) {
        String s = String.format("You are in: %s%s",currentLocation.getName(), System.lineSeparator());
        System.out.println(MAP_LAYOUT);
        System.out.println(Color.GREEN.setFontColor(s));
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
