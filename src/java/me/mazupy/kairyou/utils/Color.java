package me.mazupy.kairyou.utils;

public class Color {

    public int r, g, b, a = 255;

    public Color(int red, int green, int blue, int alpha) {
        this(red, green, blue);
        a = alpha;
    }

    public Color(int red, int green, int blue) {
        r = red;
        g = green;
        b = blue;
    }
    
}
