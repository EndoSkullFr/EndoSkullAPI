package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerCommand implements CommandExecutor {
    private Main main;
    public ServerCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("lobby")) {
            player.sendMessage("§aTéléportation au lobby...");
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF("Hub");
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (label.equalsIgnoreCase("pvpkit")) {
            player.sendMessage("§aTéléportation vers le PvpKit...");
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF("PvpKit");
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
