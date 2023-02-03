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
    private static final String ENTER_COMMAND_PROMPT = "Please enter a command or type HELP >";
    private static final String INVALID_COMMAND_TYPE_HELP = "Invalid Command. To view list of valid commands, type HELP";

    private static final String STARTING_LOCATION = "MAIN HALL";

    private final TextParser parser = new TextParser();
    private String gameMap;

    private Map<String, Location> locations;
    private Map<String, NPC> allNPCs;
    private Map<String, Item> items;

    private Player player;

    CommandHandler cmdHandler = new CommandHandler(this);

    /**
     * Starting point of the application.
     */
    public void run() {
        clearScreen();              //Game start clear

        // Load game text and splash.
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);

        // Display it.
        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        // Prompt player to play or quit.
        String userInput = parser.prompt(
                        PLAY_OR_QUIT_PROMPT_MESSAGE,
                        parser.getCommandsRegex(PlayerCommand.PLAY, PlayerCommand.QUIT),
                        Color.RED.setFontColor(INVALID_COMMAND_ENTER_PLAY_OR_QUIT)
                ).toUpperCase();

        if (userInput.equals(PlayerCommand.QUIT.toString())) {
            cmdHandler.handleQuit();
        } else if (userInput.equals(PlayerCommand.PLAY.toString())) {
            clearScreen();
            startGame();
        }
    }

    /**
     * Loads game resources and runs the game loop.
     */
    private void startGame() {
        // Load resources.
        locations = JSONLoader.loadFromJsonAsMap(LOCATION_FILE, Location.class, o -> o.getName().toUpperCase());
        allNPCs = JSONLoader.loadFromJsonAsMap(NPC_FILE, NPC.class, o -> o.getName().toUpperCase());
        items = JSONLoader.loadFromJsonAsMap(ITEM_FILE, Item.class, o -> o.getName().toUpperCase());
        gameMap = parser.loadText(MAP_FILE);

        // Set up player.
        player = new Player();
        player.setCurrentLocation(locations.get(STARTING_LOCATION));

        while (true) {

            displayPlayerInfo();

            // Prompt user for a command
            String userInput = parser.prompt(
                            ENTER_COMMAND_PROMPT,
                            parser.getCommandsRegex(),
                            Color.RED.setFontColor(INVALID_COMMAND_TYPE_HELP))
                    .toUpperCase();

            // Parse the command entered by the user.
            List<String> commandParts = parser.parseCommand(userInput);

            String command = commandParts.get(0);
            String noun = commandParts.get(1);

            clearScreen();

            // Process the command entered by the user.
            cmdHandler.handle(command, noun);
            if(PlayerCommand.QUIT.isAliasOf(command)) {
                clearScreen();
                break;
            }
        }
    }

    /**
     * Displays information pertinent to the player's state.
     */
    private void displayPlayerInfo() {
        Set<Item> inventory = player.getInventory();

        // Inventory
        StringBuilder sb = new StringBuilder("------------------- INVENTORY ---------------------\n");
        for (Item item: inventory)
            sb.append(String.format("\t%s%s", item.getName(), System.lineSeparator()));
        System.out.println(Color.YELLOW.setFontColor(sb.toString()));

        // Location
        Location currentLocation = player.getCurrentLocation();
        System.out.printf("You are in: %s%s", currentLocation.getName(), System.lineSeparator());
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

    // Accessor methods.
    public Map<String, Location> getLocations() {
        return locations;
    }

    public Map<String, NPC> getAllNPCs() {
        return allNPCs;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public Player getPlayer() {
        return player;
    }

    public String getGameMap() {
        return gameMap;
    }

    public TextParser getParser() {
        return parser;
    }
}
