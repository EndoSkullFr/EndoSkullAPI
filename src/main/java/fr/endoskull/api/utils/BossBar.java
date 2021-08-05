package fr.endoskull.api.utils;

import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BossBar {
    public Player getPlayer() {
        return player;
    }

    private Player player;
    private String message;
    private EntityWither wither;


    public BossBar(Player player, String message) {
        this.player = player;
        this.message = message;
        update(message);
    }

    public void update(String message) {
        this.message = message;
        Vector direction = player.getLocation().getDirection();
        Location loc = player.getLocation().add(direction.multiply(20));
        removeWither();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        wither = new EntityWither(world);
        wither.setLocation(loc.getX(), player.getLocation().getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        wither.setCustomName(message);
        wither.setInvisible(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void update(String message, float health) {
        this.message = message;
        Vector direction = player.getLocation().getDirection();
        Location loc = player.getLocation().add(direction.multiply(20));
        removeWither();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        wither = new EntityWither(world);
        wither.setLocation(loc.getX(), player.getLocation().getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        wither.setCustomName(message);
        wither.setInvisible(true);
        wither.setHealth(wither.getMaxHealth() * health);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void removeWither() {
        if (wither != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public String getMessage() {
        return message;
    }
}
