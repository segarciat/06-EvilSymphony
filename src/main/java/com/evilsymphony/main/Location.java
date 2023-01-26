package com.evilsymphony.main;

import java.util.List;


class Location {
    private String name;
    private String welcome_message;
    private List<String> NPCs;
    private List<String> items;
    private List<String> directions;

    public Location(String name, String welcome_message, List<String> NPCs, List<String> items, List<String> directions) {
        // update later
        this.name = name;
        this.welcome_message = welcome_message;
        this.NPCs = NPCs;
        this.items = items;
        this.directions = directions;
    }


    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWelcome_message() {
        return welcome_message;
    }

    public void setWelcome_message(String welcome_message) {
        this.welcome_message = welcome_message;
    }

    public List<String> getNPCs() {
        return NPCs;
    }

    public void setNPCs(List<String> NPCs) {
        this.NPCs = NPCs;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }
}
