package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

class JSONLoader {

    /**
     * Parses a JSON file that contains an array of objects as a Map with a string key an object as the value.
     *
     * @param jsonFile A file containing an array of objects.
     * @param classType The class object corresponding to the type requested.
     * @param keyMapper A function that maps an object of the requested type to a String key for the map.
     * @param <T> The type of the elements in the list.
     * @return A Map with a String key obtained with the keyMapper, and value being the actual object wanted.
     */
    public static <T> Map<String, T> loadFromJsonAsMap(String jsonFile, Class<T> classType, Function<T, String> keyMapper) {
        return loadFromJsonAsList(jsonFile, classType).stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * Parse a JSON file containing an array of objects as a List of objects of that type.
     *
     * @param jsonFile A file containing an array of objects.
     * @param classObject The class object corresponding to the type T requested.
     * @param <T> The type of the elements in the list.
     * @return A list of objects of the type requested.
     */
    public static <T> List<T> loadFromJsonAsList(String jsonFile, Class<T> classObject) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(classObject.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type listType = TypeToken.getParameterized(List.class, classObject).getType();
            Gson gson = new Gson();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
