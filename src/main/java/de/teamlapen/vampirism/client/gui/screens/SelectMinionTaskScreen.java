package de.teamlapen.vampirism.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.teamlapen.vampirism.REFERENCE;
import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.minion.IMinionTask;
import de.teamlapen.vampirism.client.core.ModKeys;
import de.teamlapen.lib.lib.client.gui.screens.radialmenu.RadialMenu;
import de.teamlapen.lib.lib.client.gui.screens.radialmenu.RadialMenuSlot;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.entity.minion.management.PlayerMinionController;
import de.teamlapen.vampirism.network.ServerboundSelectMinionTaskPacket;
import de.teamlapen.vampirism.network.ServerboundSimpleInputEvent;
import de.teamlapen.vampirism.util.RegUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class SelectMinionTaskScreen extends SwitchingRadialMenu<SelectMinionTaskScreen.Entry> {

    private SelectMinionTaskScreen(Collection<Entry> entries, KeyMapping keyMapping) {
        super(getRadialMenu(entries), keyMapping, SelectActionScreen::show);
    }

    public static void show() {
        show(ModKeys.MINION);
    }

    public static void show(KeyMapping mapping) {
        FactionPlayerHandler.getOpt(Minecraft.getInstance().player).filter(p -> p.getLordLevel() > 0).ifPresent(p -> {
            Collection<Entry> tasks = getTasks(p);
            Minecraft.getInstance().setScreen(new SelectMinionTaskScreen(tasks, mapping));
        });
    }

    private static List<Entry> getTasks(IFactionPlayerHandler playerHandler) {
        List<Entry> entries = PlayerMinionController.getAvailableTasks(playerHandler).stream().map(Entry::new).collect(Collectors.toList());
        entries.add(new Entry(Component.translatable("text.vampirism.minion.call_single"), new ResourceLocation(REFERENCE.MODID, "textures/minion_tasks/recall_single.png"), (SelectMinionTaskScreen::callSingle)));
        entries.add(new Entry(Component.translatable("text.vampirism.minion.call_all"), new ResourceLocation(REFERENCE.MODID, "textures/minion_tasks/recall.png"), (SelectMinionTaskScreen::callAll)));
        entries.add(new Entry(Component.translatable("text.vampirism.minion.respawn"), new ResourceLocation(REFERENCE.MODID, "textures/minion_tasks/respawn.png"), (SelectMinionTaskScreen::callRespawn)));
        return entries;
    }

    private static RadialMenu<Entry> getRadialMenu(Collection<Entry> playerHandler) {
        List<RadialMenuSlot<Entry>> parts = playerHandler.stream().map(entry -> new RadialMenuSlot<>(entry.text.getString(), entry)).toList();
        return new RadialMenu<>(i -> parts.get(i).primarySlotIcon().onSelected.run(), parts, SelectMinionTaskScreen::drawActionPart, 0);
    }

    private static void drawActionPart(Entry t, PoseStack stack, int posX, int posY, int size, boolean transparent) {
        ResourceLocation texture = t.getIconLoc();
        RenderSystem.setShaderTexture(0, texture);
        blit(stack, posX, posY, 0, 0, 0, 16, 16, 16, 16);
    }



    private static void callAll() {
        VampirismMod.dispatcher.sendToServer(new ServerboundSelectMinionTaskPacket(-1, ServerboundSelectMinionTaskPacket.RECALL));

    }

    private static void callRespawn() {
        VampirismMod.dispatcher.sendToServer(new ServerboundSelectMinionTaskPacket(-1, ServerboundSelectMinionTaskPacket.RESPAWN));

    }

    private static void callSingle() {
        VampirismMod.dispatcher.sendToServer(new ServerboundSimpleInputEvent(ServerboundSimpleInputEvent.Type.SHOW_MINION_CALL_SELECTION));
    }

    private static void sendTask(IMinionTask<?, ?> task) {
        VampirismMod.dispatcher.sendToServer(new ServerboundSelectMinionTaskPacket(-1, RegUtil.id(task)));
    }

    public static class Entry {

        private final Component text;
        private final ResourceLocation loc;
        private final Runnable onSelected;

        public Entry(@NotNull IMinionTask<?, ?> task) {
            this(task.getName(), new ResourceLocation(RegUtil.id(task).getNamespace(), "textures/minion_tasks/" + RegUtil.id(task).getPath() + ".png"), (() -> sendTask(task)));
        }

        public Entry(Component text, ResourceLocation icon, Runnable onSelected) {
            this.text = text;
            this.loc = icon;
            this.onSelected = onSelected;
        }

        public ResourceLocation getIconLoc() {
            return loc;
        }

        public Component getText() {
            return text;
        }
    }

}