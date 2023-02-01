package com.evilsymphony;

class Item {

    private final String name;
    private final String vocabName;
    private final String description;

    public Item(String name, String vocabName, String description) {
        this.name = name;
        this.vocabName = vocabName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getVocabName() {
        return vocabName;
    }

    public String getDescription() {
        return description;
    }
}