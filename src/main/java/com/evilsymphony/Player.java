package com.evilsymphony;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private Location currentLocation;
    private final Set<Item> inventory = new HashSet<>();


   public void setCurrentLocation(Location location) {
       this.currentLocation = location;
   }

   public Location getCurrentLocation() {
       return currentLocation;
   }

   public boolean has(Item item) {
       return inventory.contains(item);
   }

    public boolean has(String itemName) {
        return inventory.stream().anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }

   public void addItemToInventory(Item item) {
       inventory.add(item);
   }

   public Set<Item> getInventory() {
       return Collections.unmodifiableSet(inventory);
   }

    public void removeItemFromInventory(Item item) {
       inventory.remove(item);
    }
}
