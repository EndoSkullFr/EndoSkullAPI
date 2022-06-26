package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText(Languages.getLang(sender).getMessage(MessageUtils.Global.DISCORD)));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Languages.getLang(sender).getMessage(MessageUtils.Global.CLICK_HOVER)).create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://discord.endoskull.net"));
            player.spigot().sendMessage(msg);
        }
        if (label.equalsIgnoreCase("vote")) {
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText(Languages.getLang(sender).getMessage(MessageUtils.Global.VOTE)));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Languages.getLang(sender).getMessage(MessageUtils.Global.CLICK_HOVER)).create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://endoskull.net/vote"));
            player.spigot().sendMessage(msg);
        }
        return false;
    }
}