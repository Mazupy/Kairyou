package me.mazupy.kairyou.utils;

import net.minecraft.util.math.MathHelper;

public abstract class MathUtils {

    public static final double RAD2DEG = 180 / Math.PI;
    public static final double DEG2RAD = Math.PI / 180;

    public static int round(double v) {
        return (int) Math.round(v);
    }

    public static int ceil(double v) {
        return (int) Math.ceil(v);
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

    public static double cube(double v) {
        return v * v * v;
    }

    public static double length(double x, double z) {
        return Math.sqrt(sq(x) + sq(z));
    }

    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static double invLerp(double v, double min, double max) {
        return clamp((v - min) / (max - min), 0, 1);
    }

    // TODO: unused
    // public static double roundTo(double value, double roundTo) {
    //     return Math.round(value / roundTo) * roundTo;
    // }

}
