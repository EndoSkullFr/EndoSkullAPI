package fr.endoskull.api;

import java.io.IOException;
import java.net.Socket;

public class EndoSkullAPI {

    public static boolean isOnline(String ip, int port) {
        try (Socket s = new Socket(ip, port)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }
}
