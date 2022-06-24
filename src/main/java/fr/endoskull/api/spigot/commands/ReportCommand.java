package fr.endoskull.api.spigot.commands;

import com.google.common.base.Joiner;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.reports.Report;
import fr.endoskull.api.commons.reports.ReportUtils;
import fr.endoskull.api.spigot.inventories.ReportGui;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Languages lang = Languages.getLang(sender);
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(lang.getMessage(MessageUtils.Global.REPORT_USAGE));
            return false;
        }
        String targetName = args[0];
        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage(lang.getMessage(MessageUtils.Global.REPORT_SELF));
            return false;
        }
        UUID targetUuid = SpigotPlayerInfos.getUuidFromName(targetName);
        if (targetUuid == null) {
            player.sendMessage(lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER));
            return false;
        }
        if (args.length > 1) {
            String reason = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
            Report report = new Report(player.getUniqueId(), player.getName(), targetUuid, targetName, reason, System.currentTimeMillis(), false, null);
            ReportUtils.createReport(report, player);
            player.sendMessage(lang.getMessage(MessageUtils.Global.REPORT_SEND));
        } else {
            new ReportGui(targetName, player).open();
        }
        return false;
    }
}
