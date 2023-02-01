package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Item {

    private final String name;
    private final String vocabName;
    private final String description;

    public Item(String name, String vocabName, String description) {
        this.name = name;
        this.vocabName = vocabName;
        this.description = description;
    }

    public static Map<String, Item> loadItems(String jsonFile) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(Location.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type itemListType = new TypeToken<List<Item>>() {}.getType();
            Gson gson = new Gson();
            List<Item> locationList = gson.fromJson(reader, itemListType);
            return locationList.stream().collect(Collectors.toMap(item -> item.getName().toUpperCase(), item -> item ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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