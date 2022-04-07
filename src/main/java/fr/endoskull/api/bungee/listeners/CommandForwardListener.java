package fr.endoskull.api.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import java.util.Map;
import java.util.Objects;

import fr.endoskull.api.BungeeMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

public class CommandForwardListener implements Listener {

    @EventHandler
    public void onServerConnected(PluginMessageEvent messageEvent) {
        String channel = messageEvent.getTag();
        if (messageEvent.isCancelled() || !Objects.equals(BungeeMain.MESSAGE_CHANNEL, channel)) {
            return;
        }

        // do not forward it further to the client
        messageEvent.setCancelled(true);

        //check if the message is sent from the server
        if (Server.class.isAssignableFrom(messageEvent.getSender().getClass())) {
            parseMessage((CommandSender) messageEvent.getReceiver(),
                    ByteStreams.newDataInput(messageEvent.getData()));
        }
    }

    private void parseMessage(CommandSender sender, ByteArrayDataInput dataInput) {
        final boolean isPlayer = dataInput.readBoolean();
        final String command = dataInput.readUTF();
        final String arguments = dataInput.readUTF();
        final CommandSender invoker = (isPlayer) ? sender : BungeeMain.getInstance().getProxy().getConsole();

        invokeCommand(invoker, dataInput.readBoolean(), command, arguments);
    }

    private void invokeCommand(CommandSender invoker, boolean isOp, String command, String arguments) {
        PluginManager pluginManager = BungeeMain.getInstance().getProxy().getPluginManager();

        if (isOp) {
            pluginManager.getCommands()
                    .stream()
                    .filter(entry -> entry.getKey().equals(command.toLowerCase()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(pluginCmd -> {
                        pluginCmd.execute(invoker, arguments.split(" "));
                        return pluginCmd;
                    }).orElseGet(() -> {
                        sendErrorMessage(invoker, "Unknown command : " + command);
                        return null;
                    });
        } else {
            pluginManager.dispatchCommand(invoker, command + " " + arguments);
        }
    }

    /**
     * Print an error message
     *
     * @param sender  Sender that execute the current command
     * @param message Message to send to command sender
     */
    private void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(
                new ComponentBuilder(String.format("[%s] %s", BungeeMain.getInstance().getDescription().getName(), message))
                        .color(ChatColor.RED).append(message).create());
    }
}
