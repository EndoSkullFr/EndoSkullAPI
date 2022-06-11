package fr.endoskull.api.bungee.utils;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.miscellaneous.DiscordWebhook;
import litebans.api.Database;
import litebans.api.Entry;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class LitebansHandler {

    private static String url = "https://discord.com/api/webhooks/983384191095935076/_QXakxCnOlWVdGowGFAgViZxvitZrjsw9mkighjyb16HLIwyOpUlK1nargNXKSfrp6B2";

    public static void sendWebhookAdd(Entry entry) {
        DiscordWebhook webhook = new DiscordWebhook(url);
        webhook.setUsername("Litebans");
        webhook.setAvatarUrl("https://i.ytimg.com/vi/Ux5cQbO_ybw/mqdefault.jpg");
        String type = entry.getType();
        String targetName = getLastKnowName(entry.getUuid());
        if (targetName == null) targetName = entry.getUuid();
        String executor = entry.getExecutorName();
        String duration = entry.getDurationString();
        String reason = entry.getReason();
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle("Nouvelle punition");
        embed.setColor(Color.RED);
        embed.setDescription("Type: `" + type + "`\\n" +
                "Modérateur: `" + executor + "`\\n" +
                "Cible: `" + targetName + "`\\n" +
                "Durée: `" + duration + "`\\n" +
                "Raison: `" + reason + "`");

        webhook.addEmbed(embed);
        try {
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendWebhookRemove(Entry entry) {
        DiscordWebhook webhook = new DiscordWebhook(url);
        webhook.setUsername("Litebans");
        webhook.setAvatarUrl("https://previews.123rf.com/images/tatyanagl/tatyanagl1408/tatyanagl140800054/30621347-des-enfants-heureux-avec-leurs-mains-isol%C3%A9-sur-blanc.jpg");
        String type = entry.getType();
        String targetName = getLastKnowName(entry.getUuid());
        if (targetName == null) targetName = entry.getUuid();
        String executor = entry.getExecutorName();
        String removeBy = entry.getRemovedByName();
        String duration = entry.getDurationString();
        String reason = entry.getReason();
        String removeReason = entry.getRemovalReason();
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle("Punition retirée");
        embed.setColor(Color.GREEN);
        embed.setDescription("Type: `" + type + "`\\n" +
                "Modérateur: `" + executor + "`\\n" +
                "Retirée par: `" + removeBy + "`\\n" +
                "Cible: `" + targetName + "`\\n" +
                "Durée Initiale: `" + duration + "`\\n" +
                "Raison: `" + reason + "`\\n" +
                "Retirée pour: `" + removeReason + "`");

        webhook.addEmbed(embed);
        try {
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLastKnowName(String uuid) {
        String query = "SELECT name FROM {history} WHERE uuid=? ORDER BY date DESC LIMIT 1";
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            st.setString(1, uuid);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String playerName = rs.getString("name");
                    return playerName;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
