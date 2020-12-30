package me.mazupy.kairyou.utils;

import net.minecraft.util.math.MathHelper;

public abstract class MathUtils {

    public static final double RAD2DEG = 180 / Math.PI;
    public static final double DEG2RAD = Math.PI / 180;

    public static int round(double v) {
        return (int) Math.round(v);
    }

	public static float tan(double radians) {
        return MathHelper.sin((float) radians) / MathHelper.cos((float) radians);
    }
    
    public static float degTan(double degrees) {
        return tan(degrees * DEG2RAD);
    }

    public static double sq(double v) {
        return v * v;
    }

    public static double length(double x, double z) {
        return MathHelper.sqrt(sq(x) + sq(z));
    }

}
