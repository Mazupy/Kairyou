package me.mazupy.kairyou.setting;

import net.minecraft.nbt.NbtCompound;

import me.mazupy.kairyou.gui.ModuleSettingsScreen;
import me.mazupy.kairyou.gui.widget.KeybindWidget;
import me.mazupy.kairyou.gui.widget.Widget;
import me.mazupy.kairyou.setting.input.Keybind;

public class KeybindSetting extends Setting<Keybind> {

    public long bindSetTime = 0;

    public KeybindSetting(String label, Keybind defaultBind) {
        super(label, defaultBind);
    }

    @Override
    public Widget getWidget() {
        if (widget == null) widget = new KeybindWidget(this, ModuleSettingsScreen.defaultDim(2));
        return widget;
    }

    public void setBind() {
        bindSetTime = System.currentTimeMillis();
        ModuleSettingsScreen.locked = true;
    }

    public void updateBind() {
        double timeLeft = bindSetTime - System.currentTimeMillis() + KeybindWidget.BIND_TIME;
        if (timeLeft > 0) value.set(timeLeft);
    }

    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("bind", value.toTag());

        return tag;
    }

    public void fromTag(NbtCompound tag) {
        get().fromTag((NbtCompound) tag.get("bind"));
    }

}
