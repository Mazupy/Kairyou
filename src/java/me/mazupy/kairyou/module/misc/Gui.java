package me.mazupy.kairyou.module.misc;

import me.mazupy.kairyou.gui.GuiScreen;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

import static org.lwjgl.glfw.GLFW.*;

@Module.Info(name = "GUI", description = "Modify the GUI", category = Category.Misc)
public class Gui extends Module {

    private static Gui INSTANCE;
    
    public Gui() {
        super();
        independent = true;
        bind.get().setKeyDefault(GLFW_KEY_RIGHT_SHIFT);

        INSTANCE = this;
    }

    @Override
    public void toggle() {
        GuiScreen.toggleGui();
    }

    public static void update() {
        if (INSTANCE.enabled != GuiScreen.guiVisible) INSTANCE.superToggle();
    }

    private void superToggle() {
        super.toggle();
    }

}
