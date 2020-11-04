package me.mazupy.kairyou;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ClientModInitializer;

public class KairyouInit implements ClientModInitializer {

    public static final EventBus EVENT_BUS = new EventManager();

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		
	}

}
