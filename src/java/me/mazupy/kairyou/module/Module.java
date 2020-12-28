package me.mazupy.kairyou.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;

import me.mazupy.kairyou.Kairyou;

public abstract class Module implements Listenable {

    protected static final MinecraftClient mc = Kairyou.MC;
    
    protected final String name;
    protected final String description;
    protected final Category category;
    // protected final Settings settings;

    private boolean active = false;
    private boolean enabled = false;

    public int yOff, w, h;

    protected Module() {
        final Info annotation = getAnnotation();
        name = annotation.name();
        description = annotation.description();
        category = annotation.category();
    }

    public int getX() {
        return getCategory().x;
    }

    public int getY() {
        return getCategory().y + ModuleManager.MODULE_HEIGHT + yOff;
    }

    public boolean isAt(int mX, int mY) {
        return mX >= getX() &&
               mX <= getX() + w &&
               mY >= getY() &&
               mY <= getY() + h;
    }

    public void toggle() {
        enabled = !enabled;
        toggleActive();
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

    public boolean getActive() {
        return active;
    }

    public boolean getEnabled() {
        return enabled;
    }

    protected void onActivate() {}
    protected void onDeactivate() {}

}
