package com.evilsymphony;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum PlayerCommand {
    GO("GO LOCATION", "Changes your room", "MOVE TO"),
    HELP("HELP", "Lists all valid commands, and describes what they do."),
    QUIT("QUIT", "Ends the game.", "EXIT", "LEAVE"),
    GET("GET ITEM", "Takes an item and places it into your inventory.", "GRAB", "PICK UP", "TAKE"),
    DEALS("DEALS NPC", "Displays a list of item trading deals that can be had with NPC."),
    TALK("TALK NPC", "Start a dialog with npc.", "TELL", "SPEAK TO", "CHAT"),
    REPLACE("REPLACE ITEM", "Swap with existing item.", "CHANGE", "SWAP", "EXCHANGE"),
    TRADE("TRADE NPC ITEM", "Obtain ITEM from NPC. If NPC expects an item in return, you lose that item if you have it.", "BARTER"),
    LOOK("LOOK ITEM", "Examine an item to get more information", "EXAMINE", "CHECK", "INSPECT"),
    PLAY("PLAY", "Start the game."),
    MAP("MAP", "Displays the current map."),
    DESCRIBE("DESCRIBE", "Describes the room the player is currently in."),
    MUSIC_ON("MUSIC ON", "Turns on background music"),
    MUSIC_OFF("MUSIC OFF", "Turns off background music"),
    MUSIC_VOL("MUSIC VOL", "Turns on background music");

    private final String format;
    private final String helpText;
    private final Set<String> aliases;

    PlayerCommand(String format, String helpText, String... aliases) {
        this.format = format;
        this.helpText = helpText;
        this.aliases = new HashSet<>(Set.of(aliases));
        this.aliases.add(this.toString().replaceAll("_"," "));
    }

    /**
     * Creates a new string with a listing of available commands.
     */
    public static void displayHelpMenu() {
        StringBuilder sb = new StringBuilder("Help Menu\n");

        for (PlayerCommand cmd : PlayerCommand.values())
            sb.append(String.format("%s\n\t%s\n\tAliases: %s\n", cmd.getFormat(), cmd.getHelpText(), cmd.getAliases().toString()));

        System.out.println(sb);
    }

    public boolean isAliasOf(String s){
        return aliases.contains(s);
    }

    public static String getCommandsRegex(PlayerCommand... commands) {

        return String.format("(?i)(%s)\\s*(.*)",
                Arrays.stream(commands)
                        .flatMap(cmd -> cmd.aliases.stream())
                        .collect(Collectors.joining("|")));

    }

    public static String getCommandsRegex() {
        return getCommandsRegex(PlayerCommand.values());
    }

    public String getFormat() {
        return format;
    }

    public String getHelpText() {
        return helpText;
    }

    public Set<String> getAliases() {
        return aliases;
    }
}