package me.mazupy.kairyou.setting;

import java.util.function.Function;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.SliderDoubleWidget;
import me.mazupy.kairyou.gui.widget.Widget;
import me.mazupy.kairyou.utils.MathUtils;

public class DoubleSetting extends Setting<Double> {
    
    private final double min, max;
    private final Scale scale;

    public DoubleSetting(String label, double defaultValue, double min, double max, Scale scale) {
        super(label, defaultValue);
        
        this.min = min;
        this.max = max;
        this.scale = scale;
    }

    public DoubleSetting(String label, double defaultValue, double min, double max) {
        super(label, defaultValue);
        
        this.min = min;
        this.max = max;
        scale = Scale.Liniar;
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
        return scale.invTransform.apply(MathUtils.invLerp(value, min, max));
    }

    public void setNormValue(double delta) {
        value = MathHelper.lerp(scale.transform.apply(delta), min, max);
    }
    
    @Override
    public Widget getWidget() {
        if (widget == null) widget = new SliderDoubleWidget(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putDouble("value", getDouble());

        return tag;
    }

    public void fromTag(NbtCompound tag) {
        set(tag.getDouble("value"));
    }

    public enum Scale {
        Liniar(v -> v, v -> v),
        CubePlus(Scale::cubePlus, Scale::cubePlusInv);

        private static final double CUBE_PLUS_STIFFNESS = 0.25;
        Function<Double, Double> transform;
        Function<Double, Double> invTransform;

        Scale(Function<Double, Double> formula, Function<Double, Double> reverse) {
            transform = formula;
            invTransform = reverse;
        }

        private static double cubePlus(double v) { // (x^3 + x * S) / (1 + S)
            return (MathUtils.cube(v) + v * CUBE_PLUS_STIFFNESS) / (1 + CUBE_PLUS_STIFFNESS);
        }

        private static double cubePlusInv(double v) { // Figuring this out way took longer than expected
            final double q = MathUtils.cube(CUBE_PLUS_STIFFNESS) / 27;
            final double midX = v / 2 * (1 + CUBE_PLUS_STIFFNESS);
            final double xDiff = Math.sqrt(MathUtils.sq(midX) + q);
            return Math.cbrt(midX + xDiff) + Math.cbrt(midX - xDiff);
        }
    }

}
