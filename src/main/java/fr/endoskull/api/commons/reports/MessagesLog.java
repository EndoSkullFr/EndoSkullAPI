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

    private final HashMap<Long, String> messages = new HashMap<>();

    public HashMap<Long, String> getMessages() {
        return messages;
    }

    public HashMap<Long, String> getLastMessages(int amount) {
        return getLastMessages(amount, System.currentTimeMillis());
    }

    public HashMap<Long, String> getLastMessages(int amount, long time) {
        List<Long> timeStamps = new ArrayList<>(messages.keySet());
        Collections.sort(timeStamps, Collections.reverseOrder());
        for (Long aLong : new ArrayList<>(timeStamps)) {
            if (aLong > time) timeStamps.remove(aLong);
        }
        HashMap<Long, String> result = new HashMap<>();
        int i = 0;
        for (Long timeStamp : timeStamps) {
            if (i >= amount) break;
            result.put(timeStamp, messages.get(timeStamp));
            i++;
        }
        return result;
    }
}
