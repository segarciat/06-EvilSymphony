package com.evilsymphony;

import java.util.*;



public class Game {
    private static final String GAME_SUMMARY_FILE = "game_summary.txt";
    private static final String SPLASH_FILE = "splash.txt";
    private static final String MAP_FILE = "Map.txt";
    private static final String LOCATION_FILE = "location.json";
    private static final String NPC_FILE = "npc.json";
    private static final String ITEM_FILE = "items.json";

    private static final String STARTING_LOCATION = "MUSIC HALL";


    // commands



    private final TextParser parser = new TextParser();
    private  final String MAP_LAYOUT ;

    public Game() {
        MAP_LAYOUT = parser.loadText(MAP_FILE);
    }

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
                        "What would you like to do?\nPlay\tQuit\n",
                        PlayerCommand.getCommandsRegex(PlayerCommand.PLAY,PlayerCommand.QUIT),
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
        Map<String, Item> items = Item.loadItems(ITEM_FILE);
        Inventory inventory = new Inventory();

        Location currentLocation = locations.get(STARTING_LOCATION);

        System.out.println(currentLocation.getDescription());

        while (true) {

            // Prompt user for a command
            inventory.displayContents();
            String userInput = parser.prompt(
                            "Please enter a command > ",
                            PlayerCommand.getCommandsRegex(),
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

            clearScreen();
            if (PlayerCommand.HELP.toString().equalsIgnoreCase(command)) {
                System.out.println(PlayerCommand.getHelpMenu());
            } else if (PlayerCommand.MAP.toString().equalsIgnoreCase(command)){
                displayMap(currentLocation);
            } else if (PlayerCommand.GO.toString().equalsIgnoreCase(command) && currentLocation.containsLocation(noun)) {
                currentLocation = locations.get(noun);
                System.out.println(currentLocation.getDescription());
            } else if (PlayerCommand.TALK.toString().equalsIgnoreCase(command) && currentLocation.containsNpc(noun)) {
                NPC selectedNPC = allNPCs.get(noun);
                System.out.println(selectedNPC.getDialogue());
            } else if (PlayerCommand.EXAMINE.toString().equalsIgnoreCase(command) && currentLocation.containsItem(noun)) {
                Item item = items.get(noun);
                System.out.println(item.getDescription());
            } else if (PlayerCommand.GET.toString().equalsIgnoreCase(command) && currentLocation.containsItem(noun)){
                Item item = items.get(noun);
                inventory.addItem(item);
                currentLocation.removeItem(noun);
            }
            else {

                System.out.println(Color.RED.setFontColor(String.format("%s is invalid for %s command\nType HELP for more context\n", noun,command)));
            }

        }
        handleQuit();
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
