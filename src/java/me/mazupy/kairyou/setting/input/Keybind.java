package me.mazupy.kairyou.setting.input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.nbt.CompoundTag;

import me.mazupy.kairyou.event.InputEvent;
import me.mazupy.kairyou.setting.storage.ISavable;
import me.mazupy.kairyou.utils.HumanReadable;
import me.mazupy.kairyou.utils.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static me.mazupy.kairyou.setting.input.Trigger.*;

public class Keybind implements ISavable {

    public String readableString;
    
    private int modifier;
    private List<Integer> keys = new ArrayList<>(1);
    private int defaultKey = GLFW_KEY_UNKNOWN;
    private Trigger trigger;
    private boolean active; // TODO: unused
    private Runnable action;

    public Keybind(int key, Runnable action) {
        this(action);
        setKey(key);
    }

    public Keybind(Runnable action) {
        reset();
        this.action = action;
        BindManager.INSTANCE.addBind(this);
    }

    public void test(InputEvent event) {
        if (allKeysOfType(event.type, event.key) && 
                triggers(event.type) && 
                modifier == (event.modifier | modifierAdditive(event.key)) && 
                active) {
            action.run();
        }
    }

    private boolean triggers(int type) {
        switch (type) {
            case GLFW_RELEASE:
                return trigger == Release || trigger == Toggle;
            case GLFW_REPEAT:
                return trigger == Repeat;
            case GLFW_PRESS:
            default:
                return trigger == Press || trigger == Toggle;
        }
    }

    private boolean allKeysOfType(int type, int lastKey) {
        if (keys.isEmpty()) return false;
        boolean keyDown = type != GLFW_RELEASE;
        for (int key : keys) {
            if (!KeyState.isPressed(key) && key != lastKey) return false;
        }
        if (KeyState.isPressed(lastKey) != keyDown) return false;
        if (trigger == Toggle) return keys.contains(lastKey);
        return true;
    }

    public void set(double timeLeft) {
        List<Integer> pressedKeys = KeyState.getPressedKeys(timeLeft);
        if (pressedKeys.isEmpty()) {
            if (defaultKey != GLFW_KEY_UNKNOWN) setKey(defaultKey);
            return;
        }
        if (pressedKeys.size() == 1) {
            setKey(pressedKeys.get(0));
            return;
        }
        reset();
        for (int key : pressedKeys) addKey(key, false);
        updateString();
    }

    public void setKeyDefault(int key) {
        setKey(key);
        defaultKey = key;
    }

    public void setKey(int key) {
        reset();
        addKey(key, true);
        updateString();
    }

    private void addKey(int key, boolean singleKey) {
        int modAdd = modifierAdditive(key);
        modifier |= modAdd;
        if (singleKey || modAdd == 0) keys.add(key);
    }

    private void reset() {
        modifier = 0;
        keys.clear();
        trigger = Press;
        active = true;
        updateString();
    }

    private void updateString() {
        if (keys.isEmpty()) {
            readableString = "";
            return;
        }

        StringBuilder builder = new StringBuilder();
        if (keys.size() > 1 || modifierAdditive(keys.get(0)) == 0) {
            if (needMod(GLFW_MOD_SUPER)) builder.append("[OS]");
            if (needMod(GLFW_MOD_CONTROL)) builder.append("[CTRL]");
            if (needMod(GLFW_MOD_ALT)) builder.append("[ALT]");
            if (needMod(GLFW_MOD_SHIFT)) builder.append("[SHIFT]");
            if (needMod(GLFW_MOD_CAPS_LOCK)) builder.append("[CAPS]");
            if (needMod(GLFW_MOD_NUM_LOCK)) builder.append("[NUM]");

            if (builder.length() > 0) builder.append(" ");
        }

        builder.append(HumanReadable.keyCode(keys.get(0)));
        for (int i = 1; i < keys.size(); i++) {
            builder.append(" + " + HumanReadable.keyCode(keys.get(i)));
        }
        
        readableString = builder.toString();
    }

    public String tagName() {
        return "none";
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("modifier", modifier);
        tag.putIntArray("keys", keys);
        tag.putInt("defaultKey", defaultKey);
        tag.putString("trigger", trigger.name());
        tag.putBoolean("active", active);

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        modifier = tag.getInt("modifier");
        keys = IntStream.of(tag.getIntArray("keys")).boxed().collect(Collectors.toList());
        defaultKey = tag.getInt("defaultKey");
        trigger = Utils.match(tag.getString("trigger"), Trigger.values());
        active = tag.getBoolean("active");
        updateString();
    }

    private boolean needMod(int mod) {
        return (modifier & mod) == mod;
    }

    private int modifierAdditive(int key) {
        switch (key) {
            case  GLFW_KEY_LEFT_SHIFT:   case GLFW_KEY_RIGHT_SHIFT:
                return GLFW_MOD_SHIFT;
            case  GLFW_KEY_LEFT_CONTROL: case GLFW_KEY_RIGHT_CONTROL:
                return GLFW_MOD_CONTROL;
            case  GLFW_KEY_LEFT_ALT:     case GLFW_KEY_RIGHT_ALT:
                return GLFW_MOD_ALT;
            case  GLFW_KEY_LEFT_SUPER:   case GLFW_KEY_RIGHT_SUPER:
                return GLFW_MOD_SUPER;
            case       GLFW_KEY_CAPS_LOCK:
                return GLFW_MOD_CAPS_LOCK;
            case       GLFW_KEY_NUM_LOCK:
                return GLFW_MOD_NUM_LOCK;
            default:
                return 0;
        }
    }

}
