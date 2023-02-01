package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NPC {
    private final String name;
    private final List<String> dialogue;

    public NPC(String name,  List<String> dialogue) {
        this.name = name;
        this.dialogue = dialogue;
    }

    public static Map<String, NPC> loadNPCs(String jsonFile) {
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

    public  List<String> getDialogue() {
        return dialogue;
    }
}