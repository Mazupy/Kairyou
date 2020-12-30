package me.mazupy.kairyou.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    
    @Inject(
        method = "render", 
        at = @At( // renderHotbar(float tickDelta, MatrixStack matrices);
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"
        )
    )
    private void onRender(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        Kairyou.EVENT_BUS.post(EventProvider.render2DEvent(tickDelta, matrixStack));
    }

}
