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

    public Color(int greyscale, int alpha) { // TODO: unused
        this(greyscale);
        a = alpha;
    }

    public Color(int greyscale) {
        r = g = b = greyscale;
    }

    public int asARGB() {
        int argb = 0;
        argb += a * 0x01000000;
        argb += r * 0x00010000;
        argb += g * 0x00000100;
        argb += b * 0x00000001;
        return argb;
    }
    
}
