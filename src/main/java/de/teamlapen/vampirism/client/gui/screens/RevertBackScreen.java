package de.teamlapen.vampirism.client.gui.screens;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.network.ServerboundSimpleInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RevertBackScreen extends ConfirmScreen {

    private static String getDescription() {
        String s = UtilLib.translate("gui.vampirism.revertback.desc");
        Level w = Minecraft.getInstance().level;
        if (w != null && w.getLevelData().isHardcore()) {
            s += " You won't die in hardcore mode.";
        }
        return s;
    }

    public RevertBackScreen() {
        super((context) -> {
            if (context) {
                VampirismMod.dispatcher.sendToServer(new ServerboundSimpleInputEvent(ServerboundSimpleInputEvent.Type.REVERT_BACK));
            }
            Minecraft.getInstance().setScreen(null);
        }, Component.translatable("gui.vampirism.revertback.head"), Component.literal(getDescription()));
    }
}
