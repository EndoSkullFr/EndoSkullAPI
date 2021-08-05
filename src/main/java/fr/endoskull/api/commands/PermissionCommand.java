package fr.endoskull.api.commands;

import fr.endoskull.api.utils.RankApi;
import fr.endoskull.api.utils.RankUnit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PermissionCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("list")) {
            String rankString = args[1].toUpperCase();
            if (!RankUnit.exist(rankString)) {
                sender.sendMessage("§cLe rank §l" + rankString + "§r §cn'existe pas");
                return false;
            }
            sender.sendMessage("§a----- Liste des permissions de " + rankString + " -----");
            for (String perm : RankApi.getPermissions(RankUnit.getByName(rankString))) {
                sender.sendMessage("§e" + perm);
            }
            sender.sendMessage("§a----- Fin de la liste -----");
            return false;
        }
        if (args.length != 3) {
            sender.sendMessage("§c/" + label + " add [Rank] [Permission]\n" +
                    "/" + label + " remove [Rank] [Permission]");
            return false;
        }

        String rankString = args[1].toUpperCase();
        if (!RankUnit.exist(rankString)) {
            sender.sendMessage("§cLe rank §l" + rankString + "§r §cn'existe pas");
            return false;
        }
        RankUnit rank = RankUnit.valueOf(rankString);
        String permission = args[2];

        if(args[0].equalsIgnoreCase("add")) {
            if (RankApi.hasPerm(rank, permission)) {
                sender.sendMessage("§cCe rank possède déjà cette permission");
                return false;
            }
            RankApi.addPermission(rank, permission);
            sender.sendMessage("§eLa permission §a" + permission + " §ea été ajoutée au rank §a" + args[1]);
            return false;
        }
        if(args[0].equalsIgnoreCase("remove")) {
            if (!RankApi.hasPerm(rank, permission)) {
                sender.sendMessage("§cCe rank ne possède pas cette permission");
                return false;
            }
            RankApi.removePermission(rank, permission);
            sender.sendMessage("§eLa permission §a" + permission + " §ea été retirée au rank §a" + args[1]);
            return false;
        }
        sender.sendMessage("§cLe premier argument n'existe pas !");
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
        if(args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("list"))){
            for(RankUnit rankList : RankUnit.values()) {
                if(rankList.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    resultat.add(rankList.getName().toLowerCase());
            }
            return resultat;
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("remove")){
            if (RankUnit.exist(args[1])) {
                for (String perm : RankApi.getPermissions(RankUnit.getByName(args[1]))) {
                    if (perm.toLowerCase().startsWith(args[2].toLowerCase()))
                        resultat.add(perm.toLowerCase());
                }
            }
            return resultat;
        }
        return null;
    }
}
