package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


class Location {
    private final String name;
    private final String welcomeMessage;
    private final List<String> NPCs;
    private final List<String> items;
    private final List<String> directions;

    public Location(String name, String welcomeMessage, List<String> NPCs, List<String> items, List<String> directions) {
        // update later
        this.name = name;
        this.welcomeMessage = welcomeMessage;
        this.NPCs = NPCs;
        this.items = items;
        this.directions = directions;

    }

    // business methods

    public String getDescription() {
        // Describe the room and what the user can do here.
        StringBuilder sb = new StringBuilder();

        sb.append("You are in ").append(getName())
                .append("\n")
                .append(getWelcomeMessage())
                .append("\n");

        for (String locationName : directions)
            sb.append("Go ").append(locationName).append("\n");

        for (String itemName : items)
            sb.append("examine ").append(itemName).append("\n");

        for (String npc : NPCs)
            sb.append("talk ").append(npc).append("\n");

        return sb.toString();
    }

//    private static List<String> toUpperStrings(List<String> strings) {
//        return strings.stream().map(String::toUpperCase).collect(Collectors.toList());
//    }

    public static Map<String,Location> loadLocations(String jsonFile) {
        // Define the type of the object that will be returned


        // Create a new instance of GsonBuilder and register a custom deserializer


        // Create a null list of Location objects to store the location data


        try(Reader reader = new InputStreamReader(Objects.requireNonNull(Location.class.getClassLoader().getResourceAsStream(jsonFile)))){
            Type locationMapType = new TypeToken<Map<String,Location>>(){}.getType();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(locationMapType,new LocationsMapDeserializer())
                    .create();
            return gson.fromJson(reader,locationMapType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public boolean containsLocation(String noun) {
        for(String location : directions) {
            if (location.equalsIgnoreCase(noun)) {
                return true;
            }
        }
        return false;
    }
}
