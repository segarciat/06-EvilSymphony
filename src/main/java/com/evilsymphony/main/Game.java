package com.evilsymphony.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";
    private static final String LOCATION_FILE = "src/main/resources/location.json";
    private static final String NPC_FILE = "src/main/resources/npc.json";
    private static final String COMMAND_FILE = "src/main/resources/commands.json";


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
                "\nInvalid Command. Please enter Play or Quit\n")
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
        Map<String, Npc> allNPCs = Npc.loadNpcs(NPC_FILE);
        List<PlayerCommand> commandList = PlayerCommand.loadCommands(COMMAND_FILE);

        Location currentLocation = locations.get("Music Hall".toUpperCase());

        String userInput = "";
        while (!userInput.equals(TextParser.QUIT)) {
            List<String> directions = currentLocation.getDirections();
            List<String> items = currentLocation.getItems();
            List<String> presentNPCs = currentLocation.getNPCs();

            // Describe the room and what the user can do here.
            StringBuilder sb = new StringBuilder();

            sb.append("You are in ").append(currentLocation.getName())
                    .append("\n")
                    .append(currentLocation.getWelcomeMessage())
                    .append("\n");

            for (String locationName : directions){
                sb.append("Go ").append(locationName).append("\n");
            }
            for (String itemName : items){
                sb.append("examine ").append(itemName).append("\n");
            }
            for (String npc : presentNPCs){
                sb.append("talk ").append(npc).append("\n");
            }

            userInput = parser.promptAndCheckForQuit(
                    sb.toString(),
                    "(?i)(GO|TALK|EXAMINE) ([\\w\\s]+)|help",
                    Color.RED.setFontColor("Invalid Command. To view list of valid commands, type HELP"))
                    .toUpperCase();
            System.out.println("Input matched: " + userInput);

            List<String> commandParts = parseCommand(userInput);
            String command = commandParts.get(0);


            if ("help".equalsIgnoreCase(command)) {
                displayHelpMenu(commandList);
            } else if("go".equalsIgnoreCase(command)) {
                String destination = commandParts.get(1);

                if (directions.contains(destination)){
                    currentLocation = locations.get(destination);
                }else {
                    System.out.println(Color.RED.setFontColor(String.format("%s is invalid\n",destination)));
                    parser.promptContinue();
                }
            } else if ("talk".equalsIgnoreCase(command)) {
                String npc = commandParts.get(1);

                if (presentNPCs.contains(npc)) {
                    Npc selectedNPC = allNPCs.get(npc);
                    System.out.println(selectedNPC.getDialog().get("default"));
                } else {
                    System.out.println(Color.RED.setFontColor(String.format("%s is invalid\n",npc)));
                    parser.promptContinue();
                }
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


    private List<String> parseCommand(String userInput) {
        String[] inputArray = userInput.split(" ");
        ArrayList<String> commandParts = new ArrayList<>();
        String command = inputArray[0];
        commandParts.add(command);

        if (!"help".equalsIgnoreCase(command) && !"quit".equalsIgnoreCase(command)) {

            //        String location = String.join("",inputArray);
            //        String location = userInput.substring(command.length());

            String[] afterCommandArray = Arrays.copyOfRange(inputArray, 1, inputArray.length);

            String afterCommandString = String.join(" ",afterCommandArray);

            commandParts.add(afterCommandString);

        }

        return commandParts;

    }
}
