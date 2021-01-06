package me.mazupy.kairyou.utils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitchEntity;


import static net.minecraft.entity.attribute.EntityAttributes.*;
import static net.minecraft.entity.effect.StatusEffects.*;
import static me.mazupy.kairyou.Kairyou.*;

public abstract class DamageUtils {
    
    public static final float FALL_TOLERANCE = 3;

    public static float computeFallDamage(LivingEntity entity, float fallDistance, float damageMultiplier) {
        int jumpBoost = Utils.getPotionStrength(MC.player, JUMP_BOOST);
        int fallDamage = MathUtils.ceil((fallDistance - FALL_TOLERANCE - jumpBoost) * damageMultiplier);
        return fallDamage > 0 ? appliedDamage(entity, fallDamage, DamageSource.FALL) : 0;
    }

    public static boolean isFatal(float damage) {
        return MC.player.getHealth() + MC.player.getAbsorptionAmount() <= damage;
    }

    // Overload
    public static float appliedDamage(LivingEntity entity, float damage, DamageSource source) {
        if (source.isOutOfWorld()) return damage;

        if (source.isScaledWithDifficulty()) {
            damage = getDifficultyDamage(damage);
        }

        if (!source.bypassesArmor()) {
            float toughness = (float) entity.getAttributeValue(GENERIC_ARMOR_TOUGHNESS);
            damage = DamageUtil.getDamageLeft(damage, entity.getArmor(), toughness);
        }

        if (source.isFire() && entity.getActiveStatusEffects().containsKey(FIRE_RESISTANCE)) return 0;
        if (source.getMagic() && entity instanceof WitchEntity) damage *= 0.15;
        if (!source.isUnblockable()) {
            damage = resistanceReduction(entity, damage);
            damage = protectionReduction(entity, damage, source);
        }

        return Math.max(0, damage);
    }

    private static float getDifficultyDamage(float damage) {
        switch (MC.world.getDifficulty()) {
            case PEACEFUL:
                return 0;
            case EASY:
                return Math.min(damage / 2 + 1, damage);
            default:
            case NORMAL:
                return damage;
            case HARD:
                return damage * 1.5f;
        }
    }

    private static float protectionReduction(LivingEntity entity, float damage, DamageSource source) {
        int protLevel = EnchantmentHelper.getProtectionAmount(entity.getArmorItems(), source);
        return DamageUtil.getInflictedDamage(damage, protLevel);
    }

    private static float resistanceReduction(LivingEntity entity, float damage) {
        int lvl = Utils.getPotionStrength(entity, RESISTANCE);

        damage *= 1 - (lvl * 0.2);
        return Math.max(damage, 0);
    }
}
