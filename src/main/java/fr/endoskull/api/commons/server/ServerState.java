package fr.endoskull.api.commons.server;

public enum ServerState {
    ONLINE("§a✔"),
    SEMI_FULL("§e✔"),
    FULL("§c✘");

    private String displayName;

    ServerState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
