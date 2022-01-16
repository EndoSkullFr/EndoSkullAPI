package fr.endoskull.api.spigot.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.EndoSkullMotd;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.inventories.motd.MotdInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotdCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return false;
        }
        Player player = (Player) sender;
        new MotdInventory().open(player);
        return false;
    }
}
