package me.mazupy.kairyou.module.render;

import com.google.common.collect.Streams;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.event.Render2DEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.rendering.OverlayProjector;
import me.mazupy.kairyou.setting.IntSetting;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.TargetUtils;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "ESP", description = "Shows outlines of entities", category = Category.Render)
public class ESP extends Module {
    
    private IntSetting range = new IntSetting("Range", 64, 1, 256);
    
    public ESP() {
        settings.add(range);
    }
    
    @EventHandler
    private final Listener<Render2DEvent> on2DRender = new Listener<>(event -> {
        Streams.stream(mc.world.getEntities())
                .filter(entity -> TargetUtils.inRange(entity, range.getInt()))
                .forEach(this::drawOutline);
    });
    
    private void drawOutline(Entity e) {
        Vec3d pos = Utils.getRenderPos(e);
        OverlayProjector.lineBox(pos.x, pos.y, pos.z, e.getWidth(), e.getHeight(), Color.ESP);
    }
    
}
