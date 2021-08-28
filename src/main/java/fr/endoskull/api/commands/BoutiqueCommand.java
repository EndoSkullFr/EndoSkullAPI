package fr.endoskull.api.commands;

import fr.endoskull.api.inventories.boutique.BoutiqueInventory;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoutiqueCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return false;
        }
        Player player = (Player) sender;
        new BoutiqueInventory().open(player);
        player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
        return false;
    }
}
