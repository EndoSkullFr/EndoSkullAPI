package fr.endoskull.api.spigot.inventories.key;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.keys.Ultime;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import me.thundertnt33.animatronics.api.Animatronic;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class BoxUltimeInventory extends CustomGui {
    private static boolean openingBox = false;

    public BoxUltimeInventory(Player player) {
        super((Ultime.values().length-1)/9 + 3, "§c§lEndoSkull §8» §c§lBox Ultime");
        int lines = (Ultime.values().length-1)/9 + 3;
        player.playSound(player.getLocation(), Sound.CAT_MEOW, 50, 50);
        Account account = new AccountProvider(player.getUniqueId()).getAccount();
        int i = 0;
        for (Ultime value : Ultime.values()) {
            setItem(i, new CustomItemStack(value.getItem()).setName(value.getName()).setLore("\n§7Probabilité: " + value.getName().substring(0, 2) + value.getPourcent() + "%"));
            i++;
        }
        for (int j = lines * 9 - 18; j < lines * 9 - 9; j++) {
            setItem(j, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14).setName("§r"));
        }
        boolean s = account.getUltimeKey() > 1;
        setItem(lines * 9 - 5, new CustomItemStack(Material.ANVIL).setName("§c§lOUVRIR").setLore("\n§7Vous avez §c" + account.getUltimeKey() + " §7Clé" + (s ? "s" : "" ) +" Ultime" + (s ? "s" : "" )), player1 -> {
            player1.closeInventory();
            Account account1 = new AccountProvider(player1.getUniqueId()).getAccount();
            if (account1.getUltimeKey() < 1) {
                player1.closeInventory();
                player1.playSound(player1.getLocation(), Sound.VILLAGER_NO, 50, 50);
                player1.sendMessage("§cVous devez posséder une §lClé Ultime §cpour effectuer cette action");
                return;
            } else {
                if (openingBox) {
                    player1.sendMessage("§cUn joueur est déjà en train d'ouvrir une box, merci de patienter la fin de l'ouverture de celle-ci");
                    player1.playSound(player1.getLocation(), Sound.CAT_HIT, 1f, 1f);
                    return;
                }
                account1.setUltimeKey(account1.getUltimeKey() - 1).sendToRedis();
                openBox(player1);
            }
        });

    }

    private void openBox(Player player) {
        openingBox = true;
        Animatronic animatronic = new Animatronic("BoxAnim");
        List<Ultime> loots = new ArrayList<>();
        for (Ultime value : Ultime.values()) {
            for (int i = 0; i < value.getProbability() * 2; i++) {
                loots.add(value);
            }
        }
        Collections.shuffle(loots);
        animatronic.getArmorstand().setHelmet(loots.get(new Random().nextInt(loots.size())).getItem());
        animatronic.start();

        int[] ticks = {46,8,8,6,6,6,6,5,5,5,5,4,4,4,4,3,3,3,3,3,2,2,2,2,2,2,2,2};
        player.getWorld().playSound(player.getLocation(), Sound.ANVIL_USE, 1f, 1f);
        playTick(player, animatronic, loots, ticks, 0);
    }

    private void launch(Player player, Animatronic animatronic, List<Ultime> loots) {
        player.getWorld().playSound(animatronic.getArmorstand().getLocation(), Sound.FIREWORK_LAUNCH, 1f, 1f);
        player.getWorld().playEffect(animatronic.getArmorstand().getLocation(), Effect.LARGE_SMOKE, 500);
        animatronic.getArmorstand().setHelmet(loots.get(new Random().nextInt(loots.size())).getItem());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.getWorld().playSound(animatronic.getArmorstand().getLocation(), Sound.EXPLODE, 1f, 1f);
            player.getWorld().playEffect(animatronic.getArmorstand().getLocation(), Effect.EXPLOSION_LARGE, 500);
            Ultime reward = loots.get(new Random().nextInt(loots.size()));
            animatronic.getArmorstand().setHelmet(reward.getItem());
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                player.getWorld().playSound(animatronic.getArmorstand().getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                player.getWorld().playEffect(animatronic.getArmorstand().getLocation().add(0,1,0), Effect.NOTE, 500);
                animatronic.getArmorstand().setHelmet(reward.getItem());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.getCommand().replace("%player%", player.getName()));
                Bukkit.broadcastMessage("§cEndoSkull §8» §a" + player.getName() + " §fvient d'obtenir " + reward.getName() + " §fdans sa Clé Ultime");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    animatronic.getArmorstand().remove();
                    openingBox = false;
                }, 60);
            }, 40);
        }, 15);
    }

    private void playTick(Player player, Animatronic animatronic, List<Ultime> loots, int[] ticks, final int i) {
        if (ticks.length <= i) {
            launch(player, animatronic, loots);
            return;
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.getWorld().playSound(animatronic.getArmorstand().getLocation(), Sound.NOTE_PLING, 1f, 1f);
            animatronic.getArmorstand().setHelmet(loots.get(new Random().nextInt(loots.size())).getItem());
            playTick(player, animatronic, loots, ticks, i + 1);
        }, ticks[i]);
    }
}
