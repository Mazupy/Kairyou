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

    public Rectangle inset(int i) {
        return new Rectangle(x + i, y + i, w - 2 * i, h - 2 * i);
    }
    
}
