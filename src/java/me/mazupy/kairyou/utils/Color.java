package me.mazupy.kairyou.utils;

public enum Color {

    TEXT(255), 
    OUTLINE(120), 
    WIDGET(80), 
    ACTIVE(0, 0, 84), 
    BLUE(0, 0, 150), 
    BACKGROUND(0, 104);

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

    Color(int grayscale, int alpha) {
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
