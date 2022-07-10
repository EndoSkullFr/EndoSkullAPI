package fr.endoskull.api.commons.reports;

import java.util.*;

public class MessagesLog {
    private static final HashMap<UUID, MessagesLog> messagesLog = new HashMap<>();

    public static MessagesLog get(UUID uuid) {
        if (!messagesLog.containsKey(uuid)) {
            messagesLog.put(uuid, new MessagesLog());
        }
        return messagesLog.get(uuid);
    }

    private final LinkedHashMap<Long, String> messages = new LinkedHashMap<>();

    public LinkedHashMap<Long, String> getMessages() {
        return messages;
    }

    public LinkedHashMap<Long, String> getLastMessages(int amount) {
        return getLastMessages(amount, System.currentTimeMillis());
    }

    public LinkedHashMap<Long, String> getLastMessages(int amount, long time) {
        List<Long> timeStamps = new ArrayList<>(messages.keySet());
        Collections.sort(timeStamps, Collections.reverseOrder());
        for (Long aLong : new ArrayList<>(timeStamps)) {
            if (aLong > time) timeStamps.remove(aLong);
        }
        LinkedHashMap<Long, String> resultReversed = new LinkedHashMap<>();
        int i = 0;
        for (Long timeStamp : timeStamps) {
            if (i >= amount) break;
            resultReversed.put(timeStamp, messages.get(timeStamp));
            i++;
        }
        LinkedHashMap<Long, String> result = new LinkedHashMap<>();
        List<Long> longs = new ArrayList<>(resultReversed.keySet());
        Collections.reverse(longs);
        for (Long aLong : longs) {
            result.put(aLong, resultReversed.get(aLong));
        }
        return result;
    }
}
