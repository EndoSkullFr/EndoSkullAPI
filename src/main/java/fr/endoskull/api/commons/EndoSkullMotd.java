package fr.endoskull.api.commons;

import java.util.HashMap;

public class EndoSkullMotd {
    private HashMap<String, Integer> firstLines;
    private HashMap<String, Integer> secondLines;

    public EndoSkullMotd(HashMap<String, Integer> firstLines, HashMap<String, Integer> secondLines) {
        this.firstLines = firstLines;
        this.secondLines = secondLines;
    }

    public EndoSkullMotd() {}

    public HashMap<String, Integer> getFirstLines() {
        return firstLines;
    }

    public void setFirstLines(HashMap<String, Integer> firstLines) {
        this.firstLines = firstLines;
    }

    public HashMap<String, Integer> getSecondLines() {
        return secondLines;
    }

    public void setSecondLines(HashMap<String, Integer> secondLines) {
        this.secondLines = secondLines;
    }

    public static EndoSkullMotd getDefaultMotd() {
        HashMap<String, Integer> firstLines = new HashMap<>();
        HashMap<String, Integer> secondLines = new HashMap<>();
        firstLines.put("                    §c§lEndoSkull §7- §c[1.8+]§r", 1);
        secondLines.put("                 §6PvpKit §7- §aBedwars Goulag", 1);
        //System.out.println(firstLines);
        //System.out.println(secondLines);
        return new EndoSkullMotd(firstLines, secondLines);
    }
}
