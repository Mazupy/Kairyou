package me.mazupy.kairyou.module;

public enum Category {
    AUTOMATION,
    COMBAT,
    EXPLOIT,
    MISC,
    MOVEMENT,
    PLAYER,
    RENDER,
    SERVER;

    public static int nextX = ModuleManager.MARGIN;
    public int x, y = ModuleManager.MARGIN;
    public int moduleCount = 0;

    static {
        for (Category c : Category.values()) {
            c.x = nextX;
            nextX += ModuleManager.MODULE_WIDTH;
        }
    }
}
