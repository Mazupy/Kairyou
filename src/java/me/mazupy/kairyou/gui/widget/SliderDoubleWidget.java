package me.mazupy.kairyou.gui.widget;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.rendering.ShapeRenderer.Alignment;
import me.mazupy.kairyou.setting.DoubleSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

public class SliderDoubleWidget extends Widget {

    private final double SLIDER_MARGIN = 3;
    private final double HANDLE_WIDTH = 2;
    private final DoubleSetting setting;
    
    public SliderDoubleWidget(DoubleSetting setting, Rectangle dim) {
        super(setting.label, dim);
        this.setting = setting;
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = super.tryClick(mX, mY, button) && (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == Utils.MOUSE_DRAG_FLAG);
        clicked &= mY >= minY();
        if (clicked) setting.setNormValue(MathUtils.invLerp(mX, minX(), maxX()));
        return clicked;
    }

    @Override
    public void render() {
        // Label
        ShapeRenderer.rect(dim, Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.textAlign(label, dim.x, dim.y, Color.TEXT, Alignment.TOP_LEFT, false);
        
        // Value
        String value = String.format("%.3f", setting.getDouble());
        ShapeRenderer.textAlign(value, dim.x + dim.w, dim.y, Color.TEXT, Alignment.TOP_RIGHT, false);

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
