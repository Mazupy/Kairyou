package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.event.SendPacketEvent;
import me.mazupy.kairyou.mixin.interfaces.IPlayerMoveC2SPacket;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.setting.BooleanSetting;
import me.mazupy.kairyou.setting.IntSetting;
import me.mazupy.kairyou.utils.Utils;

import static me.mazupy.kairyou.Kairyou.*;
import static me.mazupy.kairyou.utils.DamageUtils.*;

@Module.Info(name = "NoFall", description = "Mitigates fall damage", category = Category.Movement)
public class NoFall extends Module {

    private static final IntSetting damageTolerance = new IntSetting("Fall damage tolerance", 0, 0, 35);
    private static final BooleanSetting cancelVelocity = new BooleanSetting("Cancel vertical velocity", false);

    public NoFall() {
        settings.add(damageTolerance);
        settings.add(cancelVelocity);
    }
    
    @EventHandler
    private final Listener<SendPacketEvent> onPlayerMove = new Listener<>(event -> {
        if (!(event.packet instanceof PlayerMoveC2SPacket) || Utils.isUsingElytra()) return;
        //! Assumed setOnGround would spoof damage entirely
        /* int depth = (int) Math.ceil(-mc.player.getVelocity().y);
        if (depth >= 0 && mc.player.fallDistance > FALL_TOLERANCE && !Utils.hasSpaceBelow(depth)) {
            ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
            Chat.playerChat("depth: " + depth + "; fall dist: " + mc.player.fallDistance + "; hasSpace: "
                    + Utils.hasSpaceBelow(depth));
            Chat.playerChat("setOnGround");
        } */

        // Fall damage is applied when setOnGround switches from false to true
        final double nextFallDistance = mc.player.fallDistance - mc.player.getVelocity().y;
        final float damageMultiplier = 1f; //Assume solid block (!1.17)

        final float currentFallDamage = computeFallDamage(mc.player, mc.player.fallDistance, damageMultiplier);
        final float nextFallDamage = computeFallDamage(mc.player, (float) nextFallDistance, damageMultiplier);

        if (isFatal(currentFallDamage)) return;
        if (nextFallDamage > damageTolerance.getInt() || isFatal(nextFallDamage)) {
            ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
            MC.player.fallDistance = 0;
            
            if (cancelVelocity.getBool()) {
                MC.getNetworkHandler().sendPacket(
                    new PlayerMoveC2SPacket.PositionOnly(
                        MC.player.getX(), 
                        MC.player.getY(), 
                        MC.player.getZ(), 
                        false
                    )
                );
                Vec3d vel = MC.player.getVelocity();
                MC.player.setVelocity(vel.x, 0, vel.z);
            }
        }
    });

}
