package fr.endoskull.api.spigot.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;

public class VanishUtils {

    private static final HashMap<Player, GameMode> lastGamemode = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> lastArmor = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> lastInventory = new HashMap<>();
    private static final HashMap<Player, Collection<PotionEffect>> lastEffects = new HashMap<>();

    public static void vanish(Player player, boolean save) {
        player.setMetadata("vanished", new FixedMetadataValue(Main.getInstance(), true));
        Account account = AccountProvider.getAccount(player.getUniqueId());
        account.setProperty("vanished", "true");

        if (save) {
            lastGamemode.put(player, player.getGameMode());
            lastArmor.put(player, player.getInventory().getArmorContents());
            lastInventory.put(player, player.getInventory().getContents());
            lastEffects.put(player, player.getActivePotionEffects());
        }

        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(activePotionEffect.getType());
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, new CustomItemStack(Material.SLIME_BALL).setName("§aKnockback §7§o(Clique Droit)"));
        player.getInventory().setItem(1, new CustomItemStack(Material.PACKED_ICE).setName("§bFreeze §7§o(Clique Droit)"));
        player.getInventory().setItem(3, new CustomItemStack(Material.SKULL_ITEM).setName("§eProfil AntiCheat §7§o(Clique Droit)"));
        player.getInventory().setItem(4, new CustomItemStack(Material.PAPER).setName("§fHistorique §7§o(Clique Droit)"));
        player.getInventory().setItem(5, new CustomItemStack(Material.CHEST).setName("§6Invsee §7§o(Clique Droit)"));
        player.getInventory().setItem(7, new CustomItemStack(Material.STICK).setName("§cDonner un coup §7§o(Clique Gauche)"));
        player.getInventory().setItem(8, new CustomItemStack(Material.ANVIL).setName("§4Appliquer une sanction §7§o(Clique Droit)"));
        player.getInventory().setHelmet(CustomItemStack.getPlayerSkull(player.getName()));

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            hidePlayer(player, onlinePlayer);
        }
    }

    public static void unvanish(Player player) {
        player.removeMetadata("vanished", Main.getInstance());
        player.setFlying(false);
        player.setAllowFlight(false);
        Account account = AccountProvider.getAccount(player.getUniqueId());
        account.setProperty("vanished", "false");

        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(activePotionEffect.getType());
        }

        if (lastGamemode.containsKey(player)) {
            player.setGameMode(lastGamemode.get(player));
            lastGamemode.remove(player);
        }
        if (lastArmor.containsKey(player)) {
            player.getInventory().setArmorContents(lastArmor.get(player));
            lastArmor.remove(player);
        }
        if (lastInventory.containsKey(player)) {
            player.getInventory().setContents(lastInventory.get(player));
            lastInventory.remove(player);
        }
        if (lastEffects.containsKey(player)) {
            for (PotionEffect potionEffect : lastEffects.get(player)) {
                player.addPotionEffect(potionEffect);
            }

            lastEffects.remove(player);
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            showPlayer(player, onlinePlayer);
        }
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue vanished : player.getMetadata("vanished")) {
            if (vanished.asBoolean()) return true;
        }
        return false;
    }

    public static void hidePlayer(Player player, Player viewer) {
        if (!viewer.hasPermission("endoskull.staff")) {
            viewer.hidePlayer(player);
        }
    }

    public static void showPlayer(Player player, Player viewer) {
        if (!viewer.hasPermission("endoskull.staff")) {
            viewer.showPlayer(player);
        }
    }
}
