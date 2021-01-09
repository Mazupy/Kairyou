package me.mazupy.kairyou.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int pressType, int modifier, CallbackInfo info) {
        Kairyou.EVENT_BUS.post(EventProvider.inputEvent(modifier, button, pressType));
    }

}
