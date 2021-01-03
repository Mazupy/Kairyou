package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    private static final int TEXT_INSET = 3;

    public void render() {
        // Render categories
        for (Category c : Category.values()) {
            ShapeRenderer.rect(c.x, c.y, ModuleManager.MODULE_WIDTH, ModuleManager.MODULE_HEIGHT, Color.DARK_GRAY, Color.GRAY);
            ShapeRenderer.text(c.name(), c.x + TEXT_INSET, c.y + TEXT_INSET, Color.WHITE);
        }

        // Render modules
        for (Module mod : ModuleManager.INSTANCE.getModules()) {
            final Color innerColor = mod.getEnabled() ? Color.DARK_BLUE : Color.GRAY;

            ShapeRenderer.rect(mod.getX(), mod.getY(), mod.w, mod.h, innerColor, Color.DARK_GRAY);
            ShapeRenderer.text(mod.getName(), mod.getX() + TEXT_INSET, mod.getY() + TEXT_INSET, Color.WHITE);
        }
    }
    
}
