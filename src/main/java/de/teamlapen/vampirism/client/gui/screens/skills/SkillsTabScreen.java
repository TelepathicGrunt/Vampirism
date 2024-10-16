package de.teamlapen.vampirism.client.gui.screens.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import de.teamlapen.vampirism.REFERENCE;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.skills.SkillHandler;
import de.teamlapen.vampirism.entity.player.skills.SkillNode;
import de.teamlapen.vampirism.entity.player.skills.SkillTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.advancements.AdvancementTabType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ScreenUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NonnullDefault;

import java.util.HashMap;
import java.util.Map;

@NonnullDefault
public class SkillsTabScreen extends GuiComponent {

    public static final int SCREEN_WIDTH = SkillsScreen.SCREEN_WIDTH - 18;
    public static final int SCREEN_HEIGHT = SkillsScreen.SCREEN_HEIGHT - 47;
    private final Minecraft minecraft;
    private final SkillsScreen screen;
    private final ISkillHandler<?> skillHandler;
    private final ItemStack icon;
    private final Component title;
    private final Map<SkillNode, SkillNodeScreen> nodes = new HashMap<>();
    private final AdvancementTabType position;
    private final SkillNodeScreen root;
    private final int treeWidth;
    private final int treeHeight;
    private final ResourceLocation background;
    private double scrollX;
    private double scrollY;
    private int minX = Integer.MIN_VALUE;
    private int minY = Integer.MIN_VALUE;
    private int maxX = Integer.MAX_VALUE;
    private int maxY = Integer.MAX_VALUE;
    private float zoom = 1;
    private final int index;
    private float fade;


    public SkillsTabScreen(@NotNull Minecraft minecraft, @NotNull SkillsScreen screen, int index, @NotNull ItemStack icon, @NotNull SkillNode rootNode, @NotNull ISkillHandler<?> skillHandler, @NotNull Component title) {
        this.minecraft = minecraft;
        this.screen = screen;
        this.skillHandler = skillHandler;
        this.index = index;
        this.icon = icon;
        this.title = title;
        this.position = AdvancementTabType.LEFT;
        this.root = new SkillNodeScreen(minecraft, screen, this, rootNode, ((SkillHandler<?>) skillHandler));
        this.treeWidth = SkillTree.getTreeWidth(rootNode);
        this.treeHeight = SkillTree.getTreeHeight(rootNode);
        this.background = new ResourceLocation(REFERENCE.MODID, "textures/gui/skills/backgrounds/level.png");
        addNode(this.root);

        recalculateBorders();
        this.scrollX = 0;
        this.scrollY = 20;
    }

    private void recalculateBorders() {
        this.minY = (int) -((this.treeHeight) * this.zoom);
        this.maxY = (int) (20 * this.zoom);

        this.minX = (int) ((-this.treeWidth/2) * this.zoom);
        this.maxX = (int) ((this.treeWidth/2) * this.zoom);

        this.center();
    }

    private void addNode(@NotNull SkillNodeScreen screen) {
        this.nodes.put(screen.getSkillNode(), screen);
        for (SkillNodeScreen child : screen.getChildren()) {
            addNode(child);
        }
    }

    public int getIndex() {
        return index;
    }

    public void drawTab(@NotNull PoseStack stack, int x, int y, boolean selected) {
        this.position.draw(stack, this, x, y, selected, this.index);
    }

    public void drawIcon(int x, int y, ItemRenderer itemRenderer) {
        this.position.drawIcon(x, y, this.index, itemRenderer, this.icon);
    }

    public boolean isMouseOver(int guiLeft, int guiTop, double mouseX, double mouseY) {
        return this.position.isMouseOver(guiLeft, guiTop, this.index, mouseX, mouseY);
    }

    public void drawContents(@NotNull PoseStack stack) {

        stack.pushPose();
        RenderSystem.enableDepthTest();
        stack.translate(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(stack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        stack.translate(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(stack, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, -16777216);
        RenderSystem.depthFunc(515);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.background);

        int i = getX();
        int j = getY();
        int k = i % 16;
        int l = j % 16;
        stack.scale(this.zoom, this.zoom, 1);

        for (int i1 = -1; i1 <= 15 / this.zoom; ++i1) {
            for (int j1 = -1; j1 <= 12 / this.zoom; ++j1) {
                blit(stack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        stack.pushPose();
        stack.translate(i,j,0);
        this.root.drawConnectivity(stack, 0, 0, true);
        this.root.drawConnectivity(stack, 0, 0, false);
        this.root.draw(stack, 0, 0);
        stack.popPose();

        RenderSystem.depthFunc(518);
        stack.translate(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(stack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        stack.translate(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        stack.popPose();

        if (this.minecraft.player.getEffect(ModEffects.OBLIVION.get()) != null) {
            stack.pushPose();
            RenderSystem.enableDepthTest();
            stack.translate(0.0F, 0.0F, 200.0F);
            fill(stack, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Mth.floor(0.5 * 255.0F) << 24);
            RenderSystem.disableDepthTest();
            stack.popPose();
            stack.pushPose();
            stack.translate(0, 0, 200);
            this.drawDisableText(stack);
            stack.popPose();
        }

    }

    public void drawTooltips(@NotNull PoseStack stack, int mouseX, int mouseY) {
        stack.pushPose();
        stack.translate(0.0F, 0.0F, 200.0F);
        fill(stack, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int scrollX = getX();
        int scrollY = getY();
        if (mouseX >= 0 && mouseX < 235 && mouseY >= 0 && mouseY < 173) {
            for (SkillNodeScreen nodeScreen : this.nodes.values()) {
                if (nodeScreen.isMouseOver(mouseX / this.zoom, mouseY / this.zoom, scrollX,  scrollY)) {
                    flag = true;
                    stack.pushPose();
                    stack.scale(this.zoom, this.zoom, 1);
                    nodeScreen.drawHover(stack, mouseX / this.zoom, mouseY / this.zoom, this.fade, scrollX, scrollY);
                    stack.popPose();
                    break;
                }
            }
        }

        stack.popPose();
        if (flag) {
            this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int mouseButton, double xDragged, double yDragged) {
        this.scrollX += xDragged;
        this.scrollY += yDragged;
        center();
    }

    private void center() {
        this.scrollX = Mth.clamp(this.scrollX, this.minX, this.maxX);
        this.scrollY = Mth.clamp(this.scrollY, this.minY, this.maxY);
    }

    public Component getTitle() {
        return this.title;
    }

    private int getX(){
        int centerX = (SCREEN_WIDTH / 2);
        centerX += scrollX;
        centerX /= this.zoom;
        return centerX;
    }

    private int getY() {
        int centerY = 20;
        centerY += scrollY;
        centerY /= this.zoom;
        return centerY;
    }

    @Nullable
    public ISkill<?> getSelected(int mouseX, int mouseY) {
        int i = getX();
        int j = getY();
        for (SkillNodeScreen screen : this.nodes.values()) {
            ISkill<?> selected = screen.getSelectedSkill(mouseX / this.zoom, mouseY / this.zoom, i, j);
            if (selected != null) {
                return selected;
            }
        }
        return null;
    }

    public int getRemainingPoints() {
        return this.skillHandler.getLeftSkillPoints();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        double scrollXP = this.scrollX * this.zoom;
        double scrollYP = this.scrollY * this.zoom;
        this.zoom = (float) (this.zoom + (amount / 25));
        float heightZoom = this.zoom;
        float widthZoom = this.zoom;
        if (this.zoom * (this.treeHeight) < (SCREEN_HEIGHT)) {
            heightZoom = Math.max(this.zoom, (float) (SCREEN_HEIGHT) / (this.treeHeight));
        }
        if (this.zoom * this.treeWidth < (SCREEN_WIDTH - 20)) {
            widthZoom = Math.max(this.zoom, (float) (SCREEN_WIDTH - 20) / (Math.max(60, this.treeWidth)));
        }

        this.zoom = Math.min(heightZoom, widthZoom);
        this.zoom = Math.min(1, this.zoom);

        this.scrollX = scrollXP / this.zoom;
        this.scrollY = scrollYP / this.zoom;

        recalculateBorders();
        return true;
    }

    public void drawDisableText(@NotNull PoseStack mStack) {
        if (this.minecraft.player.getEffect(ModEffects.OBLIVION.get()) == null) return;

        Component f = Component.translatable("text.vampirism.skill.unlock_unavailable").withStyle(ChatFormatting.WHITE);
        FormattedCharSequence s = Language.getInstance().getVisualOrder(f);

        int tooltipTextWidth = 219;
        int tooltipX = 7;
        int tooltipY = 17;
        int tooltipHeight = this.minecraft.font.lineHeight * 2;
        int backgroundColor = 0xF09b0404;//0xF0550404;;
        int borderColorStart = 0x505f0c0c;
        int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
        int zLevel = this.getBlitOffset();

        mStack.pushPose();
        Matrix4f mat = mStack.last().pose();
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
        ScreenUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

        MultiBufferSource.BufferSource renderType = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        mStack.translate(0.0D, 0.0D, zLevel);

        this.minecraft.font.drawInBatch(s, (float) tooltipX + (tooltipTextWidth / 2f) - this.minecraft.font.width(f) / 2f, (float) tooltipY + (tooltipHeight / 2f) - 3, -1, true, mat, renderType, false, 0, 15728880);

        renderType.endBatch();
        mStack.popPose();
    }
}
