package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;

import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    private static final Color WHITE = new Color(255);
    private static final Color GRAY = new Color(120);
    private static final Color DARK_GRAY = new Color(80);
    private static final Color DARK_BLUE = new Color(0, 0, 102);

    public void render() {
        ShapeRenderer.updateConversion();

        // Render modules
        final int MARGIN = 8;
        final int WIDTH = 60;
        final int HEIGHT = 12;
        int modX = MARGIN;
        int modY = MARGIN;

        for (Module mod : ModuleManager.INSTANCE.getModules()) {
            ShapeRenderer.rect(modX, modY, WIDTH, HEIGHT, mod.getActive() ? DARK_BLUE : GRAY, DARK_GRAY);
            ShapeRenderer.text(mod.getName(), modX + 3, modY + 3, WHITE);
            modY += HEIGHT - 1;
        }
    }
    
}
