package fr.endoskull.api.bungee.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bebedlastreat.cache.CacheAPI;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.DefaultFontInfo;
import fr.endoskull.api.commons.EndoSkullMotd;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.data.redis.RedisAccess;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.redisson.api.RBucket;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ProxyPing implements Listener {

    private final static int CENTER_PX = 100;

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        ServerPing serverPing = e.getResponse();

        EndoSkullMotd motd = null;
        try {
            motd = new ObjectMapper().readValue(CacheAPI.get("motd"), EndoSkullMotd.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        List<String> firstLines = new ArrayList<>();
        for (String s : motd.getFirstLines().keySet()) {
            for (int i = 0; i < motd.getFirstLines().get(s); i++) {
                firstLines.add(s);
            }
        }
        List<String> secondLines = new ArrayList<>();
        for (String s : motd.getSecondLines().keySet()) {
            for (int i = 0; i < motd.getSecondLines().get(s); i++) {
                secondLines.add(s);
            }
        }
        String firstLine = firstLines.get(new Random().nextInt(firstLines.size()));
        String secondLine = secondLines.get(new Random().nextInt(secondLines.size()));
        serverPing.setDescription(centerText(firstLine) + "\n" + centerText(secondLine));
        String[] hoverText = {
                "§7§m------------------------",
                "",
                "§6Discord §7⋙ §9discord.endoskull.fr",
                "§6Site Web §7⋙ §dwww.endoskull.fr",
                "§6Boutique §7⋙ §astore.endoskull.fr",
                "",
                "§7§m------------------------",
        };
        int l = hoverText.length;
        ServerPing.PlayerInfo[] profiles = new ServerPing.PlayerInfo[l];
        for (int i = 0; i < l; i++)
            profiles[i] = new ServerPing.PlayerInfo(hoverText[i], new UUID(0L, 0L));
        serverPing.setPlayers(new ServerPing.Players(500, ProxyServer.getInstance().getOnlineCount(), profiles));

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

        e.setResponse(serverPing);
    }

    @EventHandler
    public void onLogin(PreLoginEvent e) {
        int version = e.getConnection().getVersion();
        if (version < 47/* || version > 754*/) {
            e.setCancelled(true);
            e.setCancelReason("§cEndoSkull NetWork\n\n§7Le serveur est accessible uniquement à partir de la §c1.8\n\n§ehttps://discord.endoskull.fr");
        }
    }

    public String centerText(String message) {

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }
}
