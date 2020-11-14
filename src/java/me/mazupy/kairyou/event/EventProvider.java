package me.mazupy.kairyou.event;

import net.minecraft.network.Packet;
import net.minecraft.text.Text;

public class EventProvider {
    private static final KeyEvent keyEvent = new KeyEvent();
    private static final TickEvent tickEvent = new TickEvent();
    private static final SendPacketEvent sendPacketEvent = new SendPacketEvent();
    private static final GameJoinedEvent gameJoinedEvent = new GameJoinedEvent();
    private static final GameDisconnectedEvent gameDisconnectedEvent = new GameDisconnectedEvent();

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
}
