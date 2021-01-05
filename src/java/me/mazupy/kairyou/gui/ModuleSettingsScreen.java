package me.mazupy.kairyou.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.gui.widget.*;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.setting.Setting;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

public class ModuleSettingsScreen extends Screen {
    
    private final List<Widget> widgets = new ArrayList<>();

    private Screen parentScreen;
    private Module module;
    private boolean mouseDown = false;

    public ModuleSettingsScreen(Screen parentScreen, Module module) {
        super(LiteralText.EMPTY);
        this.parentScreen = parentScreen;
        this.module = module;

        int totalHeight = 0;
        for (Setting<?> setting : module.getSettings()) {
            widgets.add(setting.getWidget());
            totalHeight += setting.getWidget().getHeight();
        }
        int newY = (ShapeRenderer.maxY() - totalHeight) / 2;
        for (Widget w : widgets) newY = w.correctY(newY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (mouseDown) {
            mouseClicked(mouseX, mouseY, Utils.MOUSE_DRAG_FLAG);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final double mX = ShapeRenderer.reverseConvert(mouseX);
        final double mY = ShapeRenderer.reverseConvert(mouseY);

        for (Widget w : widgets) {
            if (w.tryClick(mX, mY, button)) {
                if (button != Utils.MOUSE_DRAG_FLAG) Utils.playClickSound();
                break;
            }
        }

        mouseDown = true;
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

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

        for (Widget w : widgets) w.render();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        module.restart();
        Kairyou.MC.openScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public static Rectangle defaultDim(int rowHeight) {
        final int w = ModuleSettingsScreen.width();
        return new Rectangle((ShapeRenderer.maxX() - w) / 2, 0, w, rowHeight * GuiScreen.MODULE_HEIGHT);
    }

    private static int width() { // TODO: make modular
        return ShapeRenderer.maxX() / 3;
    }

}
