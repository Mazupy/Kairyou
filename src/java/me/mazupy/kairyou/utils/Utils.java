package me.mazupy.kairyou.utils;

import me.mazupy.kairyou.Kairyou;
import net.minecraft.client.MinecraftClient;

public class Utils {

    private static final MinecraftClient mc = Kairyou.MC;
    
    public static boolean notInGame() {
        return mc.world == null || mc.player == null;
    }

}
