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

public class ReportUtils {
    public static void createReport(Report report, Player player) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.sadd("reports", new Gson().toJson(report));
        } finally {
            j.close();
        }
        //send plugin message -> check duel plugin
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("ReportSend");
        dataOutput.writeUTF(report.getReporterName());
        dataOutput.writeUTF(report.getTargetName());
        dataOutput.writeUTF(report.getReason());
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
}
