package fr.endoskull.api.spigot.utils;

public enum TimeUnit {
    YEAR(60 * 60 * 24 * 365, 'y'),
    DAY(60 * 60 * 24, 'd'),
    HOUR(60 * 60, 'h'),
    MINUTE(60, 'm'),
    SECOND(1, 's');

    private long duration;
    private char identifier;

    TimeUnit(long duration, char identifier) {
        this.duration = duration;
        this.identifier = identifier;
    }

    public static TimeUnit getByIdentifier(char identifier) {
        for (TimeUnit value : values()) {
            if (value.getIdentifier() == identifier) {
                return value;
            }
        }
        return null;
    }

    public long getDuration() {
        return duration;
    }

    public char getIdentifier() {
        return identifier;
    }

    public long toMillis() {
        return 1000 * duration;
    }
}
