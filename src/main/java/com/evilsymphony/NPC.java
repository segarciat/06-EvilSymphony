package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NPC {
    private final String name;
    private final List<String> dialogue;
    private final Map<String,Map<String,String>> items;

    public NPC(String name, List<String> dialogue, Map<String, Map<String, String>> items) {
        this.name = name;
        this.dialogue = dialogue;
        this.items = items;
    }

    public static Map<String, NPC> loadNPCs(String jsonFile) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(NPC.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type npcListType = new TypeToken<List<NPC>>() {}.getType();
            Gson gson = new Gson();
            List<NPC> npcList = gson.fromJson(reader, npcListType);
            return npcList.stream().collect(Collectors.toMap(npc -> npc.getName().toUpperCase(), npc -> npc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public  List<String> getDialogue() {
        System.out.println(items);
        return dialogue;

    }
}