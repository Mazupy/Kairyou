package me.mazupy.kairyou.utils;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.KeyEvent;
import me.mazupy.kairyou.gui.GuiScreen;

public class KeyManager implements Listenable {

    public KeyManager() {
        Kairyou.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private final Listener<KeyEvent> onKey = new Listener<>(event -> {
        if (event.type != GLFW.GLFW_PRESS) return;

        if (event.key == GLFW.GLFW_KEY_RIGHT_SHIFT) GuiScreen.toggleGui(); // TODO: make modular
    });
    
}
