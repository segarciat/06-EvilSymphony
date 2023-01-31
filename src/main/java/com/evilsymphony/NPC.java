package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

public class NPC {
    private String name;
    private final Map<String, String> dialog;

    public NPC(String name, Map<String, String> dialog) {
        this.name = name;
        this.dialog = dialog;
    }

    public static Map<String, NPC> loadNpcs(String jsonFile) {
        // Define the type of the object that will be returned



        try (Reader reader = new InputStreamReader(Objects.requireNonNull(NPC.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type npcMapType = new TypeToken<Map<String, NPC>>() {
            }.getType();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(npcMapType, new NPCDeserializer())
                    .create();
            return gson.fromJson(reader, npcMapType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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