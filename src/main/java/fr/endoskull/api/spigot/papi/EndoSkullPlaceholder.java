package fr.endoskull.api.spigot.papi;

import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.classement.ClassementTask;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the directory
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 * <br>
 * <br>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEnable()} by using
 * {@code new YourExpansionClass().register();}
 */
public class EndoSkullPlaceholder extends PlaceholderExpansion {

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "BebeDlaStreat";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "endoskull";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link OfflinePlayer OfflinePlayer}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return Possibly-null String of the requested identifier.
     */
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
            return (int) (account.getBoost().getRealBooster() * 100 - 100) + "%";
        }

        if(identifier.equals("classement")) {
            return String.valueOf(1 + ClassementTask.getClassement().indexOf(ClassementTask.getClassement().stream().filter(account -> account.getUuid().equals(player.getUniqueId())).findAny().orElse(new Account())));
        }
        if(identifier.startsWith("classement_")) {
            String[] args = identifier.split("_");
            int index = Integer.parseInt(args[1]);
            if (ClassementTask.getClassement().size() <= index) {
                return "§c✖";
            }
            Account account = ClassementTask.getClassement().get(index);
            if (args[2].equalsIgnoreCase("uuid")) return account.getUuid().toString();
            if (args[2].equalsIgnoreCase("name")) return account.getName();
            if (args[2].equalsIgnoreCase("level")) return account.getStringLevel();
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}