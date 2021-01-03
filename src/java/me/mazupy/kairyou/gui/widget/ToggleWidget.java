package me.mazupy.kairyou.gui.widget;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public class ToggleWidget extends Widget {
    
    protected final Runnable action;

    protected boolean toggled;

    public ToggleWidget(String label, Rectangle dim, Runnable action) {
        super(label, dim);
        this.action = action;
    }

    @Override
    public boolean tryClick(int mX, int mY, int button) {
        boolean clicked = super.tryClick(mX, mY, button) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) {
            toggled = !toggled;
            action.run();
        }
        return clicked;
    }

    @Override
	public void render() {
        ShapeRenderer.rect(dim, toggled ? Color.ACTIVE : Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.text(label, dim, Color.TEXT);
    }

}
