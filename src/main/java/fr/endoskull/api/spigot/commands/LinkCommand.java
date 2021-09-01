package fr.endoskull.api.spigot.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (label.equalsIgnoreCase("discord")) {
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§7§m--------------------------------------------------\n" +
                    ChatColor.YELLOW + "Lien du discord: " + ChatColor.GREEN + "discord.endoskull.fr\n" +
                    "§7§m--------------------------------------------------"));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dCliquez pour ouvir").create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://discord.endoskull.fr"));
            player.spigot().sendMessage(msg);
        }
        if (label.equalsIgnoreCase("vote")) {
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§7§m--------------------------------------------------\n" +
                    ChatColor.YELLOW + "Lien de la page de vote: " + ChatColor.GREEN + "endoskull.fr/vote\n" +
                    "§7§m--------------------------------------------------"));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dCliquez pour ouvir").create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://endoskull.fr/vote"));
            player.spigot().sendMessage(msg);
        }
        return false;
    }
}
