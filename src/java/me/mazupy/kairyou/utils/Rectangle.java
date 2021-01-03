package me.mazupy.kairyou.utils;

public class Rectangle {

    public int x, y, w, h;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rectangle copy() {
        return new Rectangle(x, y, w, h);
    }

    public boolean isAt(int mX, int mY) {
        return mX >= x &&
               mX <= x + w &&
               mY >= y &&
               mY <= y + h;
    }
    
}
