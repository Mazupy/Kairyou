package me.mazupy.kairyou.setting;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.SliderIntWidget;
import me.mazupy.kairyou.gui.widget.Widget;
import me.mazupy.kairyou.utils.MathUtils;
import net.minecraft.util.math.MathHelper;

public class IntSetting extends Setting<Integer> {
    
    private final int min, max;

    public IntSetting(String label, int defaultValue, int min, int max) {
        super(label, defaultValue);

        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void set(Integer value) {
        super.set(MathUtils.clamp(value, min, max));
    }

    public int getInt() {
        return value;
    }

    public double normValue() {
        return MathUtils.invLerp(value, min, max);
    }

    public void setNormValue(double delta) {
        value = MathUtils.round(MathHelper.lerp(delta, min, max));
    }
    
    @Override
    public Widget getWidget() {
        if (widget == null) widget = new SliderIntWidget(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

}
