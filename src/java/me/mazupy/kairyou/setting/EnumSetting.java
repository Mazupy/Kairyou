package me.mazupy.kairyou.setting;

import net.minecraft.nbt.CompoundTag;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.SelectionWidget;
import me.mazupy.kairyou.gui.widget.Widget;
import me.mazupy.kairyou.utils.Utils;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {

    private T[] options;

    public EnumSetting(String label, T defaultValue) {
        super(label, defaultValue);

        try {
            options = (T[]) defaultValue.getDeclaringClass().getEnumConstants();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean is(T v) { // TODO: unused
        return get().equals(v);
    }

    public void next() {
        set(options[(get().ordinal() + 1) % options.length]);
    }

    public void previous() {
        set(options[(get().ordinal() - 1 + options.length) % options.length]);
    }
    
    @Override
    public Widget getWidget() {
        if (widget == null) widget = new SelectionWidget<T>(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("value", value.name());

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        T match = Utils.match(tag.getString("value"), options);
        if (match != null) set(match);
    }

}
