package fr.endoskull.api.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.spigot.utils.PartyInfo;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PartiesChannelListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        System.out.println(channel);
        if (channel.equals("PartiesChannel")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String sub = in.readUTF();
            System.out.println(sub);
            if (sub.equals("GetPartySize")) {
                String uuid = in.readUTF();
                System.out.println(uuid);
                if (!uuid.equals(player.getUniqueId().toString())) return;
                int partySize = in.readInt();
                System.out.println(partySize);
                for (PartyInfo partyInfo : PartyInfo.getInstances()) {
                    System.out.println(partyInfo.getPlayer().getName());
                    if (partyInfo.getPlayer().equals(player)) {
                        System.out.println(partyInfo.getPlayer().getUniqueId());
                        partyInfo.getAction().accept(partySize);
                        PartyInfo.getInstances().remove(partyInfo);
                    }
                }
            }
        }
    }
}
