package fr.endoskull.api.spigot.inventories.tag;

import com.antarescraft.kloudy.signguilib.SignGUI;
import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ColorInventory extends CustomGui {
    public ColorInventory() {
        super(2, "§dCouleur du Tag");
        for (TagColor value : TagColor.values()) {
            setItem(value.getSlot(), new CustomItemStack(value.getSkull()).setName("§" + value.getColor() + value.getDisplayName()), player -> {
                player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
                Main.getInstance().getWaitingTag().put(player.getUniqueId(), value);
                String[] text = new String[]{"", ChatColor.GREEN + "Entrez le tag", ChatColor.GREEN + "3 caractères", ChatColor.GREEN + "maximum"};
                SignGUI.openSignEditor(player, text);
            });
        }
    }
}
