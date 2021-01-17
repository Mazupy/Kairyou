package me.mazupy.kairyou.setting.input;

import java.util.ArrayList;
import java.util.List;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.InputEvent;
import me.mazupy.kairyou.gui.ModuleSettingsScreen;

import static org.lwjgl.glfw.GLFW.*;

public class BindManager implements Listenable {

    public static BindManager INSTANCE;

    private static final List<Keybind> binds = new ArrayList<>();
    
    public BindManager() {
        INSTANCE = this;
        
        Kairyou.EVENT_BUS.subscribe(this);
    }

    public void addBind(Keybind bind) {
        binds.add(bind);
    }

    @EventHandler
    private final Listener<InputEvent> onInput = new Listener<>(event -> {
        KeyState.setPressed(event.key, event.type != GLFW_RELEASE);
        
        if (ModuleSettingsScreen.locked) return;
        for (Keybind bind : binds) bind.test(event);
    });

}
