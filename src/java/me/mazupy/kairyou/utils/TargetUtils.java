package me.mazupy.kairyou.utils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import static me.mazupy.kairyou.Kairyou.*;

public class TargetUtils {

    public static boolean shouldAttack(LivingEntity entity, List<EntityType<?>> targetEntities, boolean attackFriends) {
        // TODO: remove null (testing for now)
        if (targetEntities != null && !targetEntities.contains(entity.getType())) return false;

        if (entity instanceof PlayerEntity)
            return shouldAttack((PlayerEntity) entity, attackFriends);

        return entity.isAlive();
    }

    public static boolean shouldAttack(PlayerEntity player, boolean attackFriends) {
        // Basic attackable test
        if (player.abilities.invulnerable || !player.isAlive() || player == MC.player) return false;

        return true;
        /* // TODO: implement: Friends
        return attackFriends || FriendManager.shouldAttack(player); */
    }

    public static boolean inExplosionRange(Entity entity, float explosionStrength) {
        return inRange(entity, 2 * explosionStrength);
    }

    public static boolean inRange(Entity entity, double range) {
        return MC.player.squaredDistanceTo(entity) <= MathUtils.sq(range);
    }
}
