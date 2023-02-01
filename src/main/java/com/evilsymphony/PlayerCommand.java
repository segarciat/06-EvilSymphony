package com.evilsymphony;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PlayerCommand {
    GO("Go LOCATION", "Changes your room"),
    HELP("HELP", "Lists all valid commands, and describes what they do."),
    QUIT("Quit", "Ends the game."),
    GET("Get ITEM", "Takes an item and places it into your inventory."),
    EXAMINE("Examine ITEM", "Provides information about an item."),
    TALK("Talk NPC", "Start a dialog with npc."),
    FIX("Fix INSTRUMENT", "Repair an item."),
    REPLACE("Replace ITEM", "Swap with existing item."),
    TRADE("Trade ITEM", "Trade an item with an NPC."),
    LOOk("Look ITEM", "Examine an item to get more information"),
    SAVE("Save GAME", "Save the current game."),
    RESTORE("Restore GAME", "Restores a saved game."),
    PLAY("Play", "Start the game.");

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