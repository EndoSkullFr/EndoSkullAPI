package fr.endoskull.api.commons.paf;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FriendSettingsSpigot {
    FRIEND_REQUEST(10, new ItemStack(Material.PAPER), "Demandes d'amis", "§7Vous avez §aactivé §7les demandes d'amis", "§7Vous avez §cdesactivé §7les demandes d'amis"),
    PARTY_REQUEST(12, new ItemStack(Material.ANVIL), "Invitations à des parties", "§7Dorénavant, §atout le monde §7peut vous inviter dans sa partie", "§7Dorénavant, seulement §cvos amis §7peut vous inviter dans sa partie"),
    PRIVATE_MESSAGE(14, new ItemStack(Material.NAME_TAG), "Messages privés", "§7Dorénavant, §atout le monde §7peut vous envoyer des messages privés", "§7Dorénavant, seulement §cvos amis §7peuvent vous envoyer des messages privés"),
    FRIEND_NOTIFICATION(16, new ItemStack(Material.COMMAND), "Notifications de connection d'un ami", "§7Vous §arecevrez §7de nouveau un message quand un ami se connecte", "§7Vous §cne recevrez plus §7de message quand un ami se connecte");

    private int slot;
    private ItemStack item;
    private String name;
    private String enable;
    private String disable;

    FriendSettingsSpigot(int slot, ItemStack item, String name, String enable, String disable) {
        this.slot = slot;
        this.item = item;
        this.name = name;
        this.enable = enable;
        this.disable = disable;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public String getEnable() {
        return enable;
    }

    public String getDisable() {
        return disable;
    }
}
