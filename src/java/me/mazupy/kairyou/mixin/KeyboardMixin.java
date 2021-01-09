package me.mazupy.kairyou.mixin;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int pressType, int modifier, CallbackInfo info) {
        Kairyou.EVENT_BUS.post(EventProvider.inputEvent(modifier, key, pressType));
    }
}
