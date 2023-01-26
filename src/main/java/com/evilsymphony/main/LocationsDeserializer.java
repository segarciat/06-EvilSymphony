package com.evilsymphony.main;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class LocationsDeserializer implements JsonDeserializer<Map<String,Location>> {

    @Override
    public Map<String, Location> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Map <String,Location> locationMap = new HashMap<String,Location>();

        Gson gson = new Gson();

        Type locationType = new TypeToken<Location>(){}.getType();

        for (JsonElement element : jsonElement.getAsJsonArray()){

            JsonObject locationJson = element.getAsJsonObject();
            String locationName = locationJson.get("name").getAsString();
            Location location = gson.fromJson(element, locationType);
            locationMap.put(locationName,location);

        }
        return locationMap;
    }
}