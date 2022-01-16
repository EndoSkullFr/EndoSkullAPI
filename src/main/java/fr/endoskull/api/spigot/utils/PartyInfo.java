package fr.endoskull.api.spigot.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PartyInfo {

    private Player player;
    private static List<PartyInfo> instances = new ArrayList<>();
    private Consumer<? super Integer> action;

    public PartyInfo(Player player) {
        this.player = player;
        instances.add(this);
    }

    public static List<PartyInfo> getInstances() {
        return instances;
    }

    public void getPartySize(Consumer<? super Integer> action) {
        this.action = action;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("GetPartySize");
            out.writeUTF(player.getUniqueId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(Main.getInstance(), "PartiesChannel", b.toByteArray());
    }

    public Consumer<? super Integer> getAction() {
        return action;
    }

    public Player getPlayer() {
        return player;
    }
}
