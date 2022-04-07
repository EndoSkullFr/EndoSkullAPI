package fr.endoskull.api.commons;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

import java.util.HashMap;
import java.util.UUID;

public class EndoSkullAPI {

    private static LuckPerms luckPerms = LuckPermsProvider.get();
    private static UserManager userManager = luckPerms.getUserManager();
    private static String defaultPrefix = "§7";
    private static HashMap<UUID, String> prefixMap = new HashMap<>();

    public static String LINE = "§7§m-----------------------------------------------------";

    public static void loadPrefix(UUID uuid) {
        User user = userManager.getUser(uuid);
        if (user == null) {
            return;
        }
        String prefix = user.getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            prefix = defaultPrefix;
        }
        prefix = prefix.replace("&", "§");
        prefixMap.put(uuid, prefix);
    }

    public static String getPrefix(UUID uuid) {
        String prefix = prefixMap.get(uuid);
        if (prefix == null) return defaultPrefix;
        return prefix;
    }

    public static String getColor(UUID uuid) {
        String prefix = getPrefix(uuid);
        if (prefix.length() > 5) {
            prefix = prefix.substring(3, 5);
        } else {
            prefix = prefix.substring(0, 2);
        }
        return prefix;
    }
}
