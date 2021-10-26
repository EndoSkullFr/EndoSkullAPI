package fr.endoskull.api.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
    }
}
