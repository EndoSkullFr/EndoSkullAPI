package fr.endoskull.api.spigot.utils;

public enum ServerType {
    LOBBY("Lobby"),
    PVPKIT("PvpKit");

    private String serverName;

    ServerType(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }
}
