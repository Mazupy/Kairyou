package me.mazupy.kairyou.event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

public class EventProvider {
    private static final TickEvent tickEvent = new TickEvent();
    private static final SendPacketEvent sendPacketEvent = new SendPacketEvent();
    private static final GameJoinedEvent gameJoinedEvent = new GameJoinedEvent();
    private static final GameDisconnectedEvent gameDisconnectedEvent = new GameDisconnectedEvent();
    private static final Render2DEvent render2DEvent = new Render2DEvent();
    private static final WorldRenderEvent worldRenderEvent = new WorldRenderEvent();
    private static final PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent();
    private static final InputEvent inputEvent = new InputEvent();
    private static final PreTickEvent preTickEvent = new PreTickEvent();
    private static final PreWorldRenderEvent preWorldRenderEvent = new PreWorldRenderEvent();

    public static TickEvent tickEvent() {
        return tickEvent;
    }

    public static SendPacketEvent sendPacketEvent(Packet<?> packet) {
        sendPacketEvent.packet = packet;
        return sendPacketEvent;
    }

    public static GameJoinedEvent gameJoinedEvent() {
        return gameJoinedEvent;
    }

    public static GameDisconnectedEvent gameDisconnectedEvent(Text reason) {
        gameDisconnectedEvent.reason = reason;
        return gameDisconnectedEvent;
    }

    public static Render2DEvent render2DEvent(float tickDelta, MatrixStack matrices) {
        render2DEvent.tickDelta = tickDelta;
        render2DEvent.matrixStack = matrices;
        return render2DEvent;
    }

    public static WorldRenderEvent worldRenderEvent(float tickDelta, Matrix4f matrix) {
        worldRenderEvent.tickDelta = tickDelta;
        worldRenderEvent.matrix = matrix;
        return worldRenderEvent;
    }

    public static PlayerRespawnEvent playerRespawnEvent() {
        return playerRespawnEvent;
    }

    public static InputEvent inputEvent(int modifier, int key, int type) {
        inputEvent.modifier = modifier;
        inputEvent.key = key;
        inputEvent.type = type;
        return inputEvent;
    }

    public static PreTickEvent preTickEvent() {
        return preTickEvent;
    }

    public static PreWorldRenderEvent preWorldRenderEvent(float tickDelta) {
        preWorldRenderEvent.tickDelta = tickDelta;
        return preWorldRenderEvent;
    }
}
