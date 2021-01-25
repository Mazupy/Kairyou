package me.mazupy.kairyou.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.GameDisconnectedEvent;
import me.mazupy.kairyou.event.GameJoinedEvent;
import me.mazupy.kairyou.event.PlayerRespawnEvent;
import me.mazupy.kairyou.module.misc.*;
import me.mazupy.kairyou.module.movement.*;
import me.mazupy.kairyou.module.render.*;

public class ModuleManager implements Listenable {
    
    public static ModuleManager INSTANCE;

    private final Map<String, Module> modules = new HashMap<>();
    private final List<Module> activeModules = new ArrayList<>();

    public ModuleManager() {
        INSTANCE = this;

        addModules();

        Kairyou.EVENT_BUS.subscribe(this);
    }

    public void addActive(Module module) {
        activeModules.add(module);
    }

    public void removeActive(Module module) {
        activeModules.remove(module);
    }

    @EventHandler
    private final Listener<GameJoinedEvent> onGameJoin = new Listener<>(event -> {
        for (Module module : getModules()) {
            if (module.getEnabled() && !module.getActive()) module.toggleActive();
        }
    });

    @EventHandler
    private final Listener<PlayerRespawnEvent> onPlayerRespawn = new Listener<>(event -> {
        for (Module module : activeModules) module.restart();
    });

    @EventHandler
    private final Listener<GameDisconnectedEvent> onGameDisconnect = new Listener<>(event -> {
        deactivateAll();
    });

    public void deactivateAll() {
        for (int i = activeModules.size() - 1; i >= 0; i--) {
            activeModules.get(i).toggleActive();
        }
    }

    private void addModules() {
        addModule(new AutoSprint());
        addModule(new NoFall());
        addModule(new Step());
        addModule(new Hud());
        addModule(new FullBright());
        addModule(new Gui());
        addModule(new MovementSpoofer());
    }

    private void addModule(Module module) {
        modules.put(module.getName(), module);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    public List<Module> getActiveModules() {
        return activeModules;
    }
}
