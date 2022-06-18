package fr.endoskull.api.spigot.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CustomItemStack extends ItemStack {

    public CustomItemStack(ItemStack item, String displayName, String lore) {
        super(item);
    }

    public CustomItemStack(ItemStack item, String displayName) {
        super(item);
    }

    public CustomItemStack(ItemStack item) {
        super(item);
    }

    public CustomItemStack(Material material, int amount, byte data) {
        super(material, amount, data);
    }

    public CustomItemStack(Material material, int amount) {
        super(material, amount);
    }

    public CustomItemStack(Material material) {
        super(material);
    }

    public CustomItemStack setLore(String lore) {
        if (lore == null || lore.equals("")) {
            return this;
        }

        ItemMeta meta = this.getItemMeta();

        // Remove attributes
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        // Set Lore on ItemMeta
        lore = ChatColor.translateAlternateColorCodes('&', lore);
        String[] loreString = lore.split("\n");
        meta.setLore(new ArrayList<>(Arrays.asList(loreString)));
        this.setItemMeta(meta);

        return this;
    }

    public CustomItemStack setName(String displayName) {
        if (displayName == null || displayName.equals("")) {
            return this;
        }

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        this.setItemMeta(meta);

        return this;
    }

    public CustomItemStack setLeatherColor(Color color) {
        LeatherArmorMeta lch = (LeatherArmorMeta) this.getItemMeta();
        lch.setColor(color);
        this.setItemMeta(lch);
        return this;
    }

    public CustomItemStack setLoreAction(String actionName) {
        if (actionName.startsWith("§")) {
            this.addLineToLore("§7", actionName);
        } else {
            this.addLineToLore("§7", "§6§l» §eCliquez pour " + actionName);
        }
        return this;
    }

    public CustomItemStack setCustomAmount(int amount) {
        this.setAmount(amount);
        return this;
    }

    public CustomItemStack setData(byte data) {
        this.setDurability(data);
        return this;
    }

    public CustomItemStack setGlow(boolean enabled) {
        if (enabled) {
            this.setGlow();
        }
        return this;
    }

    public CustomItemStack setUnbreakable() {
        ItemMeta meta = this.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setUnbreakable(boolean enabled) {
        ItemMeta meta = this.getItemMeta();
        meta.spigot().setUnbreakable(enabled);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setGlow() {
        this.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.setItemMeta(meta);
        return this;
    }

    public static CustomItemStack getPane(int data) {
        return new CustomItemStack(Material.STAINED_GLASS_PANE).setData((byte) data);
    }

    public CustomItemStack addCustomEnchantment(Enchantment enchantment, int level) {
        super.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public void addLineToLore(String... lines) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        int i = -1;
        for (String line : lines) {
            ++i;
            if (i == 0 && line == null && !hasAlreadyLore()) {
                continue;
            }
            lore.add(line == null ? "" : line);
        }
        meta.setLore(lore);
        this.setItemMeta(meta);
    }

    private boolean hasAlreadyLore() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null) {
            return false;
        }
        List<String> lore = meta.getLore();
        return lore != null && lore.size() > 0;
    }

    public static CustomItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url.isEmpty())
            return new CustomItemStack(head);
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta((ItemMeta)headMeta);
        return new CustomItemStack(head);
    }


    public static CustomItemStack getPlayerSkull(String name) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta headMeta = (SkullMeta)head.getItemMeta();
        headMeta.setOwner(name);
        head.setItemMeta(headMeta);
        return new CustomItemStack(head);
    }
}
