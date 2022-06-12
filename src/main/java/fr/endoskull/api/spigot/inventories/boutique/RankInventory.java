package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;

public class RankInventory extends CustomGui {
    public RankInventory() {
        super(5, "§c§lEndoSkull §8» §a§lGrades");
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(2).setName("§r"));
        }
        setItem(20, new CustomItemStack(Material.GOLD_CHESTPLATE).setName("§eGrade VIP"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(21, new CustomItemStack(Material.NAME_TAG).setName("§7Liste de ses fonctionnalités:").setLore("\n" +
                "§7◇ Un booster de coins +50%\n" +
                "§7◇ Le préfix §eVIP §7dans la tablist\n" +
                "§7◇ Votre pseudo en §ejaune §7dans le chat\n" +
                "§7◇ Accès à la particule §lAiles de feux\n" +
                "§7◇ Accès au /musics pour joueur de la musique au lobby\n" +
                "§7◇ Accès aux kits de Tier 2 sur le PvpKit" +
                "\n\n" +
                "§7⇨ Prix: §e5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(23, new CustomItemStack(Material.DIAMOND_CHESTPLATE).setName("§bGrade Hero"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(24, new CustomItemStack(Material.NAME_TAG).setName("§7Liste de ses fonctionnalités:").setLore("\n" +
                "§7◇ Avantages du grade précédent\n" +
                "§7◇ Un booster de coins +100%\n" +
                "§7◇ Le préfix §bHero §7dans la tablist\n" +
                "§7◇ Votre pseudo en §bcyan §7dans le chat\n" +
                "§7◇ Accès au /tag pour créer un tag personnaliser\n" +
                "§7◇ Accès à la particule §lTatanes ténébreuses\n" +
                "§7◇ Accès aux kits de Tier 3 sur le PvpKit" +
                "\n\n" +
                "§7⇨ Prix: §b10€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(40, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory().open(player);
        });
    }
}
