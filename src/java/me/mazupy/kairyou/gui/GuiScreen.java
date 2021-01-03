package me.mazupy.kairyou.gui;

import java.util.HashMap;
import java.util.Map;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.gui.widget.*;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

public class GuiScreen extends Screen implements Listenable {

    public static GuiScreen INSTANCE;

    // Module positions
    public static final int MARGIN = 8;
    public static final int MODULE_WIDTH = 57;
    public static final int MODULE_HEIGHT = 11;

    private final Screen parentScreen;
    private final Map<String, Widget> widgets = new HashMap<>();

    public GuiScreen() {
        super(LiteralText.EMPTY);
        parentScreen = Kairyou.MC.currentScreen;
        Kairyou.EVENT_BUS.subscribe(this);

        // Add category dropdowns
        int nextX = MARGIN;
        for (Category c : Category.values()) {
            Rectangle dim = new Rectangle(nextX, MARGIN, MODULE_WIDTH, MODULE_HEIGHT);
            nextX += MODULE_WIDTH;
            widgets.put(c.name(), new DropdownWidget(c.name(), dim));
        }

        // Add modules to categories
        for (Module mod : ModuleManager.INSTANCE.getModules()) {
            DropdownWidget parentWidget = (DropdownWidget) widgets.get(mod.getCategory().name());
            int y = (parentWidget.getChildrenCount() + 1) * MODULE_HEIGHT;
            Rectangle dim = new Rectangle(0, y, MODULE_WIDTH, MODULE_HEIGHT);
            parentWidget.addWidget(new ToggleWidget(mod.getName(), dim, () -> mod.toggle()));
        }

        INSTANCE = this;
    }

    // @Override
    // public void mouseMoved(double mouseX, double mouseY) {
        
    // }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final int mX = ShapeRenderer.reverseConvert(mouseX);
        final int mY = ShapeRenderer.reverseConvert(mouseY);

        for (Widget w : widgets.values()) {
            if (w.tryClick(mX, mY, button)) {
                playClickSound();
                break;
            }
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

        for (Widget w : widgets.values()) {
            w.render();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() { // GuiManager already handles esc
        return false;
    }

    @Override
    public void onClose() {
        Kairyou.EVENT_BUS.unsubscribe(this);
        Kairyou.MC.openScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private void playClickSound() {
        Kairyou.MC.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

}
