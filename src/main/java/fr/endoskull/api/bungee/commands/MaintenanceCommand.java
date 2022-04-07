package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.bungee.utils.MaintenanceUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MaintenanceCommand extends Command {
    public MaintenanceCommand() {
        super("maintenance", "endoskull.command.maintenance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("on")) {
            MaintenanceUtils.setMaintenance("Global", true);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("off")) {
            MaintenanceUtils.setMaintenance("Global", false);
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            MaintenanceUtils.whitelist(args[1]);
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            MaintenanceUtils.unwhitelist(args[1]);
        }
    }
}
