package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.boost.BoosterManager;
import fr.endoskull.api.commons.boost.TempBooster;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.spigot.inventories.BoosterInventory;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import fr.endoskull.api.spigot.utils.TimeUnit;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StreamCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Languages lang = Languages.getLang(sender);
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        if (user.data().contains(Node.builder("group.stream-mode").build(), NodeEqualityPredicate.EXACT).asBoolean()) {
            user.data().remove(Node.builder("group.stream-mode").build());
            player.sendMessage("§eEndoSkull §8» §aMode streamer retiré");
        } else {
            user.data().add(Node.builder("group.stream-mode").build());
            player.sendMessage("§eEndoSkull §8» §aMode streamer ajouté");
        }

        return false;
    }
}