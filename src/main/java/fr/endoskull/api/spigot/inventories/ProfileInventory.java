package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.commons.BoosterManager;
import fr.endoskull.api.commons.TempBooster;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileInventory extends CustomGui {
    private int[] glassSlot = {0,1,7,8,9,17,27,35,36,37,43,44};
    public ProfileInventory(Player player) {
        super(5, "§cEndoSkull §8» §bPROFIL");
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        for (int i : glassSlot) {
            setItem(i, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3).setName("§r"));
        }

        setItem(23, new CustomItemStack(Material.GOLD_INGOT).setName("§bListe d'amis").setLore("\n§7Tu n'es pas d'amis ?\n§7Faits /f add <pseudo>"), player1 -> player1.performCommand("friendsgui"));
        setItem(20, new CustomItemStack(Material.NAME_TAG).setName("§aDemandes d'amis"), player1 -> player1.performCommand("friendrequest"));
        setItem(24, new CustomItemStack(Material.MINECART).setName("§6Voir votre partie").setLore("\n§7Tu n'es pas dans une partie ?\n§7Tu peux en créer une avec le /p invite <pseudo>\n§7Ou en rejoindre une avec /p join <pseudo>"), player1 -> player1.performCommand("partygui"));
        setItem(21, new CustomItemStack(Material.SIGN).setName("§eParamètres"), player1 -> player1.performCommand("friendsettings"));

    }
}