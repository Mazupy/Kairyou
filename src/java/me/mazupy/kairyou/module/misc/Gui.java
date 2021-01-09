package me.mazupy.kairyou.module.misc;

import me.mazupy.kairyou.gui.GuiScreen;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;

import static org.lwjgl.glfw.GLFW.*;

@Module.Info(name = "GUI", description = "Modify the GUI", category = Category.Misc)
public class Gui extends Module {
    
    public Gui() {
        super();
        independent = true;
        bind.get().setKeyDefault(GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void restart() {
        // nothing or it crashes (toggle loop)
    }

    @Override
    public void toggle() {
        super.toggle();
        enabled = GuiScreen.guiVisible;
    }

    @Override
    protected void onActivate() {
        GuiScreen.toggleGui();
    }

    @Override
    protected void onDeactivate() {
        GuiScreen.toggleGui();
    }

}
