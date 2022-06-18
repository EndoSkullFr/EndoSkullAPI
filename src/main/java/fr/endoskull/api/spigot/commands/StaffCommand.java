package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.commons.lang.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.spigot.utils.VanishUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (VanishUtils.isVanished(player)) {
            VanishUtils.unvanish(player);
            player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.UNVANISH));
        } else {
            VanishUtils.vanish(player, true);
            player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.VANISH));
        }
        return false;
    }
}
