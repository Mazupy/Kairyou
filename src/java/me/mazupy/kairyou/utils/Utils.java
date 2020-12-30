package me.mazupy.kairyou.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;

import me.mazupy.kairyou.Kairyou;

public abstract class Utils {

    public static final int TPS = 20; // TODO: unused
    public static final double EPSILON = 1e-7;
    // 2 constants taken from https://minecraft.gamepedia.com/Entity#Motion_of_entities
    public static final double PLAYER_GRAVITY = 0.08;
    public static final double PLAYER_DRAG = 0.02;

    private static final MinecraftClient mc = Kairyou.MC;

    public static boolean notInGame() {
        return mc.world == null || mc.player == null;
    }

    public static boolean isUsingElytra() {
        return hasElyraEquipped() && mc.options.keyJump.isPressed() || mc.player.isFallFlying();
    }

    public static boolean hasElyraEquipped() {
        return mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA;
    }

    //! From old (broken) NoFall
    /* public static boolean hasSpaceBelow(int depth) {
        final float HALF_W = mc.player.getWidth() / 2;
        final int minX = (int) Math.floor(mc.player.getX() - HALF_W);
        final int minZ = (int) Math.floor(mc.player.getZ() - HALF_W);
        final int maxX = (int) Math.ceil(mc.player.getX() + HALF_W);
        final int maxZ = (int) Math.ceil(mc.player.getZ() + HALF_W);
        BlockPos pos;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int yOff = 0; yOff <= depth; yOff++) {
                    pos = new BlockPos(x, (int) mc.player.getY() - yOff, z);
                    if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) return false;
                }
            }
        }
        return true;
    } */

}
