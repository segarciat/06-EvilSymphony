package com.evilsymphony;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class LocationDeserializer implements JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        // Type object to define the type of the Location class
        JsonObject jsonObj = jsonElement.getAsJsonObject();

        String name = jsonObj.get("name").getAsString();
        String welcomeMessage = jsonObj.get("welcomeMessage").getAsString();
        List<String> directions = jsonStringArrayToList(jsonObj, "directions");
        List<String> items = jsonStringArrayToList(jsonObj, "items");
        List<String> NPCs = jsonStringArrayToList(jsonObj, "NPCs");

        return new Location(name, welcomeMessage, NPCs, items, directions);
    }

    /**
     *
     * @param jsonObject Json object containing array
     * @param arrayKey key for the JsonArray with String objects
     * @return List of strings corresponding to Strings in JsonArray
     */
    private List<String> jsonStringArrayToList(JsonObject jsonObject, String arrayKey) {
        return jsonObject.get(arrayKey).getAsJsonArray().asList().stream()
                .map(JsonElement::getAsString)
                .collect(Collectors.toList());
    }

    public List<Location> loadLocations(String filename) {
        Gson gson = new Gson();
        try(Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename))){
            Type listType = new TypeToken<ArrayList<Location>>(){}.getType();
            return gson.fromJson(reader,listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}