package fr.endoskull.api.commons.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(UUID uuid) {
        super("The account (" + uuid.toString() + ") is not found");
    }
}
