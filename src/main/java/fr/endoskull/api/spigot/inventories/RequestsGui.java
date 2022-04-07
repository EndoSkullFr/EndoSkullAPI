package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.bungee.utils.FriendRequest;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestsGui extends CustomGui {
    private int[] glassSlot = {0,1,9,7,8,17,36,45,46,44,52,53};
    private int[] slots = {10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
    public RequestsGui(Player p, int page) {
        super(6, "§cEndoSkull §8» §aDemandes d'amis");
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);

        HashMap<UUID, String> requestsMap = new HashMap<>();
        List<UUID> requests = FriendUtils.getRequests(p.getUniqueId());
        for (int i = 0; i < slots.length * page; i++) {
            requests.remove(0);
        }
        for (UUID uuid : requests) {
            requestsMap.put(uuid, SpigotPlayerInfos.getNameFromUuid(uuid));
        }

        for (int i : glassSlot) {
            setItem(i, CustomItemStack.getPane(3).setName("§r"));
        }
        int i = 0;
        setItem(slots[i], new CustomItemStack(Material.BARRIER).setName("§cVous n'avez pas de demandes d'amis"));
        for (UUID uuid : requestsMap.keySet()) {
            String name = requestsMap.get(uuid);
            setItem(slots[i], CustomItemStack.getPlayerSkull(name).setName(EndoSkullAPI.getPrefix(uuid) + name), player -> {
                new ConfirmGui(name, uuid).open(player);
            });
            if (i >= slots.length) break;
        }
        if (page > 0) {
            setItem(48, new CustomItemStack(Material.ARROW).setName("§ePage précédente"), player -> {
                new RequestsGui(player, page - 1);
            });
        }
        if (requests.size() > slots.length) {
            setItem(50, new CustomItemStack(Material.ARROW).setName("§ePage suivante"), player -> {
                new RequestsGui(player, page + 1);
            });
        }
    }

    private static class ConfirmGui extends CustomGui {

        public ConfirmGui(String name, UUID uuid) {
            super(3, "§cEndoSkull §8» §aDemandes d'amis");
            setItem(13, CustomItemStack.getPlayerSkull(name).setName(EndoSkullAPI.getPrefix(uuid) + name));
            setItem(11, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName("§aAccepter"), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " friend accept " + name);
                player.closeInventory();
            });
            setItem(15, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName("§cRefuser"), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " friend deny " + name);
                player.closeInventory();
            });
        }
    }
}
