package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
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

        StringBuilder sb = new StringBuilder();

        sb.append("You are in ").append(getName())
                .append(System.lineSeparator())
                .append(getWelcomeMessage())
                .append(System.lineSeparator());

        for (String locationName : directions)
            sb.append(PlayerCommand.GO).append(" ").append(locationName).append(System.lineSeparator());

        for (String itemName : items)
            sb.append(PlayerCommand.EXAMINE).append(" ").append(itemName).append(System.lineSeparator());

        for (String npc : NPCs)
            sb.append(PlayerCommand.TALK).append(" ").append(npc).append(System.lineSeparator());

        return sb.toString();
    }



    public static Map<String, Location> loadLocations(String jsonFile) {


        try (Reader reader = new InputStreamReader(Objects.requireNonNull(Location.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type locationListType = new TypeToken<List<Location>>() {}.getType();
            Gson gson = new Gson();
            List<Location> locationList = gson.fromJson(reader, locationListType);
            return locationList.stream().collect(Collectors.toMap(loc -> loc.getName().toUpperCase(), loc -> loc ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // business methods

    public boolean containsLocation(String noun) {
        return containsNounCaseInsensitive(directions, noun);
    }

    public boolean containsNpc(String noun) {
        return containsNounCaseInsensitive(NPCs, noun);
    }

    private boolean containsNounCaseInsensitive(List<String> list, String searchKey) {
        for (String noun : list) {
            if (noun.equalsIgnoreCase(searchKey)) {
                return true;
            }
        }
        return false;
    }

    // getters

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
