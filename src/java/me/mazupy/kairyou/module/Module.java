package me.mazupy.kairyou.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.setting.Setting;
import me.mazupy.kairyou.utils.Utils;

public abstract class Module implements Listenable {

    protected static final MinecraftClient mc = Kairyou.MC;
    
    protected final String name;
    protected final String description;
    protected final Category category;
    protected final List<Setting<?>> settings;

    private boolean active = false;
    private boolean enabled = false;

    protected Module() {
        final Info annotation = getAnnotation();
        name = annotation.name();
        description = annotation.description();
        category = annotation.category();
        settings = new ArrayList<>(1);
    }

    public void restart() {
        if (enabled && active) {
            onDeactivate();
            onActivate();
        }
    }

    public void toggle() {
        enabled = !enabled;
        // Only apply toggle when in game. Yes, it's not not in game
        if (!Utils.notInGame()) toggleActive();
    }

    public void toggleActive() {
        if (active) {
            ModuleManager.INSTANCE.removeActive(this);
            Kairyou.EVENT_BUS.unsubscribe(this);
            onDeactivate();
        } else { // not active
            ModuleManager.INSTANCE.addActive(this);
            Kairyou.EVENT_BUS.subscribe(this);
            onActivate();
            enabled = true;
        }
        active = !active;
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class))
            return getClass().getAnnotation(Info.class);
        throw new IllegalStateException("Missing annotation on class " + this.getClass().getCanonicalName());
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        String description() default "Descriptionless";
        Category category();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    protected void onActivate() {}
    protected void onDeactivate() {}

}
