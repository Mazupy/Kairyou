package me.mazupy.kairyou.gui.widget;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.rendering.ShapeRenderer.Alignment;
import me.mazupy.kairyou.setting.EnumSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public class SelectionWidget<T extends Enum<T>> extends Widget {

    private final EnumSetting<T> setting;
    private final Rectangle optionBox;

    public SelectionWidget(EnumSetting<T> setting, Rectangle dim) {
        super(setting.label, dim);
        this.setting = setting;

        optionBox = ModuleSettingsScreen.settingBox(dim, 1.0, 1, true);
    }
    
    @Override
    public int correctY(int y) {
        optionBox.y = y + dim.h - ModuleSettingsScreen.BOX_MARGIN - optionBox.h;
        return super.correctY(y);
    }
    
    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = optionBox.isAt(mX, mY);
        if (clicked) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) setting.next();
            else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) setting.previous();
        }
        return clicked;
    }

    @Override
    public boolean tryScroll(double mX, double mY, double amount) {
        boolean scrolled = optionBox.isAt(mX, mY);
        if (scrolled) {
            if (amount < 0) setting.next();
            else setting.previous();
        }
        return scrolled;
    }

    @Override
    public void render() {
        ShapeRenderer.rect(dim, Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.textAlign(label, dim, Color.TEXT, Alignment.TOP_LEFT, false);

        ShapeRenderer.rect(optionBox, Color.WIDGET, Color.OUTLINE);
        ShapeRenderer.textAlign(setting.get().toString(), optionBox, Color.TEXT, Alignment.LEFT, false);
    }

}
