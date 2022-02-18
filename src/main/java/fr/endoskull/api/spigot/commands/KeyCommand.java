package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.spigot.utils.PlayerInfos;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
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
            Account account = new AccountProvider(targetUUID).getAccount();

            String key = args[2].toLowerCase();
            int number = 0;
            try {
                number = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le quatrième argument n'est pas un nombre !");
                return;
            }
            if (key.equalsIgnoreCase("ultime")) {
                if (args[0].equals("add")) {
                    account.setUltimeKey(account.getUltimeKey() + number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setUltimeKey(account.getUltimeKey() - number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setUltimeKey(number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            if (key.equalsIgnoreCase("vote")) {
                if (args[0].equals("add")) {
                    account.setVoteKey(account.getVoteKey() + number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setVoteKey(account.getVoteKey() - number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setVoteKey(number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            /*if (key.equalsIgnoreCase("coins")) {
                if (args[0].equals("add")) {
                    account.setCoinsKey(account.getCoinsKey() + number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setCoinsKey(account.getCoinsKey() - number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setCoinsKey(number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            if (key.equalsIgnoreCase("kit")) {
                if (args[0].equals("add")) {
                    account.setKitKey(account.getKitKey() + number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setKitKey(account.getKitKey() - number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setKitKey(number).sendToRedis();
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }*/
        });
        return false;
    }
}
