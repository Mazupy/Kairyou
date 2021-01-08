package me.mazupy.kairyou.gui.widget;

import java.util.ArrayList;
import java.util.List;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

import static org.lwjgl.glfw.GLFW.*;

public class DropdownWidget extends Widget {

    protected final List<Widget> childWidgets = new ArrayList<>();

    protected boolean dropped = true;

    public DropdownWidget(String label, Rectangle dim) {
        super(label, dim);
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        if (dropped && tryClickChildren(childWidgets, mX, mY, button)) return true;
        boolean clicked = dim.isAt(mX, mY) && button == GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) dropped = !dropped;
        return clicked;
    }

    @Override
    public void render() {
        super.render();
        
        // Dropdown arrow with background
        final int QY = dim.y + 1;
        final int QH = dim.h - 2;
        final int QX = dim.x + dim.w - QH - 1;
        ShapeRenderer.quad(QX, QY, QH, QH, Color.Widget);
        final int LX = QX + 1;
        final int LY = QY + 1;
        final double LH = QH - 2;
        ShapeRenderer.line(LX, LY + LH / 2, LX + LH / 2, LY + LH, Color.Outline);
        if (dropped) {
            ShapeRenderer.line(LX + LH / 2, LY + LH, LX + LH, LY + LH / 2, Color.Outline);
            renderChildren(childWidgets);
        } else ShapeRenderer.line(LX, LY + LH / 2, LX + LH / 2, LY, Color.Outline);
    }
    
    public void addWidget(Widget w) {
        childWidgets.add(w);
    }

    public int getChildrenCount() {
        return childWidgets.size();
    }

}
