package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.keys.BoxInventory;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KitKeyInventory extends CustomGui {
    public KitKeyInventory(Player p) {
        super(4, "§9Box Kits");
        Account account = new AccountProvider(p.getUniqueId()).getAccount();
        setItem(13, new CustomItemStack(Material.EXP_BOTTLE).setName("§9Kit aléatoire").setLore("\n§7Débloquez un kit aléatoire parmi ceux\n§7que vous pouvez avoir avec votre rank"));
        setItem(31, new CustomItemStack(Material.ANVIL).setName("§6Ouvrir").setLore("\n§7Vous avez §6" + account.getKitKey() + " §7Clé(s) Kit(s)"), player -> {
            Account fAccount = new AccountProvider(player.getUniqueId()).getAccount();
            if (fAccount.getKitKey() < 1) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                player.sendMessage("§cVous devez posséder une §lClé Kit §cpour effectuer cette action");
                return;
            } else {
                /*fAccount.setKitKey(fAccount.getKitKey() - 1).sendToRedis();
                BoxInventory.giveKit(player);*/
            }
        });
    }
}
