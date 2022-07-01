package fr.endoskull.api.spigot.utils;

import com.mojang.authlib.properties.Property;
import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class NickUtils {

    private static final Random r = new Random();

    public static void nick(Player player) {
        setRandomSkin(player);
    }

    public static void unnick(Player player) {
        resetSkin(player);
    }

    private static void setRandomSkin(Player player) {
        Property property = getRandomSkin();
        if (property == null) {
            player.sendMessage("§cErreur");
            return;
        }
        SkinChangerAPI.change(player, new Property("textures", property.getValue(), property.getSignature()));
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
