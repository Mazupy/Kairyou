package me.mazupy.kairyou.module.combat;

import com.google.common.collect.Streams;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.event.PreWorldRenderEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.setting.DoubleSetting;
import me.mazupy.kairyou.setting.EnumSetting;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.TargetUtils;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "Aim Assist", description = "Automatically aims or helps aim", category = Category.Combat)
public class AimAssist extends Module {
    
    private enum Mode {
        Perfect,
        Human
    }

    private enum Priority {
        LowestDistance,
        HighestDistance,
        LowestHealth,
        HighestHealth
    }

    private enum TargetHeight {
        OwnEyes,
        Eyes,
        Body,
        Feet
    }

    private EnumSetting<Mode> mode = new EnumSetting<Mode>("Mode", Mode.Perfect);
    private DoubleSetting range = new DoubleSetting("Range", 6, 0, 30);
    private EnumSetting<Priority> priority = new EnumSetting<Priority>("Priority", Priority.LowestDistance);
    private EnumSetting<TargetHeight> targetHeight = new EnumSetting<TargetHeight>("Target height", TargetHeight.OwnEyes);

    public AimAssist() {
        settings.add(mode);
        settings.add(range);
        settings.add(priority);
        settings.add(targetHeight);
    }

    @EventHandler
    private final Listener<PreWorldRenderEvent> preRender = new Listener<>(event -> {
        LivingEntity target = getTarget(); // TODO: Use hitbox not pos
        if (target == null) return;
        Vec3d targetPoint = getTargetPoint(target, event.tickDelta);

        switch (mode.get()) {
            case Perfect:
                mc.player.yaw = targetYaw(targetPoint, event.tickDelta);
                mc.player.pitch = targetPitch(targetPoint, event.tickDelta);
            case Human: break; // TODO: implement
        }
    });

    private LivingEntity getTarget() {
        return Streams.stream(mc.world.getEntities())
                .filter(entity -> TargetUtils.inRange(entity, range.getDouble()))
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .filter(this::shouldAttackEntity)
                // TODO: implement
                // .filter(this::canSeeEntity)
                .min(this::sort)
                .orElse((LivingEntity) null);
    }

    private Vec3d getTargetPoint(LivingEntity target, float tickDelta) {
        Vec3d renderPos = Utils.getRenderPos(target);
        double targetEyeHeight = target.getEyeHeight(target.getPose());

        double yFromFeet = 0;
        switch (targetHeight.get()) {
            case Eyes: yFromFeet = targetEyeHeight;     break;
            case Body: yFromFeet = targetEyeHeight / 2; break;
            case Feet: break;
            case OwnEyes:
                yFromFeet = Utils.getTrueEyeY(tickDelta) - renderPos.y;
                yFromFeet = MathUtils.clamp(yFromFeet, 0, targetEyeHeight);
                break;
        }
        return new Vec3d(renderPos.x, renderPos.y + yFromFeet, renderPos.z);
    }

    private float targetYaw(Vec3d targetPoint, float tickDelta) {
        Vec3d renderPos = Utils.getRenderPos();
        double deltaX = targetPoint.x - renderPos.x;
        double deltaZ = targetPoint.z - renderPos.z;
        return (float) (MathHelper.atan2(-deltaX, deltaZ) * MathUtils.RAD2DEG);
    }

    private float targetPitch(Vec3d targetPoint, float tickDelta) {
        Vec3d renderPos = Utils.getRenderPos();
        double deltaY = targetPoint.y - Utils.getTrueEyeY(tickDelta);
        double horizontalDist = MathUtils.length(targetPoint.x - renderPos.x, targetPoint.z - renderPos.z);
        return (float) (MathHelper.atan2(-deltaY, horizontalDist) * MathUtils.RAD2DEG);
    }

    private boolean shouldAttackEntity(LivingEntity entity) {
        return TargetUtils.shouldAttack(entity, null, false);
        // TODO: implement
        // return TargetUtils.shouldAttack(entity, targetEntities.get(), friends.get());
    }

    private int sort(LivingEntity e1, LivingEntity e2) {
        switch (priority.get()) {
            case LowestDistance:
                return Double.compare(e1.squaredDistanceTo(mc.player), e2.squaredDistanceTo(mc.player));
            case HighestDistance:
                return Double.compare(e2.squaredDistanceTo(mc.player), e1.squaredDistanceTo(mc.player));
            case LowestHealth:
                return Float.compare(e1.getHealth(), e2.getHealth());
            case HighestHealth:
                return Float.compare(e2.getHealth(), e1.getHealth());
            default: return 0;
        }
    }

}
