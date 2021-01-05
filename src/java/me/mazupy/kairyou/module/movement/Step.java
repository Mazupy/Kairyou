package me.mazupy.kairyou.module.movement;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.setting.DoubleSetting;

@Module.Info(name = "Step", description = "Step up higher blocks", category = Category.MOVEMENT)
public class Step extends Module {

    private final DoubleSetting stepHeight = new DoubleSetting("Step height", 1f, 0f, 256f);

    private float previousStepHeight;

    public Step() {
        settings.add(stepHeight);
    }

    @Override
    protected void onActivate() {
        previousStepHeight = mc.player.stepHeight;
        mc.player.stepHeight = stepHeight.getFloat();
    }

    @Override
    protected void onDeactivate() {
        mc.player.stepHeight = previousStepHeight;
    }
    
}
