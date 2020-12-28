package me.mazupy.kairyou.module.render;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

@Module.Info(name = "FullBright", description = "Increases block light", category = Category.RENDER)
public class FullBright extends Module {

    private double fullBrightGamma = 16;
    private double previousGamma;

    @Override
    protected void onActivate() {
        previousGamma = mc.options.gamma;
        mc.options.gamma = fullBrightGamma;
    }

    @Override
    protected void onDeactivate() {
        mc.options.gamma = previousGamma;
    }

}
