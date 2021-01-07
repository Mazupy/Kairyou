package me.mazupy.kairyou.gui.widget;

import org.lwjgl.glfw.GLFW;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.setting.BooleanSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

public class TickBoxWidget extends Widget {

    private final BooleanSetting setting;
    private final Rectangle checkBox;

    public TickBoxWidget(BooleanSetting setting, Rectangle dim) {
        super(setting.label, dim);
        this.setting = setting;

        checkBox = ModuleSettingsScreen.settingBox(dim, 0, 1, false);
    }
    
    @Override
    public int correctY(int y) {
        checkBox.y = y + ModuleSettingsScreen.BOX_MARGIN;
        return super.correctY(y);
    }
    
    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = checkBox.isAt(mX, mY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
        if (clicked) setting.toggle();
        return clicked;
    }

    @Override
    public void render() {
        super.render();

        ShapeRenderer.rect(checkBox, setting.getBool() ? Color.ACTIVE : Color.WIDGET, Color.OUTLINE);
    }

}
