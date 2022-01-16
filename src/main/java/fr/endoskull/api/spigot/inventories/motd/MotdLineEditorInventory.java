package fr.endoskull.api.spigot.inventories.motd;

import fr.endoskull.api.commons.EndoSkullMotd;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;

public class MotdLineEditorInventory extends CustomGui {
    public MotdLineEditorInventory(String text, int line) {
        super(3, "§c§lEndoSkull Motd");
        EndoSkullMotd motd = MotdManager.getMotd();
        setItem(11, new CustomItemStack(Material.BARRIER).setName("§cSupprimer"), player -> {
            if (line == 1) {
                motd.getFirstLines().remove(text);
            } else {
                motd.getSecondLines().remove(text);
            }
            MotdManager.setMotd(motd);
            new MotdInventory().open(player);
        });
        if (line == 1) {
            if (motd.getFirstLines().get(text) > 0) {
                setItem(14, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName("§cRéduire le power"), player -> {
                    motd.getFirstLines().put(text, motd.getFirstLines().get(text) - 1);
                    MotdManager.setMotd(motd);
                    new MotdLineEditorInventory(text, line).open(player);
                });
            }
            setItem(15, new CustomItemStack(Material.NAME_TAG, Math.min(motd.getFirstLines().get(text), 64)));

            setItem(16, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName("§aAugmenter le power"), player -> {
                motd.getFirstLines().put(text, motd.getFirstLines().get(text) + 1);
                MotdManager.setMotd(motd);
                new MotdLineEditorInventory(text, line).open(player);
            });
        } else {
            if (motd.getSecondLines().get(text) > 0) {
                setItem(14, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName("§cRéduire le power"), player -> {
                    motd.getSecondLines().put(text, motd.getSecondLines().get(text) - 1);
                    MotdManager.setMotd(motd);
                    new MotdLineEditorInventory(text, line).open(player);
                });
            }
            setItem(15, new CustomItemStack(Material.NAME_TAG, Math.min(motd.getSecondLines().get(text), 64)));

            setItem(16, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName("§aAugmenter le power"), player -> {
                motd.getSecondLines().put(text, motd.getSecondLines().get(text) + 1);
                MotdManager.setMotd(motd);
                new MotdLineEditorInventory(text, line).open(player);
            });
        }
        setItem(26, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            new MotdLineInventory(line).open(player);
        });
    }
}
