package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    private static final Color WHITE = new Color(255);
    private static final Color GRAY = new Color(120);
    private static final Color DARK_GRAY = new Color(80);
    private static final Color DARK_BLUE = new Color(0, 0, 102);

    private static final int TEXT_INSET = 3;

    public void render() {
        ShapeRenderer.updateConversion();

        // Render categories
        for (Category c : Category.values()) {
            ShapeRenderer.rect(c.x, c.y, ModuleManager.MODULE_WIDTH, ModuleManager.MODULE_HEIGHT, DARK_GRAY, GRAY);
            ShapeRenderer.text(c.name(), c.x + TEXT_INSET, c.y + TEXT_INSET, WHITE);
        }

        // Render modules
        for (Module mod : ModuleManager.INSTANCE.getModules()) {
            ShapeRenderer.rect(mod.getX(), mod.getY(), mod.w, mod.h, mod.getActive() ? DARK_BLUE : GRAY, DARK_GRAY);
            ShapeRenderer.text(mod.getName(), mod.getX() + TEXT_INSET, mod.getY() + TEXT_INSET, WHITE);
        }
    }
    
}
