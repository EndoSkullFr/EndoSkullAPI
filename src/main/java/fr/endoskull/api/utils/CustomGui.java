package fr.endoskull.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class CustomGui {
    public static Map<UUID, CustomGui> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    private UUID uuid;
    private Inventory yourInventory;
    private Map<Integer, CustomGuiAction> actions;

    public CustomGui(int line, String invName) {
        uuid = UUID.randomUUID();
        yourInventory = Bukkit.createInventory(null, line * 9, invName);
        actions = new HashMap<>();
        inventoriesByUUID.put(getUuid(), this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public interface CustomGuiAction {
        void click(Player player);
    }

    public void setItem(int slot, ItemStack stack, CustomGuiAction action){
        yourInventory.setItem(slot, stack);
        if (action != null){
            actions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack stack){
        setItem(slot, stack, null);
    }

    public void open(Player p){
        p.openInventory(yourInventory);
        openInventories.put(p.getUniqueId(), getUuid());
    }

    public static Map<UUID, CustomGui> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Map<Integer, CustomGuiAction> getActions() {
        return actions;
    }

    public void delete(){
        for (Player p : Bukkit.getOnlinePlayers()){
            UUID u = openInventories.get(p.getUniqueId());
            if (u.equals(getUuid())){
                p.closeInventory();
            }
        }
        inventoriesByUUID.remove(getUuid());
    }

}

