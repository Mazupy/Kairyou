package me.mazupy.kairyou.gui.widget;

import net.minecraft.util.math.MathHelper;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.rendering.ShapeRenderer.Alignment;
import me.mazupy.kairyou.setting.IntSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class SliderIntWidget extends Widget {

    private final double SLIDER_MARGIN = 3;
    private final double HANDLE_WIDTH = 2;
    private final IntSetting setting;
    
    public SliderIntWidget(IntSetting setting, Rectangle dim) {
        super(setting.label, dim);
        this.setting = setting;
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = dim.isAt(mX, mY);
        clicked &= (button == GLFW_MOUSE_BUTTON_LEFT || button == Utils.MOUSE_DRAG_FLAG);
        clicked &= mY >= minY();
        if (clicked) setting.setNormValue(MathUtils.invLerp(mX, minX(), maxX()));
        return clicked;
    }

    @Override
    public boolean tryScroll(double mX, double mY, double amount) {
        boolean scrolled = dim.isAt(mX, mY);
        scrolled &= mY >= minY();
        if (scrolled) setting.set(setting.get() + (amount < 0 ? 1 : -1));
        return scrolled;
    }

    @Override
    public void render() {
        // Label
        ShapeRenderer.rect(dim, Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.textAlign(label, dim, Color.TEXT, Alignment.TOP_LEFT, false);
        
        // Value
        String value = String.format("%d", setting.getInt());
        ShapeRenderer.textAlign(value, dim, Color.TEXT, Alignment.TOP_RIGHT, false);
        
        // Slide line
        ShapeRenderer.line(minX(), midY(), maxX(), midY(), Color.ACTIVE);

        // Handle
        double normValue = setting.normValue();
        double x = MathHelper.lerp(normValue, minX(), maxX()) - HANDLE_WIDTH / 2;
        ShapeRenderer.quad(x, minY(), HANDLE_WIDTH, maxY() - minY(), Color.TEXT);
    }

    private double minX() {
        return dim.x + SLIDER_MARGIN;
    }

    private double maxX() {
        return dim.x + dim.w - SLIDER_MARGIN;
    }

    private double minY() {
        return dim.y + 0.5 * dim.h;
    }

    private double maxY() {
        return dim.y + dim.h - SLIDER_MARGIN;
    }

    private double midY() {
        return (minY() + maxY()) / 2;
    }

}
