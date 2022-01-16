package fr.endoskull.api.spigot.inventories.motd;

import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Title;
import org.bukkit.Material;

public class MotdInventory extends CustomGui {
    public MotdInventory() {
        super(3, "§c§lEndoSkull Motd");
        setItem(11, new CustomItemStack(Material.SIGN).setName("§aAjouter une possibilité sur la première ligne"), player -> {
            player.closeInventory();
            Title.sendTitle(player, 10, 40, 10, "", "§aMarquez la ligne dans le chat");
            MotdManager.getWaitingLines().put(player.getUniqueId(), 1);
        });

        setItem(12, new CustomItemStack(Material.SIGN, 2).setName("§aAjouter une possibilité sur la seconde ligne"), player -> {
            player.closeInventory();
            Title.sendTitle(player, 10, 40, 10, "", "§aMarquez la ligne dans le chat");
            MotdManager.getWaitingLines().put(player.getUniqueId(), 2);
        });

        setItem(14, new CustomItemStack(Material.ANVIL).setName("§aEditer les premières lignes"), player -> {
            new MotdLineInventory(1).open(player);
        });

        setItem(15, new CustomItemStack(Material.ANVIL, 2).setName("§aEditer les secondes lignes"), player -> {
            new MotdLineInventory(2).open(player);
        });
    }
}
