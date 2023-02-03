package com.evilsymphony;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NPC {
    private final String name;
    private final List<String> dialogue;
    private final Map<String,Map<String,String>> items;

    public NPC(String name, List<String> dialogue, Map<String, Map<String, String>> items) {
        this.name = name;
        this.dialogue = dialogue;
        this.items = items == null? null: items.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toUpperCase(),
                        Map.Entry::getValue)
                );
    }

    public NPC(NPC npc) {
        this(npc.getName(), npc.dialogue, npc.items);
    }

    public static Map<String, NPC> loadNPCs(String jsonFile) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(NPC.class.getClassLoader().getResourceAsStream(jsonFile)))) {
            Type npcListType = new TypeToken<List<NPC>>() {}.getType();
            Gson gson = new Gson();
            List<NPC> npcList = gson.fromJson(reader, npcListType);
            npcList = npcList.stream().map(NPC::new).collect(Collectors.toList());
            return npcList.stream().collect(Collectors.toMap(npc -> npc.getName().toUpperCase(), npc -> npc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  String getDialogue() {
        if (dialogue == null || dialogue.isEmpty()) {
            return "";
        }else {
            int listSize = dialogue.size();
            int randomDialogIndex = (int) (Math.random() * listSize);
            return String.format("%s: %s", getName(), dialogue.get(randomDialogIndex));
        }
    }

    // getters
    public String getName() {
        return name;
    }

    public String getTradeDeals() {
        StringBuilder sb = new StringBuilder(String.format("-------------Trade Deals for NPC: %s-----------", getName()))
                .append(System.lineSeparator());

        // items can be null if an NPC has no "items" attribute (because they don't own items)
        if (items == null || items.isEmpty())
            return sb.append("NO DEALS AVAILABLE").toString();

        for (var item: items.entrySet()) {
            String itemName = item.getKey();
            String expects = item.getValue().getOrDefault("expects", "NOTHING");
            sb.append(String.format("Item: %s%sEXPECTS: %s%s%s",
                    itemName, System.lineSeparator(), expects, System.lineSeparator(), System.lineSeparator()));
        }

        return sb.toString();
    }

    public boolean has(String s) {
        return items != null && items.keySet().stream().anyMatch(itemName -> itemName.equalsIgnoreCase(s));
    }

    public String expectsWhenTrade(String itemPlayerWants) {
        if (!has(itemPlayerWants))
            return "";
        return items.get(itemPlayerWants).getOrDefault("expects", "");
    }

    /**
     * Takes item away from NPC if it matches itemName exactly, and returns trade text said by NPC.
     * @param itemName Item to be removed from NPC.
     * @return Text that NPC says during trade.
     */
    public String removeItem(String itemName) {
        var itemEntry = items == null?
                null:
                items.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);

        if (itemEntry == null)
            return "";

        items.remove(itemEntry.getKey());
        return String.format("%s: %s", getName(), itemEntry.getValue().get("tradeText"));
    }
}