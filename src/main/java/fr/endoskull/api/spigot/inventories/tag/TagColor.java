package fr.endoskull.api.spigot.inventories.tag;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public enum TagColor {
    DARK_RED(0, '4', "Rouge Foncé", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY0ZGMzYzM3NTNiZjViMGI3ZjA4MWNkYjQ5YjgzZDM3NDI4YTEyZTQxODdmNjM0NmRlYzA2ZmFjNTRjZSJ9fX0="),
    RED(1, 'c', "Rouge", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZkZTNiZmNlMmQ4Y2I3MjRkZTg1NTZlNWVjMjFiN2YxNWY1ODQ2ODRhYjc4NTIxNGFkZDE2NGJlNzYyNGIifX19"),
    GOLD(2, '6', "Orange", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM0ODg2ZWYzNjJiMmM4MjNhNmFhNjUyNDFjNWM3ZGU3MWM5NGQ4ZWM1ODIyYzUxZTk2OTc2NjQxZjUzZWEzNSJ9fX0="),
    YELLOW(3, 'e', "Jaune", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY0MTY4MmY0MzYwNmM1YzlhZDI2YmM3ZWE4YTMwZWU0NzU0N2M5ZGZkM2M2Y2RhNDllMWMxYTI4MTZjZjBiYSJ9fX0="),
    DRAK_GREEN(4, '2', "Vert Foncé", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM5ZTYwMWVkOTE5OGRiYjM0YzUxZGRmMzIzOTI5ZjAxYTVmOTU4YWIxMTEzM2UzZTA0MDdiNjk4MzkzYjNmIn19fQ=="),
    GREEN(5, 'a', "Vert", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5YjI3ZmNjZDgwOTIxYmQyNjNjOTFkYzUxMWQwOWU5YTc0NjU1NWU2YzljYWQ1MmU4NTYyZWQwMTgyYTJmIn19fQ=="),
    AQUA(6, 'b', "Aqua", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmNzVjYzJiN2YzYjI0MTgyNDJlNDU0MTg3MTU2YzUxYjA1OGU0MzQyNTQ4OWE4MGE1NTY4NTQyYjgzYzk0In19fQ=="),
    DARK_AQUA(7, '3', "Cyan", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBmYjFmMTNlY2I3ZmJiMmZhNDljZDAzZDM1N2ZhN2UyNzg1MDJiNzg3MzA2MDJhYWExMDY1NWU0ZDk0OTBlMSJ9fX0="),
    DARK_BLUE(8, '1', "Bleu Foncé", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE0NjA1MzAxMmM2OGYyODlhYmNmYjE3YWI4MDQyZDVhZmJhOTVkY2FhOTljOTljMWUwMzYwODg2ZDM1In19fQ=="),
    BLUE(10, '9', "Bleu", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhZmExNTU1ZTlmODc2NDgxZTNjNDI5OWVjNmU5MWQyMmI0MDc1ZTY3ZTU4ZWY4MGRjZDE5MGFjZTY1MTlmIn19fQ=="),
    PINK(11, 'd', "Rose", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA1YzE3NjUwZTVkNzQ3MDEwZThiNjlhNmYyMzYzZmQxMWViOTNmODFjNmNlOTliZjAzODk1Y2VmYjkyYmFhIn19fQ=="),
    PURPLE(12, '5', "Violet", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ4NzJjOGY3YTZjZWY3OTg2NDc2OWYwY2ZmNzFlZjIzZjU2NTY1NjdkZTAwMDFhZWYzNmM2YjYxNjJhYjAyZCJ9fX0="),
    WHITE(13, 'f', "Blanc", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ5MzExN2I5ZTE4MGUwZGMzOWU1ZThhMDUwODQ4MmNmMWY2MGU0NDZlMDIyOTc4ZmUwNjUxYTU2MmE1OTdmIn19fQ=="),
    GRAY(14, '7', "Gris", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQzY2ZjMjM5MDA2YjI1N2I4YjIwZjg1YTdiZjQyMDI2YzRhZGEwODRjMTQ0OGQwNGUwYzQwNmNlOGEyZWEzMSJ9fX0="),
    DARK_GRAY(15, '8', "Gris Foncé", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4ZjMyMzQ2MmZiNDM0ZTkyOGJkNjcyODYzOGM5NDRlZTNkODEyZTE2MmI5YzZiYTA3MGZjYWM5YmY5In19fQ=="),
    BLACK(16, '0', "Noir", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3YTJmMjE4YTZlNmUzOGYyYjU0NWY2YzE3NzMzZjRlZjliYmIyODhlNzU0MDI5NDljMDUyMTg5ZWUifX19");

    private int slot;
    private char color;
    private String displayName;
    private String url;

    TagColor(int slot, char color, String displayName, String url) {
        this.slot = slot;
        this.color = color;
        this.displayName = displayName;
        this.url = url;
    }

    public int getSlot() {
        return slot;
    }

    public char getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrl() {
        return url;
    }

    public ItemStack getSkull() {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
}
