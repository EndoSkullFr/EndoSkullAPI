package fr.endoskull.api.spigot.utils;

import com.github.javafaker.Faker;
import com.mojang.authlib.properties.Property;
import fr.endoskull.api.commons.nick.NickData;
import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NickUtils {

    private static final Random r = new Random();

    public static void initNick(Player player) {
        String name = new Faker(Locale.FRANCE).superhero().name();
        Property property = getRandomSkin();
        if (property == null) {
            player.sendMessage("§cErreur");
            return;
        }
        String value = property.getValue();
        String signature = property.getSignature();
        NickData.nick(player.getUniqueId(), name, value, signature);
        nick(player, name, value, signature);
    }

    public static void nick(Player player, String name, String value, String signature) {
        player.setDisplayName(name);
        NickData.nick(player.getUniqueId(), name, value, signature);
    }

    public static void unnick(Player player) {
        resetSkin(player);
        NickData.unnick(player.getUniqueId());
    }

    private static Property setRandomSkin(Player player) {
        Property property = getRandomSkin();
        if (property == null) {
            return null;
        }
        SkinChangerAPI.change(player, new Property("textures", property.getValue(), property.getSignature()));
        return property;
    }

    private static void resetSkin(Player player) {
        SkinChangerAPI.change(player, SkinChangerAPI.getByUUID(player.getUniqueId().toString()));
    }

    private static Property getRandomSkin() {
        String url1 = "https://api.mineskin.org/get/list/" + r.nextInt(100);
        try {
            @SuppressWarnings("deprecation")
            String json1 = IOUtils.toString(new URL(url1));
            if(json1.isEmpty()) return null;
            JSONObject jsonObject1 = (JSONObject) JSONValue.parseWithException(json1);

            JSONArray skins = (JSONArray) jsonObject1.get("skins");
            Iterator<JSONObject> iterator = skins.iterator();
            List<String> skinList = new ArrayList<>();
            while (iterator.hasNext()) {
                JSONObject skinObject = iterator.next();
                skinList.add(skinObject.get("uuid").toString());
            }
            String skinUuid = skinList.get(r.nextInt(skinList.size()));
            String url2 = "https://api.mineskin.org/get/uuid/" + skinUuid;
            String json2 = IOUtils.toString(new URL(url2));
            if(json2.isEmpty()) return null;
            JSONObject jsonObject2 = (JSONObject) JSONValue.parseWithException(json2);
            JSONObject skinData = (JSONObject) jsonObject2.get("data");
            JSONObject texture = (JSONObject) skinData.get("texture");
            return new Property("textures", texture.get("value").toString(), texture.get("signature").toString());

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
