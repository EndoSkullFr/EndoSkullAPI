package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AboutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.ABOUT).replace("{server}", Main.getInstance().getServer().getName()));
        //sender.sendMessage(EndoSkullAPI.LINE + "\n§bEndoSkull §7est développé et géré par §b§o@BebeDlaStreat\n§7Serveur: §e" + Main.getInstance().getServer().getName() + "\n§7Proxy: §eWaterfall 1.18\n§7Nous n'avons pas que des plugins fait par nous mais nous essayons de faire nous même les features les plus importantes du serveur\n" + EndoSkullAPI.LINE);
        return false;
    }
}
