package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.JedisAccess;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EndoSkullCommand implements CommandExecutor, TabCompleter {
    private Main main;

    public EndoSkullCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            if (!sender.hasPermission("endoskull.stop." + main.getServerType().toString())) {
                sender.sendMessage("§cPermission manquante: " + "endoskull.stop." + main.getServerType().toString());
                return false;
            }
            main.getServer().shutdown();
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("deploy")) {
            if (args.length < 4) {
                sender.sendMessage("§c/" + label + " deploy <projet> <fileName> <template>");
                return false;
            }
            File projectFolder = new File("/root/Docker/data/jenkins/workspace/EndoSkull/" + args[1]);
            if (!projectFolder.exists()) {
                sender.sendMessage("§cCe projet n'existe pas");
                return false;
            }
            File buildFolder = new File("/root/Docker/data/jenkins/workspace/EndoSkull/" + args[1] + "/target");
            if (!buildFolder.exists()) {
                sender.sendMessage("§cCe projet n'a pas été encore build");
                return false;
            }
            File jarFile = new File("/root/Docker/data/jenkins/workspace/EndoSkull/" + args[1] + "/target/" + args[2]);
            if (!jarFile.exists()) {
                sender.sendMessage("§cCe projet ne contient pas ce fichier là dans ses builds");
                return false;
            }
            File serverFolder = new File("/root/Cloudnet/local/templates/" + args[3]);
            if (!serverFolder.exists()) {
                sender.sendMessage("§cCette template n'existe pas");
                return false;
            }
            if (!sender.hasPermission("endoskull.deploy.project." + args[1])) {
                sender.sendMessage("§cPermission manquante: " + "endoskull.deploy.project." + args[1]);
                return false;
            }
            if (!sender.hasPermission("endoskull.deploy.folder." + args[3])) {
                sender.sendMessage("§cPermission manquante: " + "endoskull.deploy.folder." + args[3]);
                return false;
            }
            try {
                if (serverFolder.getName().equalsIgnoreCase("Global")) {
                    FileUtils.copyFile(jarFile, new File(serverFolder + "/server/plugins/" + args[2]));
                } else if (serverFolder.getName().equalsIgnoreCase("Proxy")) {
                    FileUtils.copyFile(jarFile, new File("Global/proxy/plugins/" + args[2]));
                } else {
                    FileUtils.copyFile(jarFile, new File(serverFolder + "/default/plugins/" + args[2]));
                }
                sender.sendMessage("§aLe fichier a bien été copié");
            } catch (IOException e) {
                sender.sendMessage("§cErreur lors de la copie du fichier");
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String s : Arrays.asList("deploy")) {
                if (s.startsWith(args[0])) result.add(s);
            }
            return result;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("deploy")) {
            File folder = new File("/root/Docker/data/jenkins/workspace/EndoSkull/");
            List<String> subFolders = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isDirectory() && !file.getName().endsWith("@tmp") && file.getName().startsWith(args[1])) subFolders.add(file.getName());
            }
            return subFolders;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("deploy")) {
            File folder = new File("/root/Docker/data/jenkins/workspace/EndoSkull/" + args[1] + "/target");
            if (!folder.exists()) return null;
            List<String> jarFiles = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".jar") && !file.getName().contains("original-") && file.getName().startsWith(args[2])) jarFiles.add(file.getName());
            }
            return jarFiles;
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("deploy")) {
            File folder = new File("/root/Cloudnet/local/templates/");
            if (!folder.exists()) return null;
            List<String> folders = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith(args[3])) folders.add(file.getName());
            }
            return folders;
        }
        return null;
    }
}
