package me.mazupy.kairyou.event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

public class EventProvider {
    private static final KeyEvent keyEvent = new KeyEvent();
    private static final TickEvent tickEvent = new TickEvent();
    private static final SendPacketEvent sendPacketEvent = new SendPacketEvent();
    private static final GameJoinedEvent gameJoinedEvent = new GameJoinedEvent();
    private static final GameDisconnectedEvent gameDisconnectedEvent = new GameDisconnectedEvent();
    private static final Render2DEvent render2DEvent = new Render2DEvent();
    private static final WorldRenderEvent worldRenderEvent = new WorldRenderEvent();

    public static KeyEvent keyEvent(int key, int type) {
        keyEvent.key = key;
        keyEvent.type = type;
        return keyEvent;
    }

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
}
