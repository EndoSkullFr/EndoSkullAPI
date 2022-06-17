package fr.endoskull.api.spigot.utils;

public interface MessageUtils {
    String getPath();

    enum Global implements MessageUtils {
        ABOUT;

        private final static String path = "global";

        @Override
        public String getPath() {
            return path + "." + this.toString().toLowerCase().replace("_", "-");
        }
    }
}
