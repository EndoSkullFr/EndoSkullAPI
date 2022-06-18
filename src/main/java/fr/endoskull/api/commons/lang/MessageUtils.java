package fr.endoskull.api.commons.lang;

public interface MessageUtils {
    String getPath();

    enum Global implements MessageUtils {
        ABOUT, CONSOLE, CLICK_HOVER, LESS_PERMISSION, UNKNOWN_SERVER, ANY_SERVER, UNKNOWN_PLAYER,
        LEVEL, VOTE, DISCORD, COINS, VANISH, UNVANISH, SHOP_RANKS, SHOP_RANKS_DESC, SHOP_KEYS, SHOP_KEYS_DESC, SHOP_BOUGHT,
        SHOP_FEATURES, SHOP_VIP, SHOP_VIP_DESC, SHOP_HERO, SHOP_HERO_DESC, BACK, GUI_SHOP, GUI_RANKS, GUI_KEYS,
        GUI_BOOSTER, HOW_TO_BOOSTER, HOW_TO_BOOSTER_DESC, BOOSTER_LINE, BOOSTER_PERM, BOOSTER_TEMP, BOOSTER_NO_PERM, BOOSTER_NO_TEMP,
        BOOSTER_PERM_DESC, BOOSTER_TEMP_DESC, ULTIME_1, ULTIME_5, ULTIME_10, ULTIME_1_DESC, ULTIME_5_DESC, ULTIME_10_DESC,
        GUI_FREQUESTS, ANY_FREQUESTS, PREVIOUS_PAGE, NEXT_PAGE, ACCEPT, DENY, GUI_SERVERS, STATE, PLAYERS,
        CLICK_FOR, ENABLE, DISABLE, FRIENDS, CLICK_COPY, API_GENERATED, API_HELP, API_ALREADY;

        private final static String path = "global";

        @Override
        public String getPath() {
            return path + "." + this.toString().toLowerCase().replace("_", "-");
        }

        public enum FriendSettings implements MessageUtils {
            FRIEND_REQUEST, FRIEND_REQUEST_ENABLE, FRIEND_REQUEST_DISABLE,
            PARTY_REQUEST, PARTY_REQUEST_ENABLE, PARTY_REQUEST_DISABLE,
            PRIVATE_MESSAGE, PRIVATE_MESSAGE_ENABLE, PRIVATE_MESSAGE_DISABLE,
            FRIEND_NOTIFICATION, FRIEND_NOTIFICATION_ENABLE, FRIEND_NOTIFICATION_DISABLE;

            private final static String path = "global.friend-settings";
            @Override
            public String getPath() {
                return path + "." + this.toString().toLowerCase().replace("_", "-");
            }
        }
    }
}
