package fr.endoskull.api.spigot.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.reports.Report;
import org.bukkit.entity.Player;

public class SpigotReportUtils {

    public static void sendCreatedPluginMessage(Player player, Report report) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("ReportSend");
        dataOutput.writeUTF(report.getReporterName());
        dataOutput.writeUTF(report.getTargetName());
        dataOutput.writeUTF(report.getReason());
        dataOutput.writeUTF(report.getUuid().toString());
        player.sendPluginMessage(Main.getInstance(), Main.CHANNEL, dataOutput.toByteArray());
    }

    public static void sendResolvedPluginMessage(Player player, Report report) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("ReportResolved");
        dataOutput.writeUTF(report.getUuid().toString());
        player.sendPluginMessage(Main.getInstance(), Main.CHANNEL, dataOutput.toByteArray());
    }
}
