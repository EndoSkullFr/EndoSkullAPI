package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeployCommand implements CommandExecutor, TabCompleter {
    private Main main;

    public DeployCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c/deploy <projet> <fileName>");
            return false;
        }
        if (!sender.hasPermission("endoskull.deploy." + args[0])) {
            sender.sendMessage("§cVous n'avez pas la permission de deploy ce projet");
            return false;
        }
        File projectFolder = new File("/var/lib/jenkins/workspace/EndoSkull/" + args[0]);
        if (!projectFolder.exists()) {
            sender.sendMessage("§cCe projet n'existe pas");
            return false;
        }
        File buildFolder = new File("/var/lib/jenkins/workspace/EndoSkull/" + args[0] + "/target");
        if (!buildFolder.exists()) {
            sender.sendMessage("§cCe projet n'a pas été encore build");
            return false;
        }
        File jarFile = new File("/var/lib/jenkins/workspace/EndoSkull/" + args[0] + "/target/" + args[1]);
        if (!jarFile.exists()) {
            sender.sendMessage("§cCe projet ne contient pas ce fichier là dans ses builds");
            return false;
        }
        try {
            FileUtils.copyFile(jarFile, new File(main.getDataFolder().getParentFile() + "/" + args[1]));
            sender.sendMessage("§aLe fichier a bien été copié, le serveur va redémarrer dans 5 seconde");
            Bukkit.getScheduler().runTaskLater(main, () -> {
                main.getServer().shutdown();
            }, 100);
        } catch (IOException e) {
            sender.sendMessage("§cErreur lors de la copie du fichier");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1) {
            File folder = new File("/var/lib/jenkins/workspace/EndoSkull/");
            List<String> subFolders = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) subFolders.add(file.getName());
            }
            return subFolders;
        }
        if (args.length == 2) {
            File folder = new File("/var/lib/jenkins/workspace/EndoSkull/" + args[0] + "/target");
            if (!folder.exists()) return null;
            List<String> jarFiles = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith(".jar") && !file.getName().contains("original-")) jarFiles.add(file.getName());
            }
            return jarFiles;
        }
        return null;
    }
}
