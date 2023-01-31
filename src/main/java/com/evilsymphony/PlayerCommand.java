package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerCommand {

    private final String name;
    private final String helpText;

    public PlayerCommand(String name, String helpText) {
        this.name = name;
        this.helpText = helpText;

    }

    //method
    public static List<PlayerCommand> loadCommands(String jsonFile) {
        try(Reader reader = new InputStreamReader(Objects.requireNonNull(PlayerCommand.class.getClassLoader().getResourceAsStream(jsonFile)))){
            Type commandListType = new TypeToken<List<PlayerCommand>>(){}.getType();
            Gson gson = new Gson();
            return gson.fromJson(reader,commandListType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // getters and setters
    public String getName() {
        return name;
    }


    public String getHelpText() {
        return helpText;
    }

    public String getDescription() {
        return String.format("%s \n\t %s",getName(),getHelpText());
    }

    //toString
    @Override
    public String toString() {
        return "User{" + "name='" + getName() + "\n" + ", helpText=" + getHelpText() + "}";
    }
}