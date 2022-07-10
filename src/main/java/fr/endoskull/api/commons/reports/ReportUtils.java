package fr.endoskull.api.commons.reports;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.miscellaneous.DiscordWebhook;
import fr.endoskull.api.data.redis.JedisAccess;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportUtils {
    public static void createReport(Report report, Player player) {
        saveReport(report);
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("ReportSend");
        dataOutput.writeUTF(report.getReporterName());
        dataOutput.writeUTF(report.getTargetName());
        dataOutput.writeUTF(report.getReason());
        dataOutput.writeUTF(report.getUuid().toString());
        player.sendPluginMessage(Main.getInstance(), Main.CHANNEL, dataOutput.toByteArray());
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/983354998584913951/ZBIGUC1aKZr80zDaKmP5L1TNw4ohYKu9pG0fIvUW7PkkAPCety4KGomDh95P7P9QZal5");
        webhook.setAvatarUrl("https://www.ville-gravelines.fr/sites/default/files/field/image/report_1.png");
        webhook.setUsername("Report");
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setColor(Color.RED);
        embedObject.setThumbnail("http://cravatar.eu/avatar/" + report.getTargetName() + "/64.png");
        embedObject.setDescription("Cible: `" + report.getTargetName() + "`\\nSignalé par: `" + report.getReporterName() + "`\\nRaison: `" + report.getReason() + "`");
        webhook.addEmbed(embedObject);
        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void resolveReport(Report report, Player player, Report.Result result) {
        report.setResolved(true);
        report.setResult(result);
        saveReport(report);
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("ReportResolved");
        dataOutput.writeUTF(report.getUuid().toString());
        player.sendPluginMessage(Main.getInstance(), Main.CHANNEL, dataOutput.toByteArray());
    }

    public static void saveReport(Report report) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.set("report:" + report.getUuid(), new Gson().toJson(report));
        } finally {
            j.close();
        }
    }

    public static void delReport(Report report) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.del("report:" + report.getUuid());
        } finally {
            j.close();
        }
    }

    public static List<Report> loadReports() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            List<Report> reports = new ArrayList<>();
            for (String key : j.keys("report:*")) {
                Report report = new Gson().fromJson(j.get(key), Report.class);
                if (report.getCreatedOn() < System.currentTimeMillis() - 604800000) {
                    delReport(report);
                } else {
                    reports.add(report);
                }
            }
            return reports;
        } finally {
            j.close();
        }
    }

    public static Report loadReport(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            if (j.exists("report:" + uuid)) {
                Report report = new Gson().fromJson(j.get("report:" + uuid), Report.class);
                return report;
            }
        } finally {
            j.close();
        }
        return null;
    }
}
