package com.evilsymphony.controller;

import com.evilsymphony.*;
import com.evilsymphony.util.Color;
import com.evilsymphony.PlayerCommand;

import java.util.List;

public class CommandHandler {
    private final Game game;

    public CommandHandler(Game game) {
        this.game = game;
    }
    public void handle(String command, String noun) {
        if (PlayerCommand.QUIT.isAliasOf(command)) {
          game.handleQuit();
        } else if (PlayerCommand.HELP.isAliasOf(command)) {
            handleHelpCommand();
        } else if (PlayerCommand.DESCRIBE.isAliasOf(command)) {
            handleDescribeCommand();
        } else if (PlayerCommand.MAP.isAliasOf(command)){
            handleMapCommand();
        } else if (PlayerCommand.GO.isAliasOf(command)) {
            handleGoCommand(noun);
        } else if (PlayerCommand.TALK.isAliasOf(command)) {
            handleTalkCommand(noun);
        } else if (PlayerCommand.DEALS.isAliasOf(command)) {
            handleDealsCommand(noun);
        } else if (PlayerCommand.TRADE.isAliasOf(command)) {
            handleTrade(noun);
        } else if (PlayerCommand.LOOK.isAliasOf(command)) {
            handleLookCommand(noun);
        } else if (PlayerCommand.GET.isAliasOf(command)){
            handleGetCommand(noun);
        } else if (PlayerCommand.MUSIC_ON.isAliasOf(command)) {
            handleMusicOnCommand();
        } else if (PlayerCommand.MUSIC_OFF.isAliasOf(command)) {
            handleMusicOffCommand();
        } else if (PlayerCommand.MUSIC_VOL.isAliasOf(command)) {
            handleMusicVolCommand();
//        } else if (PlayerCommand.SAVE.isAliasOf(command)) {
//            handleSaveCommand();
        } else {
            handleUnmatchedCommand(command);
        }
    }

    private void handleSaveCommand() {
        game.save();
    }

    private void handleMusicVolCommand() {
        game.getMusic().promptVolume();
    }

    private void handleMusicOffCommand() {
        if(game.getMusic().isPlaying()) {
            game.getMusic().stop();
            game.getMusic().setMusicOptionIsYes(false);
        }
    }

    private void handleMusicOnCommand() {
        if(!game.getMusic().isPlaying()) {
            game.getMusic().play(game.getPlayer().getCurrentLocation().getMusic());
            game.getMusic().setMusicOptionIsYes(true);
        }
    }

    /**
     * Creates a new string with a listing of available commands.
     */
    private void handleHelpCommand() {
        StringBuilder sb = new StringBuilder("Help Menu\n");

        for (PlayerCommand cmd : PlayerCommand.values())
            sb.append(String.format("%s\n\t%s\n\tAliases: %s\n", cmd.getFormat(), cmd.getHelpText(), cmd.getAliases().toString()));

        System.out.println(sb);
    }

    private void handleUnmatchedCommand(String command) {
        String feedback = String.format("The command %s is not yet supported.", command);
        System.out.println(Color.RED.setFontColor(feedback));
    }

    private void handleGetCommand(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        if (!currentLocation.containsItem(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No item named %s in %s",
                            PlayerCommand.GET,noun, currentLocation.getName()))
            );
        } else {
            Item item = game.getItems().get(noun);
            game.getPlayer().addItemToInventory(item);
            currentLocation.removeItem(item.getName());
        }
    }

    private void handleLookCommand(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        if (!currentLocation.containsItem(noun) && !game.getPlayer().has(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No item named %s in %s or in your inventory",
                            PlayerCommand.LOOK,noun, currentLocation.getName()))
            );
        } else {
            Item item = game.getItems().get(noun);
            System.out.println(item.getDescription());
        }
    }

    private void handleDealsCommand(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        if (!currentLocation.containsNpc(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No NPC named %s in %s",
                            PlayerCommand.DEALS,noun, currentLocation.getName()))
            );
        } else {
            NPC npc = game.getAllNPCs().get(noun);
            System.out.println(npc.getTradeDeals());
        }
    }

    private void handleTalkCommand(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        if (!currentLocation.containsNpc(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: No NPC named %s in %s",
                            PlayerCommand.TALK,noun, currentLocation.getName()))
            );
        } else {
            NPC npc = game.getAllNPCs().get(noun);
            System.out.println(npc.getDialogue());
        }
    }

    private void handleGoCommand(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        if (!currentLocation.reaches(noun)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: Cannot reach %s from %s",
                            PlayerCommand.GO, noun, currentLocation.getName()))
            );
        } else {
            if (game.getMusic().isPlaying()) game.getMusic().stop();
            currentLocation = game.getLocations().get(noun);
            game.getPlayer().setCurrentLocation(currentLocation);
            if (game.getMusic().MusicOptionIsYes()) game.getMusic().play(currentLocation.getMusic());
            System.out.println(currentLocation.getDescription());
        }
    }

    private void handleDescribeCommand() {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        System.out.println(currentLocation.getDescription());
    }

    private void handleTrade(String noun) {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        List<String> nounParts = game.getParser().parseNPCTradeNouns(noun, currentLocation.getNPCs());
        if (nounParts == null) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: expected NPC located at %s and an item name.",
                            PlayerCommand.TRADE, currentLocation.getName()))
            );
            return;
        }

        NPC npc = game.getAllNPCs().get(nounParts.get(0));
        String itemPlayerWants = nounParts.get(1);
        if (!npc.has(itemPlayerWants)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: NPC named %s does not have item named %s",
                            PlayerCommand.TRADE, npc.getName(), itemPlayerWants))
            );
            return;
        }

        String itemExpectedByNPC = npc.expectsWhenTrade(itemPlayerWants).toUpperCase();
        if (!itemExpectedByNPC.isEmpty() && !game.getPlayer().has(itemExpectedByNPC)) {
            System.out.println(Color.RED.setFontColor(
                    String.format("%s ERROR: NPC named %s expects %s in exchange for %s, but you do not have it.",
                            PlayerCommand.TRADE, npc.getName(), itemExpectedByNPC, itemPlayerWants))
            );
            return;
        }

        Item item = game.getItems().get(itemPlayerWants);
        String tradeText = npc.removeItem(item.getName());
        System.out.println(tradeText);

        if (!itemExpectedByNPC.isEmpty()) {
            game.getPlayer().removeItemFromInventory(game.getItems().get(itemExpectedByNPC));
            System.out.printf("You have lost: %s%s", itemExpectedByNPC, System.lineSeparator());
        }

        game.getPlayer().addItemToInventory(item);
        System.out.println("You have gained: " + itemPlayerWants);

    }

    private void handleMapCommand() {
        Location currentLocation = game.getPlayer().getCurrentLocation();
        String s = String.format("You are in: %s%s",currentLocation.getName(), System.lineSeparator());
        System.out.println(game.getGameMap());
        System.out.println(Color.GREEN.setFontColor(s));
    }
}
