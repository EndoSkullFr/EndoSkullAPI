package fr.endoskull.api.commons.reports;

import fr.endoskull.api.data.sql.MySQL;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MessagesLog {

    public static CompletableFuture<MessagesLog> get(UUID uuid) {
        CompletableFuture<MessagesLog> completableFuture = new CompletableFuture<>();
        MySQL.getInstance().query("SELECT * FROM chatlog WHERE `uuid`='" + uuid + "'", rs -> {
            MessagesLog messagesLog = new MessagesLog();
            try {
                while (rs.next()) {
                    messagesLog.getMessages().put(rs.getLong("time"), rs.getString("message"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            completableFuture.complete(messagesLog);
        });
        return completableFuture;
    }

    public static void log(UUID uuid, String message) {
        log(uuid, System.currentTimeMillis(), message);
    }

    public static void log(UUID uuid, long time, String message) {
        MySQL.getInstance().update("INSERT INTO `chatlog`(`uuid`, `time`, `message`) VALUES ('" + uuid + "', " + time + ", '" + message.replace("'", "\\'") + "')");
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
        LinkedHashMap<Long, String> result = new LinkedHashMap<>();
        int i = 0;
        for (Long timeStamp : timeStamps) {
            if (i >= amount) break;
            result.put(timeStamp, messages.get(timeStamp));
            i++;
        }
        return reverseMap(result);
    }

    public static <T, Q> LinkedHashMap<T, Q> reverseMap(LinkedHashMap<T, Q> toReverse) {
        LinkedHashMap<T, Q> reversedMap = new LinkedHashMap<>();
        List<T> reverseOrderedKeys = new ArrayList<>(toReverse.keySet());
        Collections.reverse(reverseOrderedKeys);
        reverseOrderedKeys.forEach((key)->reversedMap.put(key,toReverse.get(key)));
        return reversedMap;
    }
}
