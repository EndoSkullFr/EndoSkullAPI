package fr.endoskull.api.spigot.commands;

import com.google.common.base.Joiner;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.reports.Report;
import fr.endoskull.api.commons.reports.ReportUtils;
import fr.endoskull.api.spigot.inventories.ReportGui;
import fr.endoskull.api.spigot.inventories.ReportsGui;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class ReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Languages lang = Languages.getLang(sender);
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (args.length > 0) {
            String uuid = args[0];
            for (Report report : ReportUtils.loadReports()) {
                if (report.getUuid().toString().equalsIgnoreCase(uuid) && !report.isResolved()) {
                    new ReportsGui.SubReportGui(player, report).open();
                    return false;
                }
            }
        }
        new ReportsGui(player, 0).open();
        return false;
    }
}
