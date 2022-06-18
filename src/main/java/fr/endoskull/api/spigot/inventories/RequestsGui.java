package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.spigot.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class RequestsGui extends CustomGui {
    private int[] glassSlot = {0,1,9,7,8,17,36,45,46,44,52,53};
    private int[] slots = {10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
    public RequestsGui(Player p, int page) {
        super(6, Languages.getLang(p).getMessage(MessageUtils.Global.GUI_FREQUESTS));
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
        setItem(slots[i], new CustomItemStack(Material.BARRIER).setName(Languages.getLang(p).getMessage(MessageUtils.Global.ANY_FREQUESTS)));
        for (UUID uuid : requestsMap.keySet()) {
            String name = requestsMap.get(uuid);
            setItem(slots[i], CustomItemStack.getPlayerSkull(name).setName(EndoSkullAPI.getPrefix(uuid) + name), player -> {
                new ConfirmGui(name, uuid, player).open(player);
            });
            if (i >= slots.length) break;
        }
        if (page > 0) {
            setItem(48, new CustomItemStack(Material.ARROW).setName(Languages.getLang(p).getMessage(MessageUtils.Global.PREVIOUS_PAGE)), player -> {
                new RequestsGui(player, page - 1);
            });
        }
        if (requests.size() > slots.length) {
            setItem(50, new CustomItemStack(Material.ARROW).setName(Languages.getLang(p).getMessage(MessageUtils.Global.NEXT_PAGE)), player -> {
                new RequestsGui(player, page + 1);
            });
        }
    }

    private static class ConfirmGui extends CustomGui {

        public ConfirmGui(String name, UUID uuid, Player p) {
            super(3, Languages.getLang(p).getMessage(MessageUtils.Global.GUI_FREQUESTS));
            setItem(13, CustomItemStack.getPlayerSkull(name).setName(EndoSkullAPI.getPrefix(uuid) + name));
            setItem(11, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName(Languages.getLang(p).getMessage(MessageUtils.Global.ACCEPT)), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " friend accept " + name);
                player.closeInventory();
            });
            setItem(15, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName(Languages.getLang(p).getMessage(MessageUtils.Global.DENY)), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " friend deny " + name);
                player.closeInventory();
            });
        }
    }
}
