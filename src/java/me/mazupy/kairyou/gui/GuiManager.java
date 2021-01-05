package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.KeyEvent;

public class GuiManager implements Listenable {

    public static boolean guiVisible = false;

    public GuiManager() {
        Kairyou.EVENT_BUS.subscribe(this);
    }

    public static void toggleGui() {
        if (guiVisible) {
            if (Kairyou.MC.currentScreen instanceof GuiScreen) {
                GuiScreen.INSTANCE.onClose();
                guiVisible = false;
            } else Kairyou.MC.currentScreen.onClose();
        } else {
            GuiScreen.INSTANCE.setParent();
            Kairyou.MC.openScreen(GuiScreen.INSTANCE);
            guiVisible = true;
        }
    }
    
    @EventHandler
    private final Listener<KeyEvent> onKey = new Listener<>(event -> {
        if (event.type != GLFW.GLFW_PRESS) return;

        if (event.key == GLFW.GLFW_KEY_ESCAPE && guiVisible) GuiManager.toggleGui();
    });

}
