package me.mazupy.kairyou.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.mazupy.kairyou.Kairyou;
import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;

public abstract class Module implements Listenable {

    protected static final MinecraftClient mc = Kairyou.MC;
    
    protected final String name;
    protected final String description;
    protected final Category category;

    private boolean active = false;

    // protected final Settings settings;

    protected Module() {
        final Info annotation = getAnnotation();
        name = annotation.name();
        description = annotation.description();
        category = annotation.category();
    }

    public void toggle() {
        if (active) {
            Kairyou.EVENT_BUS.unsubscribe(this);
        } else { // not active
            Kairyou.EVENT_BUS.subscribe(this);
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

    protected boolean getActive() {
        return active;
    }

}
