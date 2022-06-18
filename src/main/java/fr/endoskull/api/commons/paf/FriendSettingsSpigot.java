package fr.endoskull.api.commons.paf;

import fr.endoskull.api.spigot.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FriendSettingsSpigot {
    FRIEND_REQUEST(10, new ItemStack(Material.PAPER), MessageUtils.Global.FriendSettings.FRIEND_REQUEST, MessageUtils.Global.FriendSettings.FRIEND_REQUEST_ENABLE, MessageUtils.Global.FriendSettings.FRIEND_REQUEST_DISABLE),
    PARTY_REQUEST(12, new ItemStack(Material.ANVIL), MessageUtils.Global.FriendSettings.PARTY_REQUEST, MessageUtils.Global.FriendSettings.PARTY_REQUEST_ENABLE, MessageUtils.Global.FriendSettings.PARTY_REQUEST_DISABLE),
    PRIVATE_MESSAGE(14, new ItemStack(Material.NAME_TAG), MessageUtils.Global.FriendSettings.PRIVATE_MESSAGE, MessageUtils.Global.FriendSettings.PRIVATE_MESSAGE_ENABLE, MessageUtils.Global.FriendSettings.PRIVATE_MESSAGE_DISABLE),
    FRIEND_NOTIFICATION(16, new ItemStack(Material.COMMAND), MessageUtils.Global.FriendSettings.FRIEND_NOTIFICATION, MessageUtils.Global.FriendSettings.FRIEND_NOTIFICATION_ENABLE, MessageUtils.Global.FriendSettings.FRIEND_NOTIFICATION_DISABLE);

    private int slot;
    private ItemStack item;
    private MessageUtils name;
    private MessageUtils enable;
    private MessageUtils disable;

    FriendSettingsSpigot(int slot, ItemStack item, MessageUtils name, MessageUtils enable, MessageUtils disable) {
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

    public MessageUtils getName() {
        return name;
    }

    public MessageUtils getEnable() {
        return enable;
    }

    public MessageUtils getDisable() {
        return disable;
    }
}
