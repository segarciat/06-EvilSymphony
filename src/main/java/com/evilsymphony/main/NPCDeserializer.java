package com.evilsymphony.main;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class NPCDeserializer implements JsonDeserializer<Map<String, Npc>> {
    @Override
    public Map<String, Npc> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Map<String, Npc> npcMap = new HashMap<>();

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            // Get the JSON object for each location element in the array
            JsonObject npcJsonObject = element.getAsJsonObject();

            String npcName = npcJsonObject.get("name").getAsString();

            JsonObject dialogKeyValJson = npcJsonObject.get("dialog").getAsJsonObject();

            Map<String, String> dialogMap = dialogKeyValJson.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().getAsString()
                    ));

            // List<String> actions = npcJsonObject.get("actions").getAsJsonArray().asList()
            npcMap.put(npcName.toUpperCase(), new Npc(npcName, dialogMap));
        }
        return npcMap;
    }
}
