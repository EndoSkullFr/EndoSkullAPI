package fr.endoskull.api.spigot.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum ForwardPermissions {
    FORWARD("commandforward.bukkit.command.forward"),
    FORWARD_CONSOLE("commandforward.bukkit.command.forward.console"),
    FORWARD_OTHER("commandforward.bukkit.command.forward.other");

    private String permission;
    public static String ERROR_MESSAGE = "You are not allowed to execute this command";

    private ForwardPermissions(final String perm) {
        this.permission = perm;
    }

    /**
     * Define if the permission is set
     *
     * @param player Player on which check the permissions
     * @return Return a boolean to define if the permission is set
     */
    public Boolean isSetOn(final CommandSender player) {
        return player != null && (!(player instanceof Player) || ((Player)player).hasPermission(this.permission));
    }
}