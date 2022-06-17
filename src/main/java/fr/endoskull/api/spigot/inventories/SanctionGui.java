package fr.endoskull.api.spigot.inventories;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.SanctionEnum;

public class SanctionGui extends CustomGui {

    public SanctionGui(String target) {
        super(3, "Sanction " + target);
        for (SanctionEnum sanction : SanctionEnum.values()) {
            setItem(sanction.getSlot(), new CustomItemStack(sanction.getItem()).setName(sanction.getName()).setLore("\n§7Type: §a" + (sanction.isBan() ? "ban" : "mute")), player -> {
                ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                dataOutput.writeBoolean(true);
                dataOutput.writeUTF(sanction.isBan() ? "ban" : "mute");
                dataOutput.writeUTF(target + " " + sanction.getTemplate());
                dataOutput.writeBoolean(player.isOp());
                player.sendPluginMessage(Main.getInstance(), Main.MESSAGE_CHANNEL, dataOutput.toByteArray());
            });
        }
    }
}
