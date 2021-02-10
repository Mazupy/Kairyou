package me.mazupy.kairyou.utils;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

import static me.mazupy.kairyou.Kairyou.*;

public abstract class Utils {

    public static final double EPSILON = 1e-7;
    public static final int MOUSE_DRAG_FLAG = -1;
    public static final int ITEM_SIZE = 16;
    public static final int TPS = 20; // TODO: unused
    // 2 constants taken from https://minecraft.gamepedia.com/Entity#Motion_of_entities
    public static final double PLAYER_GRAVITY = -0.08;
    public static final double PLAYER_DRAG = 0.98;

    private static long lastUIsound = 0;

    public static boolean notInGame() {
        return MC.world == null || MC.player == null;
    }

    public static boolean isUsingElytra() {
        return hasElyraEquipped() && MC.options.keyJump.isPressed() || MC.player.isFallFlying();
    }

    public static boolean hasElyraEquipped() {
        return MC.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA;
    }

    public static int width(String text) {
        return MC.textRenderer.getWidth(text);
    }

    public static int height(String text) {
        return MC.textRenderer.fontHeight;
    }

    public static void playClickSound() {
        if (System.currentTimeMillis() - lastUIsound > 40) {
            lastUIsound = System.currentTimeMillis();
            MC.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    public static int getPotionStrength(LivingEntity entity, StatusEffect effect) {
        int strength = 0;
        if (entity.getActiveStatusEffects().containsKey(effect)) strength = entity.getStatusEffect(effect).getAmplifier() + 1;
        return strength;
    }

	public static <T extends Enum<T>> T match(String string, T[] values) {
        for (T v : values) {
            if (string.equals(v.name())) return v;
        }
		return null;
    }

    // TODO: unused
    // public static <T extends Enum<T>> T closestMatch(String stringValue, T[] options) {
    //     if (options.length < 1) return null;
    //     int bestIndex = 0;
    //     int bestScore = Integer.MAX_VALUE;
    //     for (int i = 0; i < options.length; i++) {
    //         int score = Utils.levensheinDist(options[i].name(), stringValue);
    //         if (score < bestScore) {
    //             bestScore = score;
    //             bestIndex = i;
    //             if (score < 1) break;
    //         }
    //     }
    //     return options[bestIndex];
    // }

    // public static int levensheinDist(String s0, String s1) {
    //     if (s0.isEmpty()) return s1.length();
    //     if (s1.isEmpty()) return s0.length();
        
    //     int sub = levensheinDist(s0.substring(1), s1.substring(1)) + (s0.charAt(0) == s1.charAt(0) ? 0 : 1);
    //     if (sub < 2) return sub;
    //     int insert = levensheinDist(s0, s1.substring(1)) + 1;
    //     int del = levensheinDist(s0.substring(1), s1) + 1;

    //     return MathUtils.min(sub, insert, del);
    // }

    // TODO: unused
    // public static double getAttributute(Item item, EntityAttribute genericAttackDamage) {
    //     Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = item.getAttributeModifiers(EquipmentSlot.MAINHAND);
    //     if (attributeModifiers.containsKey(genericAttackDamage)) {
    //         return attributeModifiers.get(genericAttackDamage).iterator().next().getValue();
    //     }
    //     return 0;
    // }

    //! From old (broken) NoFall
    /* public static boolean hasSpaceBelow(int depth) {
        final float HALF_W = MC.player.getWidth() / 2;
        final int minX = (int) Math.floor(MC.player.getX() - HALF_W);
        final int minZ = (int) Math.floor(MC.player.getZ() - HALF_W);
        final int maxX = (int) Math.ceil(MC.player.getX() + HALF_W);
        final int maxZ = (int) Math.ceil(MC.player.getZ() + HALF_W);
        BlockPos pos;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int yOff = 0; yOff <= depth; yOff++) {
                    pos = new BlockPos(x, (int) MC.player.getY() - yOff, z);
                    if (!MC.world.getBlockState(pos).getMaterial().isReplaceable()) return false;
                }
            }
        }
        return true;
    } */

}
