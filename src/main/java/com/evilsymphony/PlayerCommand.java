package com.evilsymphony;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PlayerCommand {
    GO("GO LOCATION", "Changes your room"),
    HELP("HELP", "Lists all valid commands, and describes what they do."),
    QUIT("QUIT", "Ends the game."),
    GET("GET ITEM", "Takes an item and places it into your inventory."),
    TALK("TALK NPC", "Start a dialog with npc."),
    REPLACE("REPLACE ITEM", "Swap with existing item."),
    TRADE("TRADE ITEM", "Trade an item with an NPC."),
    LOOK("LOOK ITEM", "Examine an item to get more information"),
    PLAY("PLAY", "Start the game."),
    MAP("MAP", "Displays the current map."),
    DESCRIBE("DESCRIBE", "Describes the room the player is currently in.");

    private final String format;
    private final String helpText;

    PlayerCommand(String format, String helpText) {
        this.format = format;
        this.helpText = helpText;
    }

    /**
     * Creates a new string with a listing of available commands.
     *
     * @return String of commands.
     */
    public static String getHelpMenu() {
        //System.out.println("HELP\n\tList all valid commands and describe what they do");
        StringBuilder sb = new StringBuilder("Help Menu\n");

        for (PlayerCommand cmd : PlayerCommand.values()) {
            sb.append(String.format("%s\n\t%s\n", cmd.getFormat(), cmd.getHelpText()));
        }
        return sb.toString();
    }


    public static String getCommandsRegex(PlayerCommand... commands) {

        return String.format("(?i)(%s).*",
                Arrays.stream(commands)
                        .map(Enum::toString)
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
}