package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private static final String GAME_SUMMARY_FILE = "game_summary.txt";
    private static final String SPLASH_FILE = "splash.txt";
    private static final String MAP_FILE = "map.txt";

    private static final String LOCATION_FILE = "location.json";
    private static final String NPC_FILE = "npc.json";
    private static final String ITEM_FILE = "items.json";

    private static final String WHAT_WOULD_YOU_LIKE_TO_DO = "What would you like to do?\n";
    private static final String INVALID_COMMAND_ENTER_PLAY_OR_QUIT = "\nInvalid Command. Please enter Play or Quit\n";
    private static final String ENTER_COMMAND_PROMPT = "Please enter a command or type HELP >";
    private static final String INVALID_COMMAND_TYPE_HELP = "Invalid Command. To view list of valid commands, type HELP";

    private static final String STARTING_LOCATION = "MAIN HALL";
    public static final String SAVED_LOCATIONS_JSON = "savedLocations.json";
    public static final String SAVED_NPCS_JSON = "savedNPCs.json";
    public static final String SAVED_PLAYER_JSON = "savedPlayer.json";

    private final TextParser parser = new TextParser();
    private String gameMap;

    private Map<String, Location> locations;
    private Map<String, NPC> allNPCs;
    private Map<String, Item> items;

    private Player player;
    private BackgroundMusic music = new BackgroundMusic();

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

        // Display correct options, based on whether save file exists.
        PlayerCommand[] options;
        if (JSONLoader.exists(SAVED_PLAYER_JSON))
            options = new PlayerCommand[]{PlayerCommand.NEW_GAME, PlayerCommand.LOAD_GAME, PlayerCommand.QUIT};
        else
            options = new PlayerCommand[]{PlayerCommand.NEW_GAME, PlayerCommand.QUIT};

        String optionsText = Arrays.stream(options)
                .map(cmd -> cmd.toString().replaceAll("_", " "))
                .collect(Collectors.joining("\t")) + System.lineSeparator();

        // Prompt player to play or quit.
        String userInput = parser.prompt(
                        WHAT_WOULD_YOU_LIKE_TO_DO + optionsText,
                        parser.getCommandsRegex(options),
                        Color.RED.setFontColor(INVALID_COMMAND_ENTER_PLAY_OR_QUIT)
                ).toUpperCase();

        if (PlayerCommand.QUIT.isAliasOf(userInput)) {
            handleQuit();
        } else if (PlayerCommand.NEW_GAME.isAliasOf(userInput)) {
            startGame(true);
        } else if (PlayerCommand.LOAD_GAME.isAliasOf(userInput)) {
            startGame(false);
        } else {
            System.out.println("No options matched.");
        }
    }

    /**
     * Loads game resources and runs the game loop.
     */
    private void startGame(boolean newGame) {
        clearScreen();
        // Load resources.
        String locationFile, npcFile;
        if (newGame) {
            locationFile = LOCATION_FILE;
            npcFile = NPC_FILE;
        } else {
            locationFile = SAVED_LOCATIONS_JSON;
            npcFile = SAVED_NPCS_JSON;
        }

        locations = JSONLoader.loadFromJsonAsMap(locationFile, Location.class, o -> o.getName().toUpperCase());
        allNPCs = JSONLoader.loadFromJsonAsMap(npcFile, NPC.class, o -> o.getName().toUpperCase());
        items = JSONLoader.loadFromJsonAsMap(ITEM_FILE, Item.class, o -> o.getName().toUpperCase());
        gameMap = parser.loadText(MAP_FILE);

        // Set up player.
        if (newGame) {
            player = new Player();
            player.setCurrentLocation(locations.get(STARTING_LOCATION));
        } else {
            player = JSONLoader.loadPlayerFromJson(SAVED_PLAYER_JSON);
        }

        music.play(player.getCurrentLocation().getMusic());
        System.out.println(player.getCurrentLocation().getDescription());

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
                break;
            }
        }
    }

    /**
     * Performs any necessary cleanup, including saving data.
     */
    public void handleQuit() {
        clearScreen();
        System.out.println("Thanks for playing!");
    }


    /**
     * Saves the Player's data,including location and inventory.
     * Saves all changes in the locations, as well as NPCs.
     */
    public void save() {
        Collection<Location> locationsColl = locations.values();
        Collection<NPC> npcColl = allNPCs.values();

        Type npcCollType = TypeToken.getParameterized(Collection.class, NPC.class).getType();
        Type locationsCollType = TypeToken.getParameterized(Collection.class, Location.class).getType();
        Type playerType = TypeToken.get(Player.class).getType();

        saveGameData(locationsColl, locationsCollType, SAVED_LOCATIONS_JSON);
        saveGameData(npcColl, npcCollType, SAVED_NPCS_JSON);
        saveGameData(player, playerType, SAVED_PLAYER_JSON);
        System.out.println(Color.GREEN.setFontColor("Saved game successfully!"));
    }

    /**
     * Saves an object as JSON to a file.
     *
     * @param data Data to save to as JSON
     * @param type Type of the data being saved.
     * @param filename The name of the JSON file being saved to.
     */
    private void saveGameData(Object data, Type type, String filename) {
        String base = Objects.requireNonNull(getClass().getClassLoader().getResource("")).getPath();
        File file = new File(base, filename);
        try (JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)))) {
            Gson gson = new Gson();
            gson.toJson(data, type, writer);
        } catch(IOException e) {
            System.out.println("Unable to save");
            throw new RuntimeException(e);
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

    public BackgroundMusic getMusic() {
        return music;
    }
}
