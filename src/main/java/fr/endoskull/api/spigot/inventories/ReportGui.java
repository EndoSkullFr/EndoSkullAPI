package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.SanctionEnum;
import org.bukkit.entity.Player;

public class ReportGui extends CustomGui {

    public ReportGui(String targetName, Player p) {
        super(3, "Report " + targetName, p);
        for (SanctionEnum sanction : SanctionEnum.values()) {
            setItem(sanction.getSlot(), new CustomItemStack(sanction.getItem()).setName(sanction.getName()), player -> {
                player.closeInventory();
                player.performCommand("report " + targetName + " " + sanction.getName());
            });
        }
    }
}
