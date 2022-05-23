package fr.endoskull.api.spigot.papi;
/**
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.account.ClassementAccount;
import fr.endoskull.api.spigot.classement.ClassementTask;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
public class EndoSkullPlaceholder extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return "BebeDlaStreat";
    }

    @Override
    public String getIdentifier(){
        return "endoskull";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(identifier.equals("level")){
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            return account.getStringLevel();
        }
        if(identifier.equals("money") || identifier.equals("solde")){
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            return account.getStringSolde();
        }
        if(identifier.equals("boost") || identifier.equals("booster")){
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            //return (int) (account.getBoost().getRealBooster() * 100 - 100) + "%";
        }

        if(identifier.equals("classement")) {
            return String.valueOf(1 + ClassementTask.getClassement().indexOf(ClassementTask.getClassement().stream().filter(account -> account.getUuid().equals(player.getUniqueId())).findAny().orElse(new ClassementAccount())));
        }
        if(identifier.startsWith("classement_")) {
            String[] args = identifier.split("_");
            int index = Integer.parseInt(args[1]);
            if (ClassementTask.getClassement().size() <= index) {
                return "§c✖";Re
            }
            ClassementAccount account = ClassementTask.getClassement().get(index);
            if (args[2].equalsIgnoreCase("uuid")) return account.getUuid().toString();
            if (args[2].equalsIgnoreCase("name")) return account.getName();
            if (args[2].equalsIgnoreCase("level")) return account.getStringLevel();
        }
        if(identifier.startsWith("rank")) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            String prefix = user.getCachedData().getMetaData().getPrefix();
            if (prefix.length() < 5) {
                return prefix.substring(0, 2);
            } else {
                return prefix.substring(3, 5);
            }
        }
        if(identifier.startsWith("statistic_")) {
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            String[] args = identifier.split("_");
            //return String.valueOf(account.getStatistic(args[1]));
        }
        if(identifier.startsWith("progression")) {
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            int progression = (int) ((account.getXp() / account.xpToLevelSup()) * 10);
            String result = "§b" + account.getLevel() + " §7[§b§m";
            for (int i = 0; i < progression; i++) {
                result += "-";
            }
            result += "§f§m";
            for (int i = 0; i < 10 - progression; i++) {
                result += "-";
            }
            result += "§7] §b" + (account.getLevel() + 1);
            return result;
        }
        if (identifier.equalsIgnoreCase("nick")) {
            Player oPlayer = Bukkit.getPlayer(player.getUniqueId());
            if (oPlayer != null) {
                return oPlayer.getCustomName() == null ? oPlayer.getName() : oPlayer.getCustomName();
            } else {
                return player.getName();
            }
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}*/