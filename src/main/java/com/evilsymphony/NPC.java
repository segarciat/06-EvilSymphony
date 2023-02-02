package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
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

// Task 629: Change the implementation of the instance method called getDialogue() in the NPC class so that it returns a single, random String from the list

    public  String getDialogue() {
        if (dialogue == null || dialogue.isEmpty()) {
            return "";
        }else {
            int listSize = dialogue.size();
            int randomDialogIndex = (int) (Math.random() * listSize);
            return dialogue.get(randomDialogIndex);
        }


    }
    // getters

    public String getName() {
        return name;
    }
}