package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.boost.BoosterManager;
import fr.endoskull.api.commons.boost.TempBooster;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.spigot.inventories.BoosterInventory;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import fr.endoskull.api.spigot.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BoosterCommand implements CommandExecutor {
    private Main main;
    public BoosterCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("booster.edit") || args.length == 0) {
                new BoosterInventory(player).open(player);
                return false;
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 2) {
                sender.sendMessage("/" + label + " add/set/remove {player} {number}");
                sender.sendMessage("/" + label + " addtemp {player} {number} {durée}");
                sender.sendMessage("/" + label + " remtemp {player} {id}");
                sender.sendMessage("/" + label + " get {player}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = SpigotPlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }
            if (!JedisManager.isLoad(targetUUID)) AccountProvider.loadAccount(targetUUID);
            Account account = new AccountProvider(targetUUID).getAccount();
            if (args[0].equals("get")) {
                BoosterManager booster = account.getBoost();
                booster.checkExpirate();
                sender.sendMessage("§7§m--------------------\n" +
                        "§eBooster: §6+" + booster.getBoost() * 100 + "%");
                sender.sendMessage("§eBooster(s) Temporaire(s):");
                if (booster.getBooster().getTempBoosts().isEmpty()) {
                    sender.sendMessage("§6Aucun");
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss dd/MM/yy");
                    for (TempBooster tempBoost : booster.getBooster().getTempBoosts()) {
                        sender.sendMessage("§6+" + tempBoost.getBoost() * 100 + "% §7jusqu'au §e" + format.format(new Date(tempBoost.getExpiry())));
                    }
                }
                sender.sendMessage("§7§m--------------------");
                return;
            }
            if (args.length < 3) {
                sender.sendMessage("/" + label + " add/set/remove {player} {number}");
                sender.sendMessage("/" + label + " addtemp {player} {number} {durée}");
                sender.sendMessage("/" + label + " remtemp {player} {id}");
                return;
            }
            if (args[0].equals("remtemp")) {
                int id = 0;
                try {
                    id = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                    return;
                }
                if (id < 0) {
                    sender.sendMessage("§4L'id doit être supérieur à 0 !");
                    return;
                }
                BoosterManager booster = account.getBoost();
                if (booster.getBooster().getTempBoosts().size() <= id) {
                    sender.sendMessage("§4Cette id n'existe pas !");
                    return;
                }
                booster.removeTempBoost(id);
                sender.sendMessage("§aBooster temporaire retiré");
                return;

            }

            double number = 0;
            try {
                number = Double.parseDouble(args[2]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                return;
            }
            if (args[0].equals("add")) {
                account.getBoost().addBoost(number);
                sender.sendMessage("§a" + number + " §ebooster ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("remove")) {
                account.getBoost().removeBoost(number);
                sender.sendMessage("§a" + number + " §eboster ont été retiré à §a" + args[1]);
            }
            if (args[0].equals("set")) {
                account.getBoost().setBoost(number);
                sender.sendMessage("§a" + number + " §ebooster ont été défini à §a" + args[1]);
            }
            if (args[0].equals("addtemp")) {
                if (args.length < 4) {
                    sender.sendMessage("/" + label + " addtemp {player} {number} {durée}");
                    return;
                }
                String temp = args[3];
                if (temp.length() < 2) {
                    sender.sendMessage("§aDurée : nombre{s/m/h/d/y}");
                    return;
                }
                String numStr = temp.substring(0, temp.length() - 1);
                char durCar = temp.charAt(temp.length() - 1);
                int duration = 0;
                try {
                    duration = Integer.parseInt(numStr);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le quatrième argument n'est pas un nombre !");
                    return;
                }
                TimeUnit unit = TimeUnit.getByIdentifier(durCar);
                if (unit == null) {
                    sender.sendMessage("§4Cette unité de temps n'existe pas !");
                    return;
                }
                account.getBoost().addTempBoost(new TempBooster(number, System.currentTimeMillis() + (unit.toMillis() * duration)));
                sender.sendMessage("§a" + number + " §ebooster ont été défini à §a" + args[1] + " §epour une durée de §a" + args[3]);
            }
        });
        return false;
    }
}