package fr.endoskull.api.spigot.utils;

import fr.endoskull.api.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ServerConfig {

    private static File file = new File(Main.getInstance().getDataFolder(), "server.yml");
    private static YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public static int getSemiFull() {
        return configuration.getInt("semi-full");
    }

    public static int getFull() {
        return configuration.getInt("full");
    }
}
