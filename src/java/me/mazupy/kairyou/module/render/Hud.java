package me.mazupy.kairyou.module.render;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.Render2DEvent;
import me.mazupy.kairyou.gui.GuiScreen;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.rendering.ShapeRenderer.Alignment;
import me.mazupy.kairyou.utils.Color;

@Module.Info(name = "HUD", description = "Shows HUD", category = Category.RENDER)
public class Hud extends Module {
    
    @EventHandler
    private final Listener<Render2DEvent> on2DRender = new Listener<>(event -> {
        if (Kairyou.MC.options.debugEnabled || GuiScreen.guiVisible) return;

        textBottomLeft("Kairyou v" + Kairyou.VERSION, 0);
        final int activeModules = ModuleManager.INSTANCE.getActiveModules().size();
        final int moduleCount = ModuleManager.INSTANCE.getModules().size();
        textBottomLeft("Active modules: " + activeModules + "/" + moduleCount, 1);
    });
    
    private void textBottomLeft(String text, int index) {
        int y = ShapeRenderer.maxY() - GuiScreen.MARGIN - index * Kairyou.MC.textRenderer.fontHeight;
        ShapeRenderer.textAlign(text, GuiScreen.MARGIN, y, Color.TEXT, Alignment.BOTTOM_LEFT, true);
    }

}
