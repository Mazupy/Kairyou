package me.mazupy.kairyou.setting;

import me.mazupy.kairyou.gui.widget.Widget;

public abstract class Setting<T> {

    public final String label;
    protected Widget widget;

    private final T defaultValue;
    protected T value;
    
    public Setting(String label, T defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;

        reset();
    }

    public void reset() {
        value = defaultValue;
    }

    public void set(T newValue) {
        value = newValue;
    }

    public T get() {
        return value;
    }

    public abstract Widget getWidget();

}
