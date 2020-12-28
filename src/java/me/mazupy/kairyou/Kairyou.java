package me.mazupy.kairyou;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import me.mazupy.kairyou.gui.GuiManager;
import me.mazupy.kairyou.module.ModuleManager;

public class Kairyou implements ClientModInitializer {

    public static final EventBus EVENT_BUS = new EventManager();
    public static final MinecraftClient MC = MinecraftClient.getInstance();
    public static final String VERSION = getVersion();

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

    private static String getVersion() {
        // Gets the version from the jar name (same as gradle.properties -> mod_version)
        try {
            File modsFolder = new File("mods");
            String kairyouJarName = modsFolder.list((File dir, String name) -> name.startsWith("Kairyou-") && name.endsWith(".jar"))[0];

            Pattern versionPattern = Pattern.compile("\\d(\\.\\d)+(-[A-z_]+)?(\\+[0-9A-z_]+)?"); // \d(\.\d)+(-[A-z_]+)?(\+[0-9A-z_]+)?
            Matcher matcher = versionPattern.matcher(kairyouJarName);
            if (matcher.find()) return matcher.group(0);
        } catch (Exception e) {}
        return "[?]";
    }

}
