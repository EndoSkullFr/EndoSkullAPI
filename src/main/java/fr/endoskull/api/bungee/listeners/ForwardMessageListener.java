package fr.endoskull.api.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import fr.endoskull.api.BungeeMain;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ForwardMessageListener implements Listener {
    private BungeeMain main;
    public ForwardMessageListener(BungeeMain main) {
        this.main = main;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase(main.CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String action = in.readUTF();
            if (action.equalsIgnoreCase("command")) {
                String command = in.readUTF();
                ProxiedPlayer player = (ProxiedPlayer) e.getReceiver();
                BungeeCord.getInstance().getPluginManager().dispatchCommand(player, command);
            }
        }
        if (e.getTag().equals("PartiesChannel")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String sub = in.readUTF();
            System.out.println(sub);

            if (sub.equals("GetPartySize")) {
                String uuid = in.readUTF();
                System.out.println(uuid);
                System.out.println(PAFPlayerManager.getInstance());
                System.out.println(PartyManager.getInstance());
                System.out.println(PAFPlayerManager.getInstance().getPlayer(UUID.fromString(uuid)));
                PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(UUID.fromString(uuid));
                ProxiedPlayer player = main.getProxy().getPlayer(UUID.fromString(uuid));
                if (player == null) return;
                PlayerParty party = PartyManager.getInstance().getParty((OnlinePAFPlayer) pafPlayer);
                int partySize = 1;
                if (party != null) {
                    if (party.isLeader(pafPlayer)) {
                        partySize = party.getAllPlayers().size();
                    }
                }
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                System.out.println(partySize);
                out.writeUTF("GetPartySize");
                out.writeUTF(uuid);
                out.writeInt(partySize);

                player.getServer().sendData("PartiesChannel", out.toByteArray());

            }
        }
    }
}
