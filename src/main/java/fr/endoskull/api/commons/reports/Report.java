package fr.endoskull.api.commons.reports;

import java.util.UUID;

public class Report {
    private UUID reporterUUID;
    private String reporterName;
    private UUID targetUUID;
    private String targetName;
    private String reason;
    private UUID uuid;

    public Report() {};

    public Report(UUID reporterUUID, String reporterName, UUID targetUUID, String targetName, String reason) {
        this.reporterUUID = reporterUUID;
        this.reporterName = reporterName;
        this.targetUUID = targetUUID;
        this.targetName = targetName;
        this.reason = reason;
        this.uuid = UUID.randomUUID();
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getReason() {
        return reason;
    }

    public UUID getUuid() {
        return uuid;
    }
}
