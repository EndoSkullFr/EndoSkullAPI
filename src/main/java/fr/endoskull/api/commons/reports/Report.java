package fr.endoskull.api.commons.reports;

import fr.endoskull.api.commons.lang.MessageUtils;

import java.util.UUID;

public class Report {
    private UUID reporterUUID;
    private String reporterName;
    private UUID targetUUID;
    private String targetName;
    private String reason;
    private UUID uuid;
    private long createdOn;
    private boolean resolved;
    private Result result;

    public Report() {};

    public Report(UUID reporterUUID, String reporterName, UUID targetUUID, String targetName, String reason, long createdOn, boolean resolved, Result result) {
        this.reporterUUID = reporterUUID;
        this.reporterName = reporterName;
        this.targetUUID = targetUUID;
        this.targetName = targetName;
        this.reason = reason;
        this.createdOn = createdOn;
        this.resolved = resolved;
        this.result = result;
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

    public long getCreatedOn() {
        return createdOn;
    }

    public boolean isResolved() {
        return resolved;
    }

    public Result getResult() {
        return result;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static enum Result {
        VALID(MessageUtils.Global.VALID), UNCERTAIN(MessageUtils.Global.UNCERTAIN), FALSE(MessageUtils.Global.FALSE);

        private MessageUtils message;

        Result(MessageUtils message) {
            this.message = message;
        }

        public MessageUtils getMessage() {
            return message;
        }
    }
}
