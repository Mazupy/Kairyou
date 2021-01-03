package me.mazupy.kairyou.gui.widget;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public class DropdownWidget extends Widget {

    protected final List<Widget> childWidgets = new ArrayList<>();

    protected boolean dropped = false;

	public DropdownWidget(String label, Rectangle dim) {
        super(label, dim);
    }

    @Override
    public boolean tryClick(int mX, int mY, int button) {
        if (dropped && tryClickChildren(childWidgets, mX, mY, button)) return true;
        boolean clicked = super.tryClick(mX, mY, button) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) dropped = !dropped;
        return clicked;
    }

    @Override
    public void render() {
        super.render();
        final int QY = dim.y + 1;
        final int QH = dim.h - 2;
        final int QX = dim.x + dim.w - QH - 1;
        ShapeRenderer.quad(QX, QY, QH, QH, Color.WIDGET);
        final int LX = QX + 1;
        final int LY = QY + 1;
        final double LH = QH - 2;
        ShapeRenderer.line(LX, LY + LH / 2, LX + LH / 2, LY + LH, Color.OUTLINE);
        if (dropped) {
            ShapeRenderer.line(LX + LH / 2, LY + LH, LX + LH, LY + LH / 2, Color.OUTLINE);
            renderChildren(childWidgets);
        } else ShapeRenderer.line(LX, LY + LH / 2, LX + LH / 2, LY, Color.OUTLINE);
    }
    
    public void addWidget(Widget w) {
        childWidgets.add(w);
    }

    public int getChildrenCount() {
        return childWidgets.size();
    }

}
