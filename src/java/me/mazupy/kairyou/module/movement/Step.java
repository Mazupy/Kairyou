package me.mazupy.kairyou.module.movement;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

@Module.Info(name = "Step", description = "Step up higher blocks", category = Category.MOVEMENT)
public class Step extends Module { // FIXME: resets on dimension swap / teleport?

    private final float stepHeight = 1;

    private float previousStepHeight;

    @Override
    protected void onActivate() {
        previousStepHeight = mc.player.stepHeight;
        mc.player.stepHeight = stepHeight;
    }

    @Override
    protected void onDeactivate() {
        mc.player.stepHeight = previousStepHeight;
    }
    
}
