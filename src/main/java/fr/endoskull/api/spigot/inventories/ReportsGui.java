package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.reports.Report;
import fr.endoskull.api.commons.reports.ReportUtils;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ReportsGui extends CustomGui {
    public ReportsGui(Player p, int page) {
        super(6, "EndoSkull Reports", p);
        Languages lang = Languages.getLang(p);
        p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
        for (int i = 45; i < 54; i++) {
            setItem(i, CustomItemStack.getPane(3).setName("§r"));
        }
        if (page > 0) {
            setItem(48, new CustomItemStack(Material.PAPER).setName(lang.getMessage(MessageUtils.Global.PREVIOUS_PAGE)), player -> {
                new ReportsGui(player, page - 1).open();
            });
        }
        if (page < 100) {
            setItem(50, new CustomItemStack(Material.PAPER).setName(lang.getMessage(MessageUtils.Global.NEXT_PAGE)), player -> {
                new ReportsGui(player, page + 1).open();
            });
        }
        int i = 0;
        int j = 0;
        for (Report report : ReportUtils.loadReports()) {
            while (j < 45*page) j++;
            if (i >= 45) break;
            if (report.isResolved()) continue;
            setItem(i, CustomItemStack.getPlayerSkull(report.getTargetName()).setLore("\n§7Par §e" + report.getReporterName() + "\n§7Pour §e" + report.getReason()), player -> {
                new SubReportGui(player, report).open();
            });
            i++;
        }
    }

    public static class SubReportGui extends CustomGui {

        public SubReportGui(Player p, Report report) {
            super(5, "Report " + report.getTargetName(), p);
            Languages lang = Languages.getLang(p);
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
            setItem(12, CustomItemStack.getPlayerSkull(report.getTargetName()).setName("§c" + report.getTargetName())
                    .setLore("\n§fHistorique du chat:\n...\n\n§e» Cliquez pour se téléporter à ce joueur"), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " stp " + report.getTargetName());
            });
            setItem(14, CustomItemStack.getPlayerSkull(report.getReporterName()).setName("§a" + report.getReporterName())
                    .setLore("\n§fHistorique du chat:\n...\n\n§e» Cliquez pour se téléporter à ce joueur"), player -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " stp " + report.getReporterName());
            });
            setItem(29, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 5).setName("§aDéfinir comme valide"), player -> {
                report.setResolved(true);
                report.setResult(Report.Result.VALID);
                ReportUtils.saveReport(report);
                player.closeInventory();
                player.sendMessage("§eEndoSkull §8» §7Pour avez défini ce report comme §aValide");
                player.performCommand("sanction " + report.getTargetName());
            });
            setItem(31, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 4).setName("§eDéfinir comme incertain"), player -> {
                report.setResolved(true);
                report.setResult(Report.Result.UNCERTAIN);
                ReportUtils.saveReport(report);
                player.closeInventory();
                player.sendMessage("§eEndoSkull §8» §7Pour avez défini ce report comme §eIncertain");
            });
            setItem(33, new CustomItemStack(Material.STAINED_CLAY, 1, (byte) 14).setName("§eDéfinir comme invalide"), player -> {
                report.setResolved(true);
                report.setResult(Report.Result.FALSE);
                ReportUtils.saveReport(report);
                player.closeInventory();
                player.sendMessage("§eEndoSkull §8» §7Pour avez défini ce report comme §aInvalide");
            });
            setItem(44, new CustomItemStack(Material.ARROW).setName(lang.getMessage(MessageUtils.Global.BACK)));
        }
    }
}
