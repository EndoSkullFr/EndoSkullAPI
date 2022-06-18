package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.boost.BoosterManager;
import fr.endoskull.api.commons.boost.TempBooster;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoosterInventory extends CustomGui {
    private int[] glassSlot = {0,1,7,8,9,17,27,35,36,37,43,44};
    public BoosterInventory(Player player) {
        super(5, Languages.getLang(player).getMessage(MessageUtils.Global.GUI_BOOSTER), player);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        for (int i : glassSlot) {
            setItem(i, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3).setName("§r"));
        }
        setItem(40, new CustomItemStack(Material.NAME_TAG).setName(Languages.getLang(player).getMessage(MessageUtils.Global.HOW_TO_BOOSTER))
                .setLore(Languages.getLang(player).getMessage(MessageUtils.Global.HOW_TO_BOOSTER_DESC)));

        Account account = new AccountProvider(player.getUniqueId()).getAccount();
        BoosterManager booster = account.getBoost();
        setItem(21, new CustomItemStack(Material.EXP_BOTTLE, (int) booster.getBoost() * 10).setName(Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_PERM))
                .setLore("\n" + (booster.getBoost() == 0 ? Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_NO_PERM) : Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_PERM_DESC).replace("{boost}", String.valueOf(booster.getBoost() * 100)))));
        double tBoost = 0;
        String tPhrase = "";
        SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss dd/MM/yy");
        for (TempBooster tempBoost : booster.getBooster().getTempBoosts()) {
            tBoost += tempBoost.getBoost();
            tPhrase += Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_LINE).replace("{boost}", String.valueOf(tempBoost.getBoost() * 100)).replace("{date}", format.format(new Date(tempBoost.getExpiry())));
        }

        setItem(23, new CustomItemStack(Material.POTION, (int) tBoost * 10, (byte) 8233).setName(Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_TEMP))
                .setLore("\n" + (tPhrase.equals("") ? Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_NO_TEMP) : tPhrase+ "\n" + Languages.getLang(player).getMessage(MessageUtils.Global.BOOSTER_TEMP_DESC).replace("{boost}", String.valueOf(tBoost * 100)))));
    }
}