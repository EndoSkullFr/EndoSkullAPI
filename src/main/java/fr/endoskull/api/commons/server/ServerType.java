package fr.endoskull.api.commons.server;

import java.util.Arrays;

public enum ServerType {
    LOBBY("Lobby", true, 24),
    PVPKIT("PvpKit", false, 24),
    BEDWARSSOLO("BedwarsSolo", false, 32),
    UNKNOW("none", false, 16);

    private String serverName;
    private boolean lowIsBest;
    private int semiFull;

    ServerType(String serverName, boolean lowIsBest, int semiFull) {
        this.serverName = serverName;
        this.lowIsBest = lowIsBest;
        this.semiFull = semiFull;
    }

    public String getServerName() {
        return serverName;
    }

    public static ServerType getByName(String name) {
        return Arrays.stream(values()).filter(serverType -> serverType.toString().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean isLowIsBest() {
        return lowIsBest;
    }

    public int getSemiFull() {
        return semiFull;
    }
}
