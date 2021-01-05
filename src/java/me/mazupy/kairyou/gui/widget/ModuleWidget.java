package me.mazupy.kairyou.gui.widget;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public class ModuleWidget extends Widget {
    
    protected final Runnable action0;
    protected final Runnable action1;

    protected boolean toggled;

    public ModuleWidget(String label, Rectangle dim, Runnable action0, Runnable action1) {
        super(label, dim);
        this.action0 = action0;
        this.action1 = action1;
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = super.tryClick(mX, mY, button);
        boolean leftClicked = clicked && button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        boolean rightClicked = clicked && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
        if (leftClicked) {
            toggled = !toggled;
            action0.run();
        } else if (rightClicked) {
            action1.run();
        }
        return clicked;
    }

    @Override
    public void render() {
        ShapeRenderer.rect(dim, toggled ? Color.ACTIVE : Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.text(label, dim, Color.TEXT);
    }

}
