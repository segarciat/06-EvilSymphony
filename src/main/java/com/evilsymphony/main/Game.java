package com.evilsymphony.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class Game {
    private static final String SPLASH_FILE = "src/main/resources/splash.txt";
    private static final String GAME_SUMMARY_FILE = "src/main/resources/game_summary.txt";
    private static final String LOCATION_FILE = "src/main/resources/location.json";
    private static final String NPC_FILE = "src/main/resources/npc.json";


    private final TextParser parser = new TextParser();

    /**
     * Starting point of the application.
     */
    public void run() {
        String splashText = parser.loadText(SPLASH_FILE);
        String gameSummary = parser.loadText(GAME_SUMMARY_FILE);


        System.out.printf("%s\n\n", splashText);
        System.out.printf("%s\n\n", gameSummary);

        // testing Location load
        List<Location> listL = loadLocations(LOCATION_FILE);
        for (Location location : listL) {
            System.out.println(location.getDirections());
        }
        System.out.println("\n");

        String userInput = parser.promptAndCheckForQuit("What would you like to do?\nPlay\n", "(?i)(PLAY)");




        if (userInput.equals(TextParser.QUIT)) {
            System.out.println("Good bye!");
        } else if (userInput.equals("PLAY"))
            startGame();

    }

    /**
     * Initializes main game loop.
     */
    private void startGame() {
        System.out.println("The game has started");
        String userInput = "";
        while (!userInput.equals(TextParser.QUIT)) {
            userInput = parser.promptAndCheckForQuit("Enter a number\n", "\\d+");
            System.out.println("Input matched: " + userInput);
        }
        handleQuit();
    }

    /**
     * Performs any necessary cleanup and or closes files.
     */
    private void handleQuit() {
        System.out.println("Thanks for playing!");
    }

    public static List<Location> loadLocations(String jsonFile) {
        // Create a Gson object for parsing JSON data
        Gson gson = new Gson();

        // Define the type of the object that will be returned
        Type locationListType = new TypeToken<List<Location>>(){}.getType();

        // Create a null list of Location objects to store the location data
        List<Location> locations = null;

        try {
            // Open a JsonReader using the FileReader class, passing the jsonFile as parameter
            JsonReader reader = new JsonReader(new FileReader(jsonFile));

            // Use the gson object to parse the json data in the jsonFile, using the jsonreader and the locationListType
            locations = gson.fromJson(reader, locationListType);
        } catch (FileNotFoundException ex) {
            // If the specified jsonFile could not be found, print the stack trace of the exception
            ex.printStackTrace();
        }

        // Return the locations, which contains the location data from the JSON file
        return locations;
    }


    public static List<Npc> loadNpc(String jsonFile) {
        // Create a Gson object for parsing JSON data
        Gson gson = new Gson();

        // Define the type of the object that will be returned
        Type npcListType = new TypeToken<List<Npc>>(){}.getType();

        // Create a empty list of Npc objects to store the NPC data
        List<Npc> npcList = null;

        try {
            // Open a JsonReader using the FileReader class, passing the jsonFile as parameter
            JsonReader reader = new JsonReader(new FileReader(jsonFile));

            // Use the gson object to parse the json data in the jsonFile, using the jsonreader and the npcListType
            npcList = gson.fromJson(reader, npcListType);
        } catch (FileNotFoundException ex) {
            // If the specified jsonFile could not be found, print the stack trace of the exception
            ex.printStackTrace();
        }

        // Return the npcList, which contains the NPC data from the JSON file
        return npcList;
    }
}
