package me.mazupy.kairyou.setting;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.TickBoxWidget;
import me.mazupy.kairyou.gui.widget.Widget;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String label, boolean defaultValue) {
        super(label, defaultValue);
    }

    public void toggle() {
        value = !value;
    }

    public boolean getBool() {
        return value;
    }
    
    @Override
    public Widget getWidget() {
        if (widget == null) widget = new TickBoxWidget(this, ModuleSettingsScreen.defaultDim(1));
        return widget;
    }

}
