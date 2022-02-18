package fr.endoskull.api.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) return false;
        String s = args[0];
        Sound sound;
        try {
            sound = Sound.valueOf(s);
        } catch (Exception ex) {
            return false;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player != null) {
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }
        return false;
    }
}
