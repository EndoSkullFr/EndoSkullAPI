package fr.endoskull.api.data.yaml;

import fr.endoskull.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class BoxLocation {
    private static File file = new File(Main.getInstance().getDataFolder(), "boxloc.yml");
    private static YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public static void setLocation(String box, Location loc) {
        configuration.set(box, loc);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocation(String box) {
        if (configuration.get(box) == null) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        return (Location) configuration.get(box);
    }
}
