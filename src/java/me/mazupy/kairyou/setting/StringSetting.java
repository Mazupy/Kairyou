package me.mazupy.kairyou.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.TextBoxWidget;
import me.mazupy.kairyou.gui.widget.Widget;

public class StringSetting extends Setting<String> {

    private final Pattern validation;

    public StringSetting(String label, String defaultValue, String regex) {
        super(label, defaultValue);

        validation = Pattern.compile(regex);
    }

    public StringSetting(String label, String defaultValue) {
        super(label, defaultValue);

        validation = null;
    }

    @Override
    public void set(String value) {
        if (validation == null) super.set(value);
        Matcher matcher = validation.matcher(value);
        if (matcher.find()) {
            super.set(matcher.group(0));
        }
    }
    
    @Override
    public Widget getWidget() { // FIXME: WRONG
        if (widget == null) widget = new TextBoxWidget(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

}
