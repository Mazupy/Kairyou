package me.mazupy.kairyou.gui.widget;

import java.util.List;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public abstract class Widget {

    /**
     * @param dim   The x and y of dim are offsets to the parent element if there's one
     */
    
    protected final String label;
    protected final Rectangle dim;

    public Widget(String label, Rectangle dim) {
        this.label = label;
        this.dim = dim;
    }

    public int getHeight() {
        return dim.h;
    }
    
    public int correctY(int y) {
        dim.y = y;
        return dim.y + dim.h;
    }

    public abstract boolean tryClick(double mX, double mY, int button);

    public boolean tryClickChildren(List<Widget> children, double mX, double mY, int button) {
        final double X = mX - dim.x;
        final double Y = mY - dim.y;
        for (Widget w : children) {
            if (w.tryClick(X, Y, button)) return true;
        }
        return false;
    }

    public boolean tryScroll(double mX, double mY, double amount) {
        return false;
    }

    public void render() {
        ShapeRenderer.rect(dim, Color.Widget, Color.Outline);
        ShapeRenderer.text(label, dim, Color.Text);
    }

    public void renderChildren(List<Widget> children) {
        ShapeRenderer.xOffset += dim.x;
        ShapeRenderer.yOffset += dim.y;
        for (Widget w : children) w.render();
        ShapeRenderer.xOffset -= dim.x;
        ShapeRenderer.yOffset -= dim.y;
    }

}
