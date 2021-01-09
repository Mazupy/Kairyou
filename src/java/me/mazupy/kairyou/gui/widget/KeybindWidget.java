package me.mazupy.kairyou.gui.widget;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.rendering.ShapeRenderer.Alignment;
import me.mazupy.kairyou.setting.KeybindSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

import static org.lwjgl.glfw.GLFW.*;
import static me.mazupy.kairyou.gui.ModuleSettingsScreen.*;

public class KeybindWidget extends Widget {

    public static final int SET_BIND_KEY = GLFW_MOUSE_BUTTON_RIGHT;
    public static final double BIND_TIME = 1000;
    public static final double BIND_KEY_BIND_TIME = 100;

    private final KeybindSetting setting;
    private final Rectangle bindBox;
    private final Rectangle cooldownBox;

    public KeybindWidget(KeybindSetting setting, Rectangle dim) {
        super(setting.label, dim);
        this.setting = setting;

        bindBox = settingBox(dim, 1.0, 1, true);
        cooldownBox = bindBox.inset(1);
    }

    @Override
    public int correctY(int y) {
        bindBox.y = y + dim.h - BOX_MARGIN - bindBox.h;
        cooldownBox.y = bindBox.y + 1;
        return super.correctY(y);
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = bindBox.isAt(mX, mY);
        if (clicked) {
            if (button == SET_BIND_KEY) setting.setBind();
        }
        return clicked;
    }
    
    @Override
    public void render() {

        ShapeRenderer.rect(dim, Color.Widget, Color.Outline);
        ShapeRenderer.textAlign(label, dim, Color.Text, Alignment.TopLeft, false);

        final double W = cooldownBox.w * ((setting.bindSetTime - System.currentTimeMillis()) / BIND_TIME + 1);

        ShapeRenderer.rect(bindBox, Color.Widget, Color.Outline);
        if (W > 0) {
            ShapeRenderer.quad(cooldownBox.x, cooldownBox.y, W, cooldownBox.h, Color.Active);
            setting.updateBind();
        } else ModuleSettingsScreen.locked = false;
        ShapeRenderer.textAlign(setting.get().readableString, bindBox, Color.Text, Alignment.Left, false);
    }

}
