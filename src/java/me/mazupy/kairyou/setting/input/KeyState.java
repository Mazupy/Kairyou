package me.mazupy.kairyou.setting.input;

import java.util.ArrayList;
import java.util.List;

import me.mazupy.kairyou.gui.widget.KeybindWidget;

import static org.lwjgl.glfw.GLFW.*;

public final class KeyState {
    
    private static final boolean[] keyStates = new boolean[maxKeyID()];
    
    public static void setPressed(int keyCode, boolean pressed) {
        if (validKey(keyCode)) keyStates[keyCode] = pressed;
    }

    public static boolean isPressed(int keyCode) {
        return validKey(keyCode) && keyStates[keyCode];
    }

    public static List<Integer> getPressedKeys(double timeLeft) {
        List<Integer> pressedKeys = new ArrayList<>();
        for (int i = 0; i < keyStates.length; i++) {
            if (i == KeybindWidget.SET_BIND_KEY && timeLeft > KeybindWidget.BIND_KEY_BIND_TIME) continue;
            if (keyStates[i]) pressedKeys.add(i);
        }
        return pressedKeys;
    }

    private static boolean validKey(int keyCode) {
        return keyCode >= 0 && keyCode < keyStates.length && keyCode != GLFW_KEY_UNKNOWN;
    }

    public static int maxKeyID() { // 348 is the hightest GLFW key code
        return 348;
    }

}
