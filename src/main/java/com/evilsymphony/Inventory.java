package com.evilsymphony;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Set<Item> items = new HashSet<>();

    public Inventory() {
    }

    public Inventory(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void exchangeItem(Item oldItem, Item newItem) {
        items.remove(oldItem);
        items.add(newItem);
    }
}
