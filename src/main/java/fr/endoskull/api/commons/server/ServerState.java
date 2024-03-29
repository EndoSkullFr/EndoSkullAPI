package fr.endoskull.api.commons.server;

public enum ServerState {
    ONLINE("En ligne"),
    SEMI_FULL("Presque plein"),
    FULL("Plein");

    private String displayName;

    ServerState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
