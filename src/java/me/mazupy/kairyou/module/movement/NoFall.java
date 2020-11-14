package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import me.mazupy.kairyou.event.SendPacketEvent;
import me.mazupy.kairyou.mixin.interfaces.IPlayerMoveC2SPacket;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "NoFall", description = "Mitigates fall damage", category = Category.MOVEMENT)
public class NoFall extends Module {

    private static float FALL_TOLERANCE = 3;
    
    @EventHandler
    private final Listener<SendPacketEvent> onPlayerMove = new Listener<>(event -> {
        if (!(event.packet instanceof PlayerMoveC2SPacket) || Utils.isUsingElytra()) return;
        //! Assumed setOnGround would spoof damage entirely
        // int depth = (int) Math.ceil(-mc.player.getVelocity().y);
        // if (depth >= 0 && mc.player.fallDistance > FALL_TOLERANCE && !Utils.hasSpaceBelow(depth)) {
        //     ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
        //     Chat.playerChat("depth: " + depth + "; fall dist: " + mc.player.fallDistance + "; hasSpace: "
        //             + Utils.hasSpaceBelow(depth));
        //     Chat.playerChat("setOnGround");
        // }

        // Fall damage is applied when setOnGround switches from false to true
        final double nextMovement = Utils.PLAYER_DRAG * (mc.player.getVelocity().y + Utils.PLAYER_GRAVITY);
        final double nextFallDistance = mc.player.fallDistance + nextMovement;

        if (nextFallDistance > FALL_TOLERANCE) ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
    });

}
