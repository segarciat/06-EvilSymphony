package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class PlayerCommand {

    private final String name;
    private final String helpText;

    public PlayerCommand(String name, String helpText) {
        this.name = name;
        this.helpText = helpText;

    }

    //method
public static List<PlayerCommand> loadCommands(String jsonFile) {
    // Create a Gson object for parsing JSON data
    Gson gson = new Gson();

    // Define the type of the object that will be returned
    Type commandListType = new TypeToken<List<PlayerCommand>>(){}.getType();

    // Create a empty list of Npc objects to store the NPC data
    List<PlayerCommand> commandList = null;

        try {
        // Open a JsonReader using the FileReader class, passing the jsonFile as parameter
        JsonReader reader = new JsonReader(new FileReader(jsonFile));

        // Use the gson object to parse the json data in the jsonFile, using the jsonreader and the npcListType
        commandList = gson.fromJson(reader, commandListType);
    } catch (
    FileNotFoundException ex) {
        // If the specified jsonFile could not be found, print the stack trace of the exception
        ex.printStackTrace();
    }

    // Return the npcList, which contains the NPC data from the JSON file
        return commandList;
}

    // getters and setters
    public String getName() {
        return name;
    }


    public String getHelpText() {
        return helpText;
    }

    public String getDescription() {
        return String.format("%s \n\t %s",getName(),getHelpText());
    }

    //toString
    @Override
    public String toString() {
        return "User{" + "name='" + getName() + "\n" + ", helpText=" + getHelpText() + "}";
    }
}