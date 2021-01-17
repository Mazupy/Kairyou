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
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "HUD", description = "Shows HUD", category = Category.Render)
public class Hud extends Module {
    
    @EventHandler
    private final Listener<Render2DEvent> on2DRender = new Listener<>(event -> {
        if (Kairyou.MC.options.debugEnabled || GuiScreen.guiVisible) return;

        textBottomLeft("Kairyou v" + Kairyou.VERSION, 0);
        final int activeModules = ModuleManager.INSTANCE.getActiveModules().size();
        final int moduleCount = ModuleManager.INSTANCE.getModules().size() - 1; // -1 because the Gui module shouldn't count
        textBottomLeft("Active modules: " + activeModules + "/" + moduleCount, 1);

        // TODO: testing
        if (Utils.notInGame()) return;
        ShapeRenderer.item(mc.player.inventory.getMainHandStack(), ShapeRenderer.maxX() / 2, ShapeRenderer.maxY() / 2 + 12, Alignment.TopMid);
    });
    
    private void textBottomLeft(String text, int index) {
        int y = ShapeRenderer.maxY() - GuiScreen.MARGIN - index * Kairyou.MC.textRenderer.fontHeight;
        ShapeRenderer.textAlign(text, GuiScreen.MARGIN, y, Color.Text, Alignment.BottomLeft, true);
    }

}
