package com.evilsymphony;

public class Item {

    private final String name;
    private final String alternate;
    private final String description;

    public Item(String name, String alternate, String description) {
        this.name = name;
        this.alternate = alternate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAlternate() {
        return alternate;
    }

    public String getDescription() {
        return String.format("%s%s\t%s%s", getName(), System.lineSeparator(), description, System.lineSeparator());
    }
}