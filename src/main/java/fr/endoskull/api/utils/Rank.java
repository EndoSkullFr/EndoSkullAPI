package fr.endoskull.api.utils;

import fr.endoskull.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Rank {
    public HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    private Scoreboard scoreboard;

    private final List<String> playersLoad = new ArrayList<>();

    private Main main;

    public Rank(Main main){
        this.main = main;
    }

    public final Scoreboard getScoreboard(){
        return scoreboard;
    }

    public void initScoreboard(){
        if(scoreboard != null) throw new UnsupportedOperationException("Le scoreboard est deja initialise.");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for(RankUnit rankUnit : RankUnit.values()){
            Team team = scoreboard.registerNewTeam(String.valueOf(rankUnit.getPower()));
            String prefix = rankUnit.getPrefix();
            if (prefix.length() > 15) {
                prefix = prefix.substring(0, 15);
            }
            team.setPrefix(prefix + " ");
            team.setSuffix("");
        }

    }

    public void loadPlayer(Player player){
        if(playersLoad.contains(main.getUuidsByName().get(player.getName()).toString())) return;

        if (perms.containsKey(player)) {
            deletePlayer(player);
        }
        PermissionAttachment attachment = player.addAttachment(main);
        perms.put(main.getUuidsByName().get(player.getName()), attachment);

        for (RankUnit rank : RankApi.getRankList(main.getUuidsByName().get(player.getName()))) {
            for (String perm : rank.getPermissions()) {
                attachment.setPermission(perm, true);
            }
        }

        playersLoad.add(main.getUuidsByName().get(player.getName()).toString());
        scoreboard.getTeam(String.valueOf(RankApi.getRank(main.getUuidsByName().get(player.getName())).getPower())).addEntry(player.getName());


    }
    public void changeRank(Player player) {
        scoreboard.getTeam(String.valueOf(RankApi.getRank(main.getUuidsByName().get(player.getName())).getPower())).removePlayer(player);
        reloadPlayer(player);
    }

    public void reloadPlayer(Player player) {
        deletePlayer(player);
        loadPlayer(player);
    }

    public void deletePlayer(Player player){
        if(!playersLoad.contains(main.getUuidsByName().get(player.getName()).toString())) return;
        playersLoad.remove(main.getUuidsByName().get(player.getName()).toString());
        if (perms.containsKey(player)) {
            PermissionAttachment attachment = perms.get(main.getUuidsByName().get(player.getName()));
            for (PermissionAttachmentInfo infos : player.getEffectivePermissions()) {
                attachment.unsetPermission(infos.getPermission());
            }
            player.removeAttachment(attachment);
            perms.remove(main.getUuidsByName().get(player.getName()), attachment);
        }

    }
}