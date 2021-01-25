package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

import me.mazupy.kairyou.event.TickEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

@Module.Info(name = "AutoSprint", description = "Automatically makes the player sprint", category = Category.Movement)
public class AutoSprint extends Module {

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        if (mc.player.forwardSpeed > 0 && 
                !mc.player.horizontalCollision && 
                !mc.player.isInSneakingPose()) {
            mc.player.setSprinting(true);
        }
    });

    // TODO: implement, but with actual benefit not just particles (virtual yaw?)
    /* protected enum Mode {
        Legit,
        Forward,
        NotSneaking,
        Always
    }

    private final EnumSetting<Mode> mode = new EnumSetting<>("Mode", Mode.Legit);

    public AutoSprint() {
        settings.add(mode);
    }

    private static boolean shouldSprint() {
        switch (mode.get()) {
            case Legit:
                if (mc.player.horizontalCollision) return false;
            case Forward:
                return mc.player.forwardSpeed > 0 && !mc.player.isInSneakingPose();
            case NotSneaking:
                if (mc.player.isInSneakingPose()) return false;
            case Always:
                return mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0;
        }
        // unreachable code, btw
        return false;
    } */

}
