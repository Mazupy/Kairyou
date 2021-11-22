package me.mazupy.kairyou.module.movement;

import com.mojang.authlib.GameProfile;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.event.TickEvent;
import me.mazupy.kairyou.event.WorldRenderEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.rendering.OverlayProjector;
import me.mazupy.kairyou.setting.KeybindSetting;
import me.mazupy.kairyou.setting.input.Keybind;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "MovementSpoofer", description = "Tries to predict and simulate movement patterns", category = Category.Movement)
public class MovementSpoofer extends Module {

    private final KeybindSetting reset = new KeybindSetting("Reset ghost", new Keybind(this::reset));

    private FakePlayerEntity fakePlayer;
    final private ClientPlayNetworkHandler fakeCPNH;
    
    public MovementSpoofer() {
        settings.add(reset);
        
        ClientConnection fakeCC = new ClientConnection(NetworkSide.CLIENTBOUND);
        GameProfile fakeGP = new GameProfile(null, "fake01234567890123456789======");
        fakeCPNH = new ClientPlayNetworkHandler(mc, null, fakeCC, fakeGP);
    }

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        fakePlayer.tick();
    });

    @EventHandler
    private final Listener<WorldRenderEvent> onRender = new Listener<> (event -> {
        Vec3d pos = Utils.getRenderPos(fakePlayer);
        OverlayProjector.lineBox(pos.x, pos.y, pos.z, fakePlayer.getWidth(), fakePlayer.getHeight(), Color.Blue);
    });

    @Override
    protected void onActivate() {
        reset();
    }

    private void reset() {
        if (Utils.notInGame()) return;
        
        fakePlayer = new FakePlayerEntity(fakeCPNH, mc.player.getPos(), mc.player.yaw, mc.player.pitch);
    }

}
