package me.mazupy.kairyou.utils;

import org.lwjgl.glfw.GLFW;

public abstract class KeyState { // TODO: unused
    
    private static final boolean[] keyStates = new boolean[348]; // 348 is the hightest GLFW key code
    
    public static void setPressed(int keyCode, boolean pressed) {
        if (validKey(keyCode)) keyStates[keyCode] = pressed;
    }

    public static boolean isPressed(int keyCode) {
        return validKey(keyCode) && keyStates[keyCode];
    }

    private static boolean validKey(int keyCode) {
        return keyCode > 0 && keyCode < keyStates.length && keyCode != GLFW.GLFW_KEY_UNKNOWN;
    }

}
