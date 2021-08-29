package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.data.sql.Keys;
import fr.endoskull.api.spigot.utils.KeyEnum;
import fr.endoskull.api.spigot.utils.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class KeyCommand implements CommandExecutor {
    private Main main;
    public KeyCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 4) {
                sender.sendMessage("/key add/set/remove {player} {clé} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = PlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }

            KeyEnum key;
            try {
                key = KeyEnum.valueOf(args[2].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§4Cette clé n'existe pas !");
                return;
            }
            int number = 0;
            try {
                number = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le quatrième argument n'est pas un nombre !");
                return;
            }
            if (args[0].equals("add")) {
                //Keys.addKey(targetUUID, key.toString().toLowerCase(), number);
                sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("remove")) {
                //Keys.removeKey(targetUUID, key.toString().toLowerCase(), number);
                sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
            }
            if (args[0].equals("set")) {
                //Keys.setKey(targetUUID, key.toString().toLowerCase(), number);
                sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
            }
        });
        return false;
    }
}
