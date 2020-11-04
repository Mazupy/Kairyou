package me.mazupy.kairyou.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.minecraft.client.MinecraftClient;

public abstract class Module {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    protected final String name;
    protected final String description;
    protected final Category category;

    // protected final Settings settings;

    protected Module() {
        final Info annotation = getAnnotation();
        name = annotation.name();
        description = annotation.description();
        category = annotation.category();
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

}
