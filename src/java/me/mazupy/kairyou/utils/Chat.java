package me.mazupy.kairyou.utils;

import me.mazupy.kairyou.Kairyou;
import net.minecraft.text.LiteralText;

public class Chat {
    
    public static void playerChat(String msg) {
        if (Utils.notInGame()) return;
        Kairyou.MC.player.sendMessage(new LiteralText(msg), false);
    }

}
