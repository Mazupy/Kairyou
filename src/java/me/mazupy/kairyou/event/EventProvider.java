package me.mazupy.kairyou.event;

import net.minecraft.network.Packet;

public class EventProvider {
    private static final KeyEvent keyEvent = new KeyEvent();
    private static final TickEvent tickEvent = new TickEvent();
    private static final SendPacketEvent sendPacketEvent = new SendPacketEvent();

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
}
