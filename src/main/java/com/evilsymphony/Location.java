package com.evilsymphony;

import java.util.List;
import java.util.stream.Collectors;


class Location {
    private final String name;
    private final String welcomeMessage;
    private final List<String> NPCs;
    private List<String> items;
    private final List<String> directions;
    private final String music;


    public Location(String name, String welcomeMessage, List<String> NPCs, List<String> items, List<String> directions,String music) {
        // update later
        this.name = name;
        this.welcomeMessage = welcomeMessage;
        this.NPCs = NPCs;
        this.items = items;
        this.directions = directions;
        this.music = music;

    }

    // business methods
    public String getDescription() {

        StringBuilder sb = new StringBuilder();

        sb.append("You are in ").append(getName())
                .append(System.lineSeparator())
                .append(getWelcomeMessage())
                .append(System.lineSeparator());

        sb.append("Connected rooms:\n");
        for (String locationName : directions)
            sb.append("\t").append(locationName).append(System.lineSeparator());

        sb.append("\nNPCs:\n");
        for (String npc : NPCs)
            sb.append("\t").append(npc).append(System.lineSeparator());

        sb.append("\nItems:\n");
        for (String itemName : items)
            sb.append("\t").append(itemName).append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Determines noun is a location name that can be reached from this location.
     *
     * @param noun String that could be the name of a location.
     * @return Whether noun is a location name that can be reached from this location.
     */
    public boolean reaches(String noun) {
        return containsNounCaseInsensitive(directions, noun);
    }

    public boolean containsNpc(String noun) {
        return containsNounCaseInsensitive(NPCs, noun);
    }

    public boolean containsItem(String noun) {
        return containsNounCaseInsensitive(items, noun);
    }

    private boolean containsNounCaseInsensitive(List<String> list, String searchKey) {
        for (String noun : list) {
            if (noun.equalsIgnoreCase(searchKey)) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(String noun) {
        items = items.stream().filter(itemName -> !noun.equalsIgnoreCase(itemName)).collect(Collectors.toList());
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

    public String getMusic() {
        return String.format("music/%s",music);
    }
}
