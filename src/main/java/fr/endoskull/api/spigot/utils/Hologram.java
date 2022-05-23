package fr.endoskull.api.spigot.utils;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Public Hologram utility by ParaPhoenix aka Phoenix1123.
 * Original credit goes to Janhektor on the Spigot forums.
 * https://www.spigotmc.org/members/janhektor.14134/
 * <p>
 * Feel free to use this however you see fit, and I hope that it is
 * of use. ^-^
 * <p>
 * To stay up to date with the latest version, you can find it on my Gist here:
 * https://gist.github.com/Phoenix1123/4a264e530368f96a435df7b0e3ae65fa
 * <p>
 * A brief explanation on how this class works for those who may not know or are wondering,
 * it uses reflection to access CB/NMS classes, which use an invisible armor stand to create floating names
 * (known as Holograms). These armor stands cannot be interacted through, so please do take note of this,
 * it may be a future feature however it is not an aim for this utility as of current.
 * <p>
 * If you do wish, you can remove this message however the update link would be useful!
 * <p>
 * Thank you! o/
 */
public class Hologram {

    private static String version;
    private static Class<?> craftWorld, entityClass, nmsWorld, armorStand, entityLiving, spawnPacket, removePacket;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        try {
            craftWorld = Class.forName("org.bukkit.craftbukkit." + version + "CraftWorld");
            entityClass = Class.forName("net.minecraft.server." + version + "Entity");
            nmsWorld = Class.forName("net.minecraft.server." + version + "World");
            armorStand = Class.forName("net.minecraft.server." + version + "EntityArmorStand");
            entityLiving = Class.forName("net.minecraft.server." + version + "EntityLiving");
            spawnPacket = Class.forName("net.minecraft.server." + version + "PacketPlayOutSpawnEntityLiving");
            removePacket = Class.forName("net.minecraft.server." + version + "PacketPlayOutEntityDestroy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Location location;
    private List<String> lines = new ArrayList<String>();
    private List<Integer> ids = new ArrayList<Integer>();
    private List<ArmorStand> entities = new ArrayList<>();
    private double offset = 0.23D;

    public Hologram(Location location, String... text) {
        this.location = location;
        if (!location.getChunk().isLoaded()) location.getChunk().load();
        addLine(text);
    }

    public Hologram(String... text) {
        this(null, text);
    }

    public Hologram(Location location) {
        this(location, null);
    }

    public Hologram() {
        this(null, null);
    }

    /**
     * Returns the CB/NMS version string. For example v1_10_R1
     *
     * @return - The CB/NMS version.
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Add a line or multiple, character colours will be converted.
     *
     * @param text - The text to add.
     */
    public void addLine(String... text) {
        lines.addAll(Arrays.asList(text));
        update();
    }

    /**
     * Returns a List of the lines the hologram is displaying.
     *
     * @return
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Sets the hologram lines, removing any currently lines previously added.
     *
     * @param text
     */
    public void setLines(String... text) {
        lines = Arrays.asList(text);
        update();
    }

    /**
     * Return the current stored location of the Hologram.
     *
     * @return - The current hologram location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location of the hologram.
     *
     * @param location - The location to set.
     */
    public void setLocation(Location location) {
        this.location = location;
        update();
    }

    public void teleport(Location loc) {
        update();
    }

    /**
     * Spawn the hologram for everyone to see.
     */
    public void spawn() {
        Location current = location.clone().add(0, (offset * lines.size()) - 1.97D, 0).add(0, offset, 0);

        for (String str : lines)
            spawnHologram(ChatColor.translateAlternateColorCodes('&', str), current.subtract(0, offset, 0));
    }

    /**
     * Spawns a hologram with -text- at -location-
     */
    private void spawnHologram(String text, Location location) {
        try {
            ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);

            configureHologram(as, text, location);

            entities.add(as);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Delete the hologram from the world.
     */
    public void remove() {
        for (ArmorStand as : entities)
            as.remove();
    }

    /**
     * Updates the currently existant hologram.
     */
    public void update() {
        try {
            if (!entities.isEmpty()) { // spawned as an actual entity, moving is ezpz.

                for (int i = 0; i < entities.size(); i++) {
                    ArmorStand as = entities.get(i);

                    if (i > lines.size() - 1) // 1 'hologram' per line
                        as.remove();
                }

                Location current = location.clone().add(0, (offset * lines.size()) - 1.97D, 0);

                for (int i = 0; i < lines.size(); i++) {
                    String text = ChatColor.translateAlternateColorCodes('&', lines.get(i));

                    if (i >= entities.size()) {
                        spawnHologram(text, current);
                    } else {
                        configureHologram(entities.get(i), text, current);
                    }

                    current.subtract(0, offset, 0);
                }

            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void configureHologram(ArmorStand as, String text, Location location) {
        as.setCustomName(text);
        as.setCustomNameVisible(true);
        as.setGravity(false);
        as.teleport(location);
        as.setVisible(false);
        as.setMarker(true);
    }


}