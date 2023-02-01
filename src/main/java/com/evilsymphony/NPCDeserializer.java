package com.evilsymphony;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class NPCDeserializer implements JsonDeserializer<Map<String, NPC>> {
    @Override
    public Map<String, NPC> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Map<String, NPC> npcMap = new HashMap<>();

        Type stringListType = new TypeToken<List<String>>(){}.getType();

        Gson gson = new Gson();


        for (JsonElement element : jsonElement.getAsJsonArray()) {
            // Get the JSON object for each location element in the array
            JsonObject npcJsonObject = element.getAsJsonObject();

            String npcName = npcJsonObject.get("name").getAsString();

            List<String> dialogue = gson.fromJson(npcJsonObject.get("dialogue"), stringListType);


            // List<String> actions = npcJsonObject.get("actions").getAsJsonArray().asList()
            npcMap.put(npcName.toUpperCase(), new NPC(npcName, dialogue));
        }
        return npcMap;
    }
}
