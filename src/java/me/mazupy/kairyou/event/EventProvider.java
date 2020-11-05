package me.mazupy.kairyou.event;

public class EventProvider {
    private static final KeyEvent keyEvent = new KeyEvent();
    private static final TickEvent tickEvent = new TickEvent();

    public static KeyEvent keyEvent(int key, int type) {
        keyEvent.key = key;
        keyEvent.type = type;
        return keyEvent;
    }

    public static TickEvent tickEvent() {
        return tickEvent;
    }
}
