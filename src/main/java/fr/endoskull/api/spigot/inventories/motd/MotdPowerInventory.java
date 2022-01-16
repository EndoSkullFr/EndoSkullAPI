package fr.endoskull.api.spigot.inventories.motd;

import fr.endoskull.api.commons.EndoSkullMotd;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;

public class MotdPowerInventory extends CustomGui {
    public MotdPowerInventory(String text, int line, int power) {
        super(3, "§c§lEndoSkull Motd");
        if (power > 0) {
            setItem(12, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName("§cRéduire le power"), player -> {
                new MotdPowerInventory(text, line, power - 1).open(player);
            });
        }
        setItem(13, new CustomItemStack(Material.NAME_TAG, Math.min(power, 64)).setName("§eCliquez pour valider"), player -> {
            EndoSkullMotd motd = MotdManager.getMotd();
            if (line == 1) {
                motd.getFirstLines().put(text, power);
            } else {
                motd.getSecondLines().put(text, power);
            }
            MotdManager.setMotd(motd);
            new MotdInventory().open(player);
        });
        setItem(14, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName("§aAugmenter le power"), player -> {
            new MotdPowerInventory(text, line, power + 1).open(player);
        });
    }
}
