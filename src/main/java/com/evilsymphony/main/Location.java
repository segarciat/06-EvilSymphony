package com.evilsymphony.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class Location {
    private String name;
    private String welcomeMessage;
    private List<String> NPCs;
    private List<String> items;
    private List<String> directions;

    public Location(String name, String welcomeMessage, List<String> NPCs, List<String> items, List<String> directions) {
        // update later
        this.name = name;
        this.welcomeMessage = welcomeMessage;
        this.NPCs = NPCs.stream().map(String::toUpperCase).collect(Collectors.toList());
        this.items = items.stream().map(String::toUpperCase).collect(Collectors.toList());
        this.directions = directions.stream().map(String::toUpperCase).collect(Collectors.toList());

    }

    //methods

    public static Map<String,Location> loadLocations(String jsonFile) {
        // Define the type of the object that will be returned
        Type locationMapType = new TypeToken<Map<String,Location>>(){}.getType();
        Type locationType = new TypeToken<Location>(){}.getType();

        // Create a new instance of GsonBuilder and register a custom deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(locationMapType,new LocationsMapDeserializer())
                .create();

        // Create a null list of Location objects to store the location data
        Map<String,Location> locations = null;

        try {
            // Open a JsonReader using the FileReader class, passing the jsonFile as parameter
            JsonReader reader = new JsonReader(new FileReader(jsonFile));

            // Use the gson object to parse the json data in the jsonFile, using the jsonreader and the locationListType
            locations = gson.fromJson(reader, locationMapType);
        } catch (FileNotFoundException ex) {
            // If the specified jsonFile could not be found, print the stack trace of the exception
            ex.printStackTrace();
        }

        // Return the locations, which contains the location data from the JSON file
        return locations;
    }


    // getters and setters

    public String getName() {
        return name;
    }


    public String getWelcomeMessage() {
        return welcomeMessage;
    }


    public List<String> getNPCs() {
        return NPCs;
    }


    public List<String> getItems() {
        return items;
    }


    public List<String> getDirections() {
        return directions;
    }

}
