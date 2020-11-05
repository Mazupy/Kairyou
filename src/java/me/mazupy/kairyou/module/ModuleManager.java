package me.mazupy.kairyou.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.KeyEvent;
import me.mazupy.kairyou.module.movement.*;
import me.mazupy.kairyou.utils.Chat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

public class ModuleManager implements Listenable {
    
    public static ModuleManager INSTANCE;

    private final List<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        if (INSTANCE == null) INSTANCE = this;

        addModules();

        Kairyou.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private final Listener<KeyEvent> onKey = new Listener<>(event -> {
        if (event.type != GLFW.GLFW_PRESS) return;
        if (event.key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            for (Module module : modules) {
                module.toggle();
                Chat.playerChat(module.name + " is now " + module.getActive());
            }
        }
    });

    private void addModules() {
        modules.add(new AutoSprint());
    }
}
