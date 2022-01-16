package fr.endoskull.api.spigot.inventories.motd;

import fr.endoskull.api.commons.EndoSkullMotd;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;

public class MotdLineInventory extends CustomGui {
    public MotdLineInventory(int line) {
        super(3, "§c§lEndoSkull Motd");
        EndoSkullMotd motd = MotdManager.getMotd();
        int i = 0;
        if (line == 1) {
            for (String s : motd.getFirstLines().keySet()) {
                setItem(i, new CustomItemStack(Material.NAME_TAG, motd.getFirstLines().get(s)).setName(s), player -> {
                    new MotdLineEditorInventory(s, 1).open(player);
                });
                i++;
            }
        } else {
            for (String s : motd.getSecondLines().keySet()) {
                setItem(i, new CustomItemStack(Material.NAME_TAG, motd.getSecondLines().get(s)).setName(s), player -> {
                    new MotdLineEditorInventory(s, 2).open(player);
                });
                i++;
            }
        }
        setItem(26, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            new MotdInventory().open(player);
        });
    }
}
