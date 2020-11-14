package me.mazupy.kairyou.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void onGameDisconnect(Text reason, CallbackInfo info) {
        Kairyou.EVENT_BUS.post(EventProvider.gameDisconnectedEvent(reason));
    }
    
}
