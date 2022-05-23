package fr.endoskull.api.bungee.tasks;

import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.commons.paf.PartyUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PAFTask {
    private static final HashMap<UUID, List<UUID>> partyRequests = new HashMap<>();
    private static final HashMap<UUID, List<UUID>> friendRequests = new HashMap<>();

    public static void run() {
        for (UUID uuid : partyRequests.keySet()) {
            for (UUID receiverUUID : new ArrayList<>(partyRequests.get(uuid))) {
                if (!PartyUtils.hasRequest(uuid, receiverUUID)) {
                    partyRequests.get(uuid).remove(receiverUUID);
                    ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(uuid);
                    if (sender != null) {
                        sender.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cLa demande de partie à " + EndoSkullAPI.getColor(receiverUUID) + BungeePlayerInfos.getNameFromUuid(receiverUUID) + " §ca expiré\n" + EndoSkullAPI.LINE).toLegacyText());
                    }
                    ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(receiverUUID);
                    if (receiver != null) {
                        receiver.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cLa demande de partie de " + EndoSkullAPI.getColor(uuid) + BungeePlayerInfos.getNameFromUuid(uuid) + " §ca expiré\n" + EndoSkullAPI.LINE).toLegacyText());
                    }
                }
            }
        }
        for (UUID uuid : friendRequests.keySet()) {
            for (UUID receiverUUID : new ArrayList<>(friendRequests.get(uuid))) {
                System.out.println(receiverUUID + " " + uuid);
                if (!FriendUtils.hasRequestFrom(receiverUUID, uuid)) {
                    friendRequests.get(uuid).remove(receiverUUID);
                    ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(uuid);
                    if (sender != null) {
                        sender.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cLa demande d'ami à " + EndoSkullAPI.getColor(receiverUUID) + BungeePlayerInfos.getNameFromUuid(receiverUUID) + " §ca expiré\n" + EndoSkullAPI.LINE).toLegacyText());
                    }
                    ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(receiverUUID);
                    if (receiver != null) {
                        receiver.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cLa demande d'ami de " + EndoSkullAPI.getColor(uuid) + BungeePlayerInfos.getNameFromUuid(uuid) + " §ca expiré\n" + EndoSkullAPI.LINE).toLegacyText());
                    }
                }
            }
        }
    }

    public static HashMap<UUID, List<UUID>> getPartyRequests() {
        return partyRequests;
    }

    public static HashMap<UUID, List<UUID>> getFriendRequests() {
        return friendRequests;
    }
}
