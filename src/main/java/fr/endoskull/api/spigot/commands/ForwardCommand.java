package fr.endoskull.api.spigot.commands;

import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.Arrays;
import java.util.Optional;

import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.ForwardPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForwardCommand implements CommandExecutor {
    private Main main;
    public ForwardCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            sendErrorMessage(sender, "Wrong command, Missing arguments");
            return false;
        }

        String channelPlayer = args[0];

        Optional<? extends Player> optPlayer = Bukkit.getOnlinePlayers().stream().findAny();
        if (!optPlayer.isPresent()) {
            sendErrorMessage(sender, "No player is online to forward this command");
            return true;
        }

        Player messageSender = optPlayer.get();

        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        if ("Console".equalsIgnoreCase(channelPlayer)) {
            if (!ForwardPermissions.FORWARD_CONSOLE.isSetOn(sender)) {
                sendErrorMessage(sender, ForwardPermissions.ERROR_MESSAGE);
                return true;
            }

            dataOutput.writeBoolean(false);
        } else {
            if(sender instanceof Player && !sender.getName().equalsIgnoreCase(channelPlayer) && !ForwardPermissions.FORWARD_OTHER.isSetOn(sender)) {
                sendErrorMessage(sender, ForwardPermissions.ERROR_MESSAGE);
                return true;
            }

            if(main.getServer().getPlayer(channelPlayer) == null) {
                sendErrorMessage(sender, "Player '" + channelPlayer + "' not found");
                return true;
            }

            dataOutput.writeBoolean(true);
            messageSender = main.getServer().getPlayer(channelPlayer);
        }

        dataOutput.writeUTF(args[1]);
        dataOutput.writeUTF(Joiner.on(' ').join(Arrays.copyOfRange(args, 2, args.length)));
        dataOutput.writeBoolean(sender.isOp());
        messageSender.sendPluginMessage(main, main.MESSAGE_CHANNEL, dataOutput.toByteArray());

        return true;
    }

    /**
     * Print an error message
     *
     * @param sender  Sender that execute the current command
     * @param message Message to send to command sender
     */
    private void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(main.PLUGIN_NAME_PREFIX + ChatColor.RED + message);
    }
}
