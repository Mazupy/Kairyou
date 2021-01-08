package me.mazupy.kairyou.module.render;

import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.setting.DoubleSetting;

@Module.Info(name = "FullBright", description = "Increases block light", category = Category.Render)
public class FullBright extends Module {

    private DoubleSetting fullBrightGamma = new DoubleSetting("Gamma", 16, 0, 16);
    private double previousGamma;

    public FullBright() {
        settings.add(fullBrightGamma);
    }

    @Override
    protected void onActivate() {
        previousGamma = mc.options.gamma;
        mc.options.gamma = fullBrightGamma.getDouble();
    }

    @Override
    protected void onDeactivate() {
        mc.options.gamma = previousGamma;
    }

}
