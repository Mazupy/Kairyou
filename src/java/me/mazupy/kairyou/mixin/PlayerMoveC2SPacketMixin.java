package me.mazupy.kairyou.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.mazupy.kairyou.mixininterfaces.IPlayerMoveC2SPacket;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin implements IPlayerMoveC2SPacket {

    @Shadow protected double y;
    @Shadow protected boolean onGround;

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    
}
