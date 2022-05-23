package fr.endoskull.api.commons.paf;

import com.google.gson.Gson;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.data.redis.JedisAccess;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PartyUtils {

    public static List<Party> getParties() {
        List<Party> parties = new ArrayList<>();
        Jedis j = null;
        try {
            j = JedisAccess.getPartyPool().getResource();
            for (String key : j.keys("party/*")) {
                parties.add(new Gson().fromJson(j.get(key), Party.class));
            }
            return parties;
        } finally {
            j.close();
        }
    }

    public static void saveParty(Party party) {
        Jedis j = null;
        try {
            j = JedisAccess.getPartyPool().getResource();
            j.set("party/" + party.getLeader(), new Gson().toJson(party));
        } finally {
            j.close();
        }
    }

    public static void removeParty(Party party) {
        Jedis j = null;
        try {
            j = JedisAccess.getPartyPool().getResource();
            j.del("party/" + party.getLeader());
            for (String key : j.keys("request/" + party.getLeader() + "/*")) {
                j.del(key);
            }
            PAFTask.getPartyRequests().remove(party.getLeader());
        } finally {
            j.close();
        }
    }

    public static boolean isInParty(UUID uuid) {
        for (Party party : getParties()) {
            if (party.getLeader().equals(uuid)) return true;
            if (party.getMembers().contains(uuid)) return true;
        }
        return false;
    }

    public static Party getParty(UUID uuid) {
        for (Party party : getParties()) {
            if (party.getLeader().equals(uuid)) return party;
            if (party.getMembers().contains(uuid)) return party;
        }
        return null;
    }

    public static void addRequest(UUID sender, UUID receiver) {
        Jedis j = null;
        try {
            j = JedisAccess.getPartyPool().getResource();
            j.set("request/" + sender + "/" + receiver, "yes");
            j.expire("request/" + sender + "/" + receiver, 30);
            if (PAFTask.getPartyRequests().containsKey(sender)) {
                List<UUID> uuids = PAFTask.getPartyRequests().get(sender);
                uuids.add(receiver);
                PAFTask.getPartyRequests().put(sender, uuids);
            } else {
                PAFTask.getPartyRequests().put(sender, new ArrayList<>(Arrays.asList(receiver)));
            }
        } finally {
            j.close();
        }
    }

    public static boolean hasRequest(UUID sender, UUID receiver) {
        Jedis j = null;
        try {
            j = JedisAccess.getPartyPool().getResource();
            return j.exists("request/" + sender + "/" + receiver);
        } finally {
            j.close();
        }
    }
}
