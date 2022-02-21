package fr.endoskull.api.bungee.listeners;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.EndoSkullAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ChatListener implements Listener {
    private List<UUID> sayEz = new ArrayList<>();
    private HashMap<UUID, String> lastMessages = new HashMap<>();
    private String[] badWords = {"fuck", "connard", "connard", "con", "fdp", "ta mère", "pute", "encule", "enculé", "batard", "batar", "negro", "nègre", "nigga", "merde", "shit", "pd", "tchoin", "salop", "salaud", "salau", "salo", "adopté", "suce", "bite", "couille", "couilles", "bites", "nique", "niqué", "niquer", "ntm", "ntr"};

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        addLog(player.getUniqueId(), player.getName() + " » " + e.getMessage());
        if (e.isCommand()) return;
        String message = e.getMessage();
        message = getUnFlood(message);
        if (isEz(message)) {
            if (!sayEz.contains(player.getUniqueId())) {
                sayEz.add(player.getUniqueId());
                player.sendMessage("§cIl semblerait que votre message contienne le mot \"ez\". Si vous êtes surpris une nouvelle fois à le faire vous serez mute");
            } else {
                BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "mute " + player.getName() + " 1h ez");
            }
            e.setCancelled(true);
            return;
        }
        if (containsBadword(message)) {
            BungeeMain.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeMain.getInstance().getProxy().getConsole(), "mute " + player.getName() + " 1h langage");
            e.setCancelled(true);
            return;
        }
        if (lastMessages.containsKey(player.getUniqueId())) {
            String lastMessage = lastMessages.get(player.getUniqueId());
            if (message.equalsIgnoreCase(lastMessage) || ((message.startsWith(lastMessage) || message.endsWith(lastMessage)) && difference(message.length(), lastMessage.length()) < 5)) {
                player.sendMessage("§cMerci de ne pas spam le même message");
                e.setCancelled(true);
                return;
            }
        }
        lastMessages.put(player.getUniqueId(), message);
        e.setMessage(message);
    }

    private boolean isEz(String message) {
        if (message.equalsIgnoreCase("ez")) return true;
        if (message.startsWith("e z")) return true;
        if (message.startsWith("ez")) return true;
        if (message.contains(" ")) {
            String[] words = message.split(" ");
            for (String word : words) {
                if (word.equalsIgnoreCase("ez")) return true;
                if (word.startsWith("ez")) return true;
            }
        }
        return false;
    }

    private boolean containsBadword(String message) {
        if (message.contains(" ")) {
            String[] words = message.split(" ");
            for (String word : words) {
                if (Arrays.asList(badWords).contains(word.toLowerCase())) return true;
            }
        } else {
            if (Arrays.asList(badWords).contains(message.toLowerCase())) return true;
        }
        return false;
    }

    private String getUnFlood(String message) {
        if (message.length() < 4) return message;
        String tempMessage = message;
        char oldLetter = message.charAt(0);
        int count = 1;
        for (int i = 1; i < tempMessage.length(); i++) {
            char letter = tempMessage.charAt(i);
            if (i == tempMessage.length() - 1 && oldLetter == letter) {
                count++;
                letter = ' ';
            }
            if (oldLetter == letter) {
                count++;
            } else {
                if (count >= 3) {
                    StringBuilder chars = new StringBuilder();
                    for (int j = 0; j < count; j++) {
                        chars.append(oldLetter);
                    }
                    message = message.replaceFirst(chars.toString(), String.valueOf(oldLetter));
                }
                count = 1;
            }
            oldLetter = letter;
        }
        return message;
    }

    private int difference(int a, int b) {
        return Math.abs(a - b);
    }

    public static void addLog(UUID uuid, String message){
        try {
            File file = new File("/root/logging/" + uuid + ".txt");
            if (!file.getParentFile().exists()) file.getParentFile().mkdir();
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
