package me.mazupy.kairyou.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;
import me.mazupy.kairyou.rendering.ShapeRenderer;
import me.mazupy.kairyou.utils.Utils;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Redirect(
        method = "getWindowTitle", 
        at = @At( // isModded()
            value = "INVOKE", 
            target = "Lnet/minecraft/client/MinecraftClient;isModded()Z"
        )
    )
    private boolean isModded(MinecraftClient client) {
        return false;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo info) {
        // The conversion doesn't need to be updated every render call. Every tick should be enough
        ShapeRenderer.updateConversion();

        if (Utils.notInGame()) return;
        Kairyou.EVENT_BUS.post(EventProvider.tickEvent());
    }

}
