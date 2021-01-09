package me.mazupy.kairyou.gui;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import me.mazupy.kairyou.gui.widget.*;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

import static me.mazupy.kairyou.Kairyou.*;

public class GuiScreen extends Screen {

    public static GuiScreen INSTANCE;
    public static boolean guiVisible = false;

    // Module positions
    public static final int MARGIN = 8;
    public static final int MODULE_WIDTH = 57;
    public static final int MODULE_HEIGHT = 11;

    private final Map<String, Widget> widgets = new HashMap<>();

    private Screen parentScreen;

    public GuiScreen() {
        super(LiteralText.EMPTY);

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
            ModuleWidget modWidget = new ModuleWidget(mod, dim, this);
            parentWidget.addWidget(modWidget);
        }

        INSTANCE = this;
    }

    public static void toggleGui() {
        if (guiVisible) MC.currentScreen.onClose(); // Closes child screens first
        else INSTANCE.open();
    }

    public void open() {
        parentScreen = MC.currentScreen;
        MC.openScreen(INSTANCE);
        guiVisible = true;
    }

    // @Override
    // public void mouseMoved(double mouseX, double mouseY) {
        
    // }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final double mX = ShapeRenderer.reverseConvert(mouseX);
        final double mY = ShapeRenderer.reverseConvert(mouseY);

        for (Widget w : widgets.values()) {
            if (w.tryClick(mX, mY, button)) {
                Utils.playClickSound();
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

        for (Widget w : widgets.values()) w.render();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        MC.openScreen(parentScreen);
        guiVisible = false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}
