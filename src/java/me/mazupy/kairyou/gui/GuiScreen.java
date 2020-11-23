package me.mazupy.kairyou.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import me.mazupy.kairyou.utils.Utils;

public class GuiScreen extends Screen { // FIXME: unused

    public GuiScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        
    }

    // @Override
    // public void mouseMoved(double mouseX, double mouseY) {
        
    // }

    // @Override
    // public boolean mouseClicked(double mouseX, double mouseY, int button) {
        
    // }

    // @Override
    // public boolean mouseReleased(double mouseX, double mouseY, int button) {
        
    // }

    // @Override
    // public boolean mouseScrolled(double d, double e, double amount) {
        
    // }

    // @Override
    // public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        
    // }

    // public void keyRepeated(int key, int mods) {
        
    // }

    // @Override
    // public boolean charTyped(char chr, int keyCode) {
        
    // }

    // @Override
    // public void tick() {
        
    // }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (Utils.notInGame()) renderBackground(matrices);

        // GuiRenderer.INSTANCE.begin();
        // root.render(GuiRenderer.INSTANCE, mouseX, mouseY, delta);
        // GuiRenderer.INSTANCE.end();
    }

    // @Override
    // public void resize(MinecraftClient client, int width, int height) {
        
    // }

    // @Override
    // public void onClose() {
        
    // }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
