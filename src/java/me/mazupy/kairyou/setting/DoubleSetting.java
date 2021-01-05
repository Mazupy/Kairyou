package me.mazupy.kairyou.setting;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.SliderDoubleWidget;
import me.mazupy.kairyou.gui.widget.Widget;
import me.mazupy.kairyou.utils.MathUtils;
import net.minecraft.util.math.MathHelper;

public class DoubleSetting extends Setting<Double> {
    
    private final double min, max;

    public DoubleSetting(String label, double defaultValue, double min, double max) {
        super(label, defaultValue);
        
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public void set(Double value) {
        super.set(MathUtils.clamp(value, min, max));
    }

    public double getDouble() {
        return value;
    }

    public float getFloat() {
        return (float) (double) value;
    }

    public double normValue() {
        return MathUtils.invLerp(value, min, max);
    }

    public void setNormValue(double delta) {
        value = MathHelper.lerp(delta, min, max);
    }
    
    @Override
    public Widget getWidget() {
        if (widget == null) widget = new SliderDoubleWidget(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

}
