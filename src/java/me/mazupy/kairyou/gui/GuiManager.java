package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.KeyEvent;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.utils.Chat;
import me.mazupy.kairyou.utils.Utils;

public class GuiManager implements Listenable {

    public static GuiManager INSTANCE;

    public static boolean guiVisible = false;

    public GuiManager() {
        if (INSTANCE == null) INSTANCE = this;

        Kairyou.EVENT_BUS.subscribe(this);
    }
    
    @EventHandler
    private final Listener<KeyEvent> onKey = new Listener<>(event -> {
        if (event.type != GLFW.GLFW_PRESS || Utils.notInGame()) return;
        if (event.key == GLFW.GLFW_KEY_F6) { // FIXME: just testing
            for (Module module : ModuleManager.INSTANCE.getModules()) module.toggle();
        } else if (event.key == GLFW.GLFW_KEY_RIGHT_SHIFT) { // TODO: Make modular
            if (guiVisible) Kairyou.EVENT_BUS.unsubscribe(GuiRenderer.INSTANCE);
            else Kairyou.EVENT_BUS.subscribe(GuiRenderer.INSTANCE);

            guiVisible = !guiVisible;

            Chat.playerChat("GUI is now " + (guiVisible ? "" : "not ") + "visible.");
        }
        else if (event.key == GLFW.GLFW_KEY_1) {
            Module stepMod = ModuleManager.INSTANCE.getModule("step");
            if (stepMod != null) stepMod.toggle();
        }
    });

}
