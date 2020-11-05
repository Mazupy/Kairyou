package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import me.mazupy.kairyou.event.TickEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

@Module.Info(name = "AutoSprint", description = "Automatically makes the player sprint", category = Category.MOVEMENT)
public class AutoSprint extends Module {

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        if (mc.player.forwardSpeed > 0) mc.player.setSprinting(true);
    });

}
