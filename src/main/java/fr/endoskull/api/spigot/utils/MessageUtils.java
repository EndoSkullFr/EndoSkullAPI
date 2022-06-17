package fr.endoskull.api.spigot.utils;

public interface MessageUtils {
    String getPath();

    enum Global implements MessageUtils {
        ABOUT, CONSOLE, CLICK_HOVER, LESS_PERMISSION, UNKNOWN_SERVER, ANY_SERVER, UNKNOWN_PLAYER,
        LEVEL, VOTE, DISCORD, COINS, VANISH, UNVANISH;

        private final static String path = "global";

        @Override
        public String getPath() {
            return path + "." + this.toString().toLowerCase().replace("_", "-");
        }
    }
}
