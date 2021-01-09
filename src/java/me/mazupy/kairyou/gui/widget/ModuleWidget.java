package me.mazupy.kairyou.gui.widget;

import net.minecraft.client.gui.screen.Screen;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Rectangle;

import static org.lwjgl.glfw.GLFW.*;
import static me.mazupy.kairyou.Kairyou.*;

public class ModuleWidget extends Widget {

    protected final Module mod;
    protected final Screen screen;

    public ModuleWidget(Module mod, Rectangle dim, Screen screen) {
        super(mod.getName(), dim);
        this.mod = mod;
        this.screen = screen;
    }

    @Override
    public boolean tryClick(double mX, double mY, int button) {
        boolean clicked = dim.isAt(mX, mY);
        if (clicked) {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                mod.toggle();
            } else if (button == GLFW_MOUSE_BUTTON_RIGHT) MC.openScreen(new ModuleSettingsScreen(screen, mod));
        }
        return clicked;
    }

    @Override
    public void render() {
        ShapeRenderer.rect(dim, mod.getEnabled() ? Color.Active : Color.Widget, Color.Outline);
        ShapeRenderer.text(label, dim, Color.Text);
    }

}
