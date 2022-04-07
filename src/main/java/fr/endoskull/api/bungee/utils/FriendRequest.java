package fr.endoskull.api.bungee.utils;

import java.util.UUID;

public class FriendRequest {

    private UUID sender;
    private UUID receiver;
    private long expiry;

    public FriendRequest(UUID sender, UUID receiver, long expiry) {
        this.sender = sender;
        this.receiver = receiver;
        this.expiry = expiry;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public long getExpiry() {
        return expiry;
    }
}
