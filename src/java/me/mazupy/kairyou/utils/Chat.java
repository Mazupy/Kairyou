package me.mazupy.kairyou.utils;

import net.minecraft.text.LiteralText;

import me.mazupy.kairyou.Kairyou;

public abstract class Chat {
    
    public static void playerChat(String msg) {
        if (Utils.notInGame()) return;
        Kairyou.MC.player.sendMessage(new LiteralText(msg), false);
    }

}
