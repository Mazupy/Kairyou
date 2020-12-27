package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final int mX = ShapeRenderer.reverseConvert(mouseX);
        final int mY = ShapeRenderer.reverseConvert(mouseY);

        switch (button) { // TODO: make modular
            case GLFW.GLFW_MOUSE_BUTTON_1:
                Module mod = ModuleManager.INSTANCE.getModuleAt(mX, mY);
                if (mod != null) mod.toggle();
                break;
            default:
                // Nothing
        }

        return true;
    }

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
