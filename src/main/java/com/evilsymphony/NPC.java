package com.evilsymphony;

import java.util.List;
import java.util.Map;
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

    private String getMatchingKeyCaseInsensitive(String key) {
        return items.keySet().stream().filter(itemName -> itemName.equalsIgnoreCase(key)).findFirst().orElse(null);
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
        return getMatchingKeyCaseInsensitive(s) != null;
    }

    public String expectsWhenTrade(String itemPlayerWants) {
        String itemKey = getMatchingKeyCaseInsensitive(itemPlayerWants);
        return items.get(itemKey).getOrDefault("expects", "");
    }

    /**
     * Takes item away from NPC if it matches itemName exactly, and returns trade text said by NPC.
     * @param itemName Item to be removed from NPC.
     * @return Text that NPC says during trade.
     */
    public String removeItem(String itemName) {
        String itemKey = getMatchingKeyCaseInsensitive(itemName);

        if (itemKey == null)
            return "";

        Map<String, String> tradeDetails = items.remove(itemKey);
        return String.format("%s: %s", getName(), tradeDetails.get("tradeText"));
    }
}