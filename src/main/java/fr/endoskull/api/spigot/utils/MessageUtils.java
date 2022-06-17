package fr.endoskull.api.spigot.utils;

public interface MessageUtils {
    String getPath();

    enum Global implements MessageUtils {
        ABOUT, CONSOLE, CLICK_HOVER, LESS_PERMISSION, UNKNOWN_SERVER, ANY_SERVER, UNKNOWN_PLAYER,
        LEVEL, VOTE, DISCORD, COINS, VANISH, UNVANISH, SHOP_RANKS, SHOP_RANKS_DESC, SHOP_KEYS, SHOP_KEYS_DESC, SHOP_BOUGHT,
        SHOP_FEATURES, SHOP_VIP, SHOP_VIP_DESC, SHOP_HERO, SHOP_HERO_DESC, BACK, GUI_SHOP, GUI_RANKS, GUI_KEYS,
        GUI_BOOSTER, HOW_TO_BOOSTER, HOW_TO_BOOSTER_DESC, BOOSTER_LINE, BOOSTER_PERM, BOOSTER_TEMP, BOOSTER_NO_PERM, BOOSTER_NO_TEMP,
        BOOSTER_PERM_DESC, BOOSTER_TEMP_DESC;

        private final static String path = "global";

        @Override
        public String getPath() {
            return path + "." + this.toString().toLowerCase().replace("_", "-");
        }
    }
}
