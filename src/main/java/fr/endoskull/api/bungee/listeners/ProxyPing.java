package fr.endoskull.api.bungee.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.endoskull.api.bungee.utils.MaintenanceUtils;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ProxyPing implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        ServerPing serverPing = e.getResponse();

        /*String[] hoverText = {
                "§7§m------------------------",
                "",
                "§6Discord §7⋙ §9discord.endoskull.net",
                "§6Site Web §7⋙ §dwww.endoskull.net",
                "§6Boutique §7⋙ §ashop.endoskull.net",
                "",
                "§7§m------------------------",
        };
        int l = hoverText.length;
        ServerPing.PlayerInfo[] profiles = new ServerPing.PlayerInfo[l];
        for (int i = 0; i < l; i++)
            profiles[i] = new ServerPing.PlayerInfo(hoverText[i], new UUID(0L, 0L));
        serverPing.setPlayers(new ServerPing.Players(500, ProxyServer.getInstance().getOnlineCount(), profiles));
        */
        try {
            serverPing.setFavicon(Favicon.create(ImageIO.read(new File("server-icon.png"))));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        int version = e.getConnection().getVersion();
        ServerPing.Protocol protocol = serverPing.getVersion();
        if (version < 47/* || version > 754*/) {
            protocol.setName("§c[1.8+]");
            protocol.setProtocol(999);
        }
        serverPing.setVersion(protocol);

        if (MaintenanceUtils.isInMaintenance("Global")) {
            serverPing.setDescription("§c§lEndoSkull » Maintenance en cours" + "\n" + "§7Plus d'infos §8» §adiscord.endoskull.net");
        } else {

        }

        e.setResponse(serverPing);
    }

    @EventHandler
    public void onLogin(PreLoginEvent e) {
        int version = e.getConnection().getVersion();
        if (version < 47/* || version > 754*/) {
            e.setCancelled(true);
            e.setCancelReason("§cEndoSkull NetWork\n\n§c[1.8+]\n\n§ehttps://discord.endoskull.net");
        }
    }

    @EventHandler
    public void onLogin(LoginEvent e){

        if(MaintenanceUtils.isInMaintenance("Global") && !MaintenanceUtils.isWhitelisted(e.getConnection().getName())){
            e.setCancelled(true);
            e.setCancelReason(new ComponentBuilder("§c§lEndoSkull Maintenance\n\n§7Plus d'infos §8» §adiscord.endoskull.net").create());
        }
    }
}