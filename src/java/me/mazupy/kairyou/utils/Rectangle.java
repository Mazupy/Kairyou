package me.mazupy.kairyou.utils;

public class Rectangle {

    public int x, y, w, h;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean isAt(double mX, double mY) {
        return mX >= x &&
               mX <= x + w &&
               mY >= y &&
               mY <= y + h;
    }
    
}
