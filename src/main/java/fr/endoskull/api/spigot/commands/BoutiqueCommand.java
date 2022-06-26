package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.spigot.inventories.boutique.BoutiqueInventory;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
            sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("link")) {
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§7§m--------------------------------------------------\n" +
                    ChatColor.YELLOW + ChatColor.BOLD + "EndoSkull " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "endoskull.net/shop\n" +
                    "§7§m--------------------------------------------------"));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Languages.getLang(sender).getMessage(MessageUtils.Global.CLICK_HOVER)).create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://endoskull.net/shop"));
            player.spigot().sendMessage(msg);
            return false;
        }
        new BoutiqueInventory(player).open();
        player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
        return false;
    }
}
