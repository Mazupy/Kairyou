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

    public static double sinDeg(float degrees) {
        return sin(degrees * DEG2RAD);
    }

    public static double sin(double radians) {
        return MathHelper.sin((float) radians);
    }

    public static double cosDeg(float degrees) {
        return cos(degrees * DEG2RAD);
    }

    public static double cos(double radians) {
        return MathHelper.cos((float) radians);
    }

    public static float tan(double radians) {
        return (float) (sin(radians) / cos(radians));
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

    public static float cube(float v) {
        return v * v * v;
    }

    public static double lengthSq(double x, double z) {
        return sq(x) + sq(z);
    }

    public static double length(double x, double z) {
        return Math.sqrt(lengthSq(x, z));
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
    // public static int min(int... values) {
    //     int lowest = Integer.MAX_VALUE;
    //     for (int v : values) lowest = Math.min(lowest, v);
    //     return lowest;
    // }

    // public static double roundTo(double value, double roundTo) {
    //     return Math.round(value / roundTo) * roundTo;
    // }

}
