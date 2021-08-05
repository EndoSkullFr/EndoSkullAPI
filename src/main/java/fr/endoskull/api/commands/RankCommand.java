package fr.endoskull.api.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.utils.PlayerInfos;
import fr.endoskull.api.utils.RankApi;
import fr.endoskull.api.utils.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankCommand implements CommandExecutor, TabCompleter {
    private Main main;
    public RankCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            if (args.length >= 2) {
                Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                    String targetName = args[1];
                    if (PlayerInfos.getUuidFromName(targetName) == null) {
                        sender.sendMessage("§4Ce joueur n'existe pas !");
                        return;
                    }
                    UUID targetUUID = PlayerInfos.getUuidFromName(targetName);
                    sender.sendMessage("§a----- Ranks de " + targetName + " -----");
                    for (RankUnit rank : RankApi.getRankList(targetUUID)) {
                        sender.sendMessage("§f" + rank.getName() + " -> " + rank.getPrefix() + " §e(" + rank.getPower() + ")");
                    }
                    sender.sendMessage("§a----- Fin de la liste -----");
                });
            } else {
                sender.sendMessage("§a----- Liste des ranks -----");
                for (RankUnit rank : RankUnit.values()) {
                    sender.sendMessage("§f" + rank.getName() + " -> " + rank.getPrefix() + " §e(" + rank.getPower() + ")");
                }
                sender.sendMessage("§a----- Fin de la liste -----");
            }
            return false;
        }
        if (args.length != 3) {
            sender.sendMessage("§c/" + label + " add [Player] [Rank]\n" +
                    "/" + label + " remove [Player] [Rank]");
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            String targetName = args[1];
            if (PlayerInfos.getUuidFromName(targetName) == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }
            UUID targetUUID = PlayerInfos.getUuidFromName(targetName);

            for (RankUnit rankList : RankUnit.values()) {
                if (rankList.getName().equalsIgnoreCase(args[2])) {
                    if(args[0].equalsIgnoreCase("add")) {
                        RankApi.addRank(targetUUID, rankList);
                        sender.sendMessage("§eLe " + label + " §a" + args[2] + " §ea été ajouté à §a" + args[1]);
                        return;
                    }
                    if(args[0].equalsIgnoreCase("remove")) {
                        RankApi.removeRank(targetUUID, rankList);
                        sender.sendMessage("§eLe " + label + " §a" + args[2] + " §ea été retiré à §a" + args[1]);
                        return;
                    }
                    sender.sendMessage("§cLe premier argument n'existe pas !");
                    return;
                }
            }
            sender.sendMessage("§cCe " + label + " n'existe pas !");
        });

        return false;
    }


    List<String> arguments = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(arguments.isEmpty()) {
            arguments.add("add");
            arguments.add("remove");
            arguments.add("list");
        }
        List<String> resultat = new ArrayList<String>();

        if(args.length == 1) {
            for(String str : arguments) {
                if(str.toLowerCase().startsWith(args[0].toLowerCase())) {
                    resultat.add(str);
                }
            }
            return resultat;
        }
        if(args.length == 3 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))){
            for(RankUnit rankList : RankUnit.values()) {
                if(rankList.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    resultat.add(rankList.getName().toLowerCase());
            }
            return resultat;
        }
        return null;
    }
}
