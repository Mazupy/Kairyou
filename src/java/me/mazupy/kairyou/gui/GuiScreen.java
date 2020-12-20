package me.mazupy.kairyou.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import me.zero.alpine.listener.Listenable;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.utils.Utils;

public class GuiScreen extends Screen implements Listenable {

    private final GuiRenderer RENDERER = new GuiRenderer();

    public GuiScreen() {
        super(LiteralText.EMPTY);
    }

    @Override
    protected void init() {
        Kairyou.EVENT_BUS.subscribe(this);
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

        RENDERER.render();
    }

    // @Override
    // public void resize(MinecraftClient client, int width, int height) {
        
    // }

    @Override
    public boolean shouldCloseOnEsc() { // GuiManager already handles esc
        return false;
    }

    @Override
    public void onClose() {
        Kairyou.EVENT_BUS.unsubscribe(this);
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}
