package com.evilsymphony;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

class LocationDeserializer implements JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        // Gson object to deserialize the JSON elements to the locationMap
        Gson gson = new Gson();

        // Type object to define the type of the Location class

        JsonObject jsonObj = jsonElement.getAsJsonObject();

        String name = jsonObj.get("name").getAsString();
        String welcomeMessage = jsonObj.get("welcomeMessage").getAsString();
        List<String> directions = jsonObj.get("directions").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());
        List<String> items = jsonObj.get("items").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());
        List<String> npcs = jsonObj.get("NPCs").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());

        Location location = new Location(name,welcomeMessage,npcs,items,directions);

        return location;
    }
}