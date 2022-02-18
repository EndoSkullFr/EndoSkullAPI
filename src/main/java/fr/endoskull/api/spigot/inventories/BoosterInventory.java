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

public class BoosterInventory extends CustomGui {
    private int[] glassSlot = {0,1,7,8,9,17,27,35,36,37,43,44};
    public BoosterInventory(Player player) {
        super(5, "§c§lEndoSkull §8» §d§lBoosters");
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        for (int i : glassSlot) {
            setItem(i, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3).setName("§r"));
        }
        setItem(40, new CustomItemStack(Material.NAME_TAG).setName("§a§lComment obtenir des boosters ?")
                .setLore("\n§aBOOSTER PERMANENT\n" +
                        "§7→ §fAvec le grade §bHero §7(+100%) §fet le grade §eVIP §7(+50%)\n\n" +
                        "§aBOOSTERS TEMPORAIRES\n" +
                        "§7→ §fEn ouvrant des Box Ultimes §7(endoskull.fr/shop)\n" +
                        "§7→ §fEn ouvrant des Box Votes §7(endoskull.fr/vote)\n" +
                        "§7→ §fLors d'évènements organisés par le staff"));

        Account account = new AccountProvider(player.getUniqueId()).getAccount();
        BoosterManager booster = account.getBoost();
        setItem(21, new CustomItemStack(Material.EXP_BOTTLE, (int) booster.getBoost() * 10).setName("§dBOOSTER PERMANENT").setLore("\n" + (booster.getBoost() == 0 ? "§7→ §cVous n'avez pas de booster permanent" : "§7→ §fValeur : §a+" + booster.getBoost() * 100 + "%")));
        double tBoost = 0;
        String tPhrase = "";
        SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss dd/MM/yy");
        for (TempBooster tempBoost : booster.getBooster().getTempBoosts()) {
            tBoost += tempBoost.getBoost();
            tPhrase += "§a+" + tempBoost.getBoost() * 100 + "% §fjusqu'au §7" + format.format(new Date(tempBoost.getExpiry())) + "\n";
        }

        setItem(23, new CustomItemStack(Material.POTION, (int) tBoost * 10, (byte) 8233).setName("§dBOOSTERS TEMPORAIRES")
                .setLore("\n" + (tPhrase.equals("") ? "§7→ §cVous n'avez pas de booster temporaire" : tPhrase+ "\n§7→ §fTotal : §a+" + tBoost * 100 + "%")));
    }
}
