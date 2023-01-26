package com.evilsymphony.main;

import java.util.List;

public class Npc {
    private String name;
    private String location;
    private List<String> dialog;
    private List<String> actions;

    public Npc(String name, String location, List<String> dialog, List<String> actions) {
        this.name = name;
        this.location = location;
        this.dialog = dialog;
        this.actions = actions;
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getDialog() {
        return dialog;
    }

    public void setDialog(List<String> dialog) {
        this.dialog = dialog;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}