package me.mazupy.kairyou;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import me.mazupy.kairyou.gui.GuiManager;
import me.mazupy.kairyou.module.ModuleManager;

public class Kairyou implements ClientModInitializer {

    public static final EventBus EVENT_BUS = new EventManager();
    public static final MinecraftClient MC = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        System.out.println("Client init");

        new ModuleManager();
        new GuiManager();

        // Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

}
