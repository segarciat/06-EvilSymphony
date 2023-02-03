package com.evilsymphony;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Map<String, Location> locations;
    private Map<String, NPC> allNPCs;
    private Map<String, Item> items;

    private Player player;

    /**
     * Starting point of the application.
     */
    public void run() {
        clearScreen();              //Game start clear
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);

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
        player = new Player();

        locations = JSONLoader.loadFromJsonAsMap(LOCATION_FILE, Location.class, o -> o.getName().toUpperCase());
        allNPCs = JSONLoader.loadFromJsonAsMap(NPC_FILE, NPC.class, o -> o.getName().toUpperCase());
        items = JSONLoader.loadFromJsonAsMap(ITEM_FILE, Item.class, o -> o.getName().toUpperCase());

        player.setCurrentLocation(locations.get(STARTING_LOCATION));

        System.out.println(player.getCurrentLocation().getDescription());

        while (true) {

            // Prompt user for a command
            displayPlayerInfo();
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
            } else if (PlayerCommand.HELP.isAliasOf(command)) {
                handleHelpCommand();
            } else if (PlayerCommand.DESCRIBE.isAliasOf(command)) {
                handleDescribeCommand();
            } else if (PlayerCommand.MAP.isAliasOf(command)){
                handleMapCommand();
            } else if (PlayerCommand.GO.isAliasOf(command)) {
                handleGoCommand(noun);
            } else if (PlayerCommand.TALK.isAliasOf(command)) {
                handleTalkCommand(noun);
            } else if (PlayerCommand.DEALS.isAliasOf(command)) {
                handleDealsCommand(noun);
            } else if (PlayerCommand.TRADE.isAliasOf(command)) {
                handleTrade(noun);
            } else if (PlayerCommand.LOOK.isAliasOf(command)) {
                handleLookCommand(noun);
            } else if (PlayerCommand.GET.isAliasOf(command)){
                handleGetCommand(noun);
            } else {
                handleUnmatchedCommand(command);
            }

        }
        handleQuit();
    }

    private void handleHelpCommand() {
        PlayerCommand.displayHelpMenu();
    }

    private void handleUnmatchedCommand(String command) {
        String feedback = String.format("The command %s is not yet supported.", command);
        System.out.println(Color.RED.setFontColor(feedback));
    }

    private void handleGetCommand(String noun) {
        Location currentLocation = player.getCurrentLocation();
        if (!currentLocation.containsItem(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No item named %s in %s",
                            PlayerCommand.GET,noun, currentLocation.getName()))
            );
        } else {
            Item item = items.get(noun);
            player.addItemToInventory(item);
            currentLocation.removeItem(item.getName());
        }
    }

    private void handleLookCommand(String noun) {
        Location currentLocation = player.getCurrentLocation();
        if (!currentLocation.containsItem(noun) && !player.has(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No item named %s in %s or in your inventory",
                            PlayerCommand.LOOK,noun, currentLocation.getName()))
            );
        } else {
            Item item = items.get(noun);
            System.out.println(item.getDescription());
        }
    }

    private void handleDealsCommand(String noun) {
        Location currentLocation = player.getCurrentLocation();
        if (!currentLocation.containsNpc(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No NPC named %s in %s",
                            PlayerCommand.DEALS,noun, currentLocation.getName()))
            );
        } else {
            NPC npc = allNPCs.get(noun);
            System.out.println(npc.getTradeDeals());
        }
    }

    private void handleTalkCommand(String noun) {
        Location currentLocation = player.getCurrentLocation();
        if (!currentLocation.containsNpc(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No NPC named %s in %s",
                            PlayerCommand.TALK,noun, currentLocation.getName()))
            );
        } else {
            NPC npc = allNPCs.get(noun);
            System.out.println(npc.getDialogue());
        }
    }

    private void handleGoCommand(String noun) {
        Location currentLocation = player.getCurrentLocation();
        if (!currentLocation.reaches(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: Cannot reach %s from %s",
                            PlayerCommand.GO,noun, currentLocation.getName()))
            );
        } else {
            currentLocation = locations.get(noun);
            player.setCurrentLocation(currentLocation);
            System.out.println(currentLocation.getDescription());
        }
    }

    private void handleDescribeCommand() {
        Location currentLocation = player.getCurrentLocation();
        System.out.println(currentLocation.getDescription());
    }

    private void handleTrade(String noun) {
        Location currentLocation = player.getCurrentLocation();
        String npcRegex = String.format("(?i)(%s)\\s+(.+)",
                String.join("|", currentLocation.getNPCs()));
        Pattern pattern = Pattern.compile(npcRegex);
        Matcher matcher = pattern.matcher(noun);

        if (!matcher.matches()) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: expected NPC located at %s and an item name.",
                            PlayerCommand.TRADE, currentLocation.getName()))
            );
            return;
        }

        NPC npc = allNPCs.get(matcher.group(1));
        String itemPlayerWants = matcher.group(2);
        if (!npc.has(itemPlayerWants)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: NPC named %s does not have item named %s",
                            PlayerCommand.TRADE, npc.getName(), itemPlayerWants))
            );
            return;
        }

        String itemExpectedByNPC = npc.expectsWhenTrade(itemPlayerWants).toUpperCase();
        if (!itemExpectedByNPC.isEmpty() && !player.has(itemExpectedByNPC)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: NPC named %s expects %s in exchange for %s, but you do not have it.",
                            PlayerCommand.TRADE, npc.getName(), itemExpectedByNPC, itemPlayerWants))
            );
            return;
        }

        Item item = items.get(itemPlayerWants);
        String tradeText = npc.removeItem(item.getName());
        System.out.println(tradeText);

        if (!itemExpectedByNPC.isEmpty()) {
            player.removeItemFromInventory(items.get(itemExpectedByNPC));
            System.out.printf("You have lost: %s%s", itemExpectedByNPC, System.lineSeparator());
        }

        player.addItemToInventory(item);
        System.out.println("You have gained: " + itemPlayerWants);

    }

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

    private void handleMapCommand() {
        Location currentLocation = player.getCurrentLocation();
        String s = String.format("You are in: %s%s",currentLocation.getName(), System.lineSeparator());
        System.out.println(MAP_LAYOUT);
        System.out.println(Color.GREEN.setFontColor(s));
    }

    /**
     * Performs any necessary cleanup and or closes files.
     */
    private void handleQuit() {
        clearScreen();
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
