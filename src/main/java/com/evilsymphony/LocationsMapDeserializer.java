package com.evilsymphony;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class LocationsMapDeserializer implements JsonDeserializer<Map<String, Location>> {



    @Override
    public Map<String, Location> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        // Type object to define the type of the Location class
        Type locationType = new TypeToken<Location>(){}.getType();

        // Map to store the location objects
        Map <String,Location> locationMap = new HashMap<String,Location>();

        // Gson object to deserialize the JSON elements to the locationMap
        Gson gson = new GsonBuilder().
                registerTypeAdapter(locationType, new LocationDeserializer())
                .create();

        // Iterate through the JSON array
        for (JsonElement element : jsonElement.getAsJsonArray()){

            // Get the JSON object for each location element in the array
            JsonObject locationJson = element.getAsJsonObject();

            // Get the name of the location, this will be used to set the key for the map
            String locationName = locationJson.get("name").getAsString();

            // Deserialize the JSON element into a Location object, converts element to the locationType
            Location location = gson.fromJson(element, locationType);

            // Add the location to the map with the name as the key
            locationMap.put(locationName.toUpperCase(),location);
        }
        return locationMap;
    }
}