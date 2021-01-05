package me.mazupy.kairyou.utils;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

import static me.mazupy.kairyou.Kairyou.*;

public abstract class Utils {

    public static final int TPS = 20; // TODO: unused
    public static final double EPSILON = 1e-7;
    public static final int MOUSE_DRAG_FLAG = -1;
    // 2 constants taken from https://minecraft.gamepedia.com/Entity#Motion_of_entities
    public static final double PLAYER_GRAVITY = -0.08;
    public static final double PLAYER_DRAG = 0.98;

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
        MC.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
    }

    public static int getPotionStrength(LivingEntity entity, StatusEffect effect) {
        int strength = 0;
        if (entity.getActiveStatusEffects().containsKey(effect)) strength = entity.getStatusEffect(effect).getAmplifier() + 1;
        return strength;
    }

    // TODO: unused
    // public static double getTrueEyeY() {
    //     return MC.player.getY() + MC.player.getEyeHeight(MC.player.getPose());
    // }

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
