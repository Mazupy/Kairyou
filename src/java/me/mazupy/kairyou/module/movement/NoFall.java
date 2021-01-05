package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.SendPacketEvent;
import me.mazupy.kairyou.mixin.interfaces.IPlayerMoveC2SPacket;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.setting.BooleanSetting;
import me.mazupy.kairyou.setting.IntSetting;
import me.mazupy.kairyou.utils.DamageUtils;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "NoFall", description = "Mitigates fall damage", category = Category.MOVEMENT)
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

        final float currentFallDamage = DamageUtils.computeFallDamage(mc.player, mc.player.fallDistance, damageMultiplier);
        final float nextFallDamage = DamageUtils.computeFallDamage(mc.player, (float) nextFallDistance, damageMultiplier);

        if (DamageUtils.isFatal(currentFallDamage)) return;
        if (nextFallDamage > damageTolerance.getInt() || DamageUtils.isFatal(nextFallDamage)) {
            ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
            Kairyou.MC.player.fallDistance = 0;
            
            if (cancelVelocity.getBool()) {
                Kairyou.MC.getNetworkHandler().sendPacket(
                    new PlayerMoveC2SPacket.PositionOnly(
                        Kairyou.MC.player.getX(), 
                        Kairyou.MC.player.getY(), 
                        Kairyou.MC.player.getZ(), 
                        false
                    )
                );
                Vec3d vel = Kairyou.MC.player.getVelocity();
                Kairyou.MC.player.setVelocity(vel.x, 0, vel.z);
            }
        }
    });

}
