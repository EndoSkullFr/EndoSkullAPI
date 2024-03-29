package fr.endoskull.api.spigot.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import fr.endoskull.api.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AntiTabComplete {
    public AntiTabComplete() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(Main.getInstance(), new PacketType[] { PacketType.Play.Client.TAB_COMPLETE })
        {
            @SuppressWarnings("rawtypes")
            public void onPacketReceiving(PacketEvent event) {
                if ((event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                        && (!event.getPlayer().hasPermission("endoskull.tab.bypass"))
                        && (((String)event.getPacket().getStrings().read(0)).startsWith("/"))
                        && !((event.getPacket().getStrings().read(0)).contains(" "))) {

                    event.setCancelled(true);

                    List<?> list = new ArrayList();
                    List<?> extra = new ArrayList();

                    String[] tabList = new String[list.size() + extra.size()];

                    for (int index = 0; index < list.size(); index++) {
                        tabList[index] = ((String)list.get(index));
                    }

                    for (int index = 0; index < extra.size(); index++) {
                        tabList[(index + list.size())] = ('/' + (String)extra.get(index));
                    }
                    PacketContainer tabComplete = manager.createPacket(PacketType.Play.Server.TAB_COMPLETE);
                    tabComplete.getStringArrays().write(0, tabList);

                    try {
                        manager.sendServerPacket(event.getPlayer(), tabComplete);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
