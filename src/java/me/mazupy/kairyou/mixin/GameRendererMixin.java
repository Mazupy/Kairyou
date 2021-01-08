package me.mazupy.kairyou.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.EventProvider;
import me.mazupy.kairyou.rendering.OverlayProjector;
import me.mazupy.kairyou.utils.Color;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    @Inject( // WorldRenderer.render(MatrixStack matrices, float tickDelta, long timeLimit, boolean renderBlockOutline, Camera camera, GameRenderer this, LightmapTextureManager lightmapTextureManager, Matrix4f matrix)
        method = "renderWorld", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", 
            shift = At.Shift.AFTER
        )
    )
    public void onRenderWorld(float tickDelta, long timeLimit, MatrixStack matrices, CallbackInfo info) {
        Kairyou.EVENT_BUS.post(EventProvider.worldRenderEvent(tickDelta, matrices.peek().getModel()));

        OverlayProjector.line(52, 75, 35, 54, 72, 35, Color.Blue); // TODO: testing
        OverlayProjector.quad(52, 71, 34, 53, 71, 34, 53, 71, 35, 52, 71, 35, Color.Blue);
        OverlayProjector.box(51, 73, 34, Color.Blue);
    }

}
