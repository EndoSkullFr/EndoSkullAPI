package fr.endoskull.api.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bebedlastreat.cache.CacheAPI;

import java.util.HashMap;
import java.util.UUID;

public class MotdManager {
    private static HashMap<UUID, Integer> waitingLines = new HashMap<>();

    public static void setMotd(EndoSkullMotd motd) {
        try {
            CacheAPI.set("motd", new ObjectMapper().writeValueAsString(motd));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static EndoSkullMotd getMotd() {
        if (CacheAPI.keyExist("motd")) {
            try {
                return new ObjectMapper().readValue(CacheAPI.get("motd"), EndoSkullMotd.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return EndoSkullMotd.getDefaultMotd();
    }

    public static HashMap<UUID, Integer> getWaitingLines() {
        return waitingLines;
    }
}
