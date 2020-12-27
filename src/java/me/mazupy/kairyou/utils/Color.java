package me.mazupy.kairyou.utils;

public enum Color {

    WHITE(255),
    GRAY(120),
    DARK_GRAY(80),
    BLACK(0),
    DARK_BLUE(0, 0, 102);

    public int r, g, b, a = 255;

    Color(int red, int green, int blue, int alpha) {
        this(red, green, blue);
        a = alpha;
    }

    Color(int red, int green, int blue) {
        r = red;
        g = green;
        b = blue;
    }

    Color(int grayscale, int alpha) { // TODO: unused
        this(grayscale);
        a = alpha;
    }

    Color(int grayscale) {
        r = g = b = grayscale;
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
