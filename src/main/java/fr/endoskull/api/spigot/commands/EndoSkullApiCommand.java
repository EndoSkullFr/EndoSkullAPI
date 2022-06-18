package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndoSkullApiCommand implements CommandExecutor {
    private Main main;
    public EndoSkullApiCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.LESS_PERMISSION));
            return false;
        }
        if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("§cUsage: /" + label + " reload");
            return false;
        }
        main.reload();
        sender.sendMessage("§aLa config a bien été relaod");
        return false;
    }
}
