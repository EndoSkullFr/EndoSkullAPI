package fr.endoskull.api.commons.server;

import java.util.Arrays;

public enum ServerType {
    LOBBY("Lobby", true, 24, false, true),
    PVPKIT("PvpKit", false, 24, false, true),
    BEDWARSSOLO("BedwarsSolo", false, 32, true, false),
    UNKNOW("none", false, 16, false, false);

    private String serverName;
    private boolean lowIsBest;
    private int semiFull;
    private boolean multiArena;
    private boolean registerServer;

    ServerType(String serverName, boolean lowIsBest, int semiFull, boolean multiArena, boolean registerServer) {
        this.serverName = serverName;
        this.lowIsBest = lowIsBest;
        this.semiFull = semiFull;
        this.multiArena = multiArena;
        this.registerServer = registerServer;
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

    public boolean isMultiArena() {
        return multiArena;
    }

    public boolean isRegisterServer() {
        return registerServer;
    }
}
