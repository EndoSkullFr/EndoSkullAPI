package fr.endoskull.api.commons.paf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Party {

    private UUID leader;
    private List<UUID> members;
    private HashMap<UUID, Long> requests;

    public Party(UUID leader, List<UUID> members, HashMap<UUID, Long> requests) {
        this.leader = leader;
        this.members = members;
        this.requests = requests;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public HashMap<UUID, Long> getRequests() {
        return requests;
    }

    public List<UUID> getPlayers() {
        List<UUID> players = new ArrayList<>(members);
        players.add(leader);
        return players;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }
}
