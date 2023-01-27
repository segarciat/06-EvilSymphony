package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

public class Npc {
    private String name;
    private final Map<String, String> dialog;

    public Npc(String name, Map<String, String> dialog) {
        this.name = name;
        this.dialog = dialog;
    }

    public static Map<String, Npc> loadNpcs(String jsonFile) {
        // Define the type of the object that will be returned
        Type npcMapType = new TypeToken<Map<String, Npc>>(){}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(npcMapType, new NPCDeserializer())
                .create();

        // Create an empty map of Npc objects to store the NPC data
        Map<String, Npc> npcMap = null;

        try {
            // Open a JsonReader using the FileReader class, passing the jsonFile as parameter
            JsonReader reader = new JsonReader(new FileReader(jsonFile));

            // Use the gson object to parse the json data in the jsonFile, using the jsonreader and the npcListType
            npcMap = gson.fromJson(reader, npcMapType);
        } catch (FileNotFoundException ex) {
            // If the specified jsonFile could not be found, print the stack trace of the exception
            ex.printStackTrace();
        }

        // Return the npcMap, which contains the NPC data from the JSON file
        return npcMap;
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getDialog() {
        return dialog;
    }
}