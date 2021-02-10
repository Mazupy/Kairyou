package me.mazupy.kairyou.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import me.mazupy.kairyou.event.TickEvent;
import me.mazupy.kairyou.event.WorldRenderEvent;
import me.mazupy.kairyou.module.Category;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.rendering.OverlayProjector;
import me.mazupy.kairyou.setting.KeybindSetting;
import me.mazupy.kairyou.setting.input.Keybind;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Utils;

@Module.Info(name = "MovementSpoofer", description = "Tries to predict and simulate movement patterns", category = Category.Movement)
public class MovementSpoofer extends Module { // TODO: test module

    private final KeybindSetting reset = new KeybindSetting("Reset ghost", new Keybind(this::reset));

    private double x = 8;
    private double y = 64;
    private double z = 8;
    private float width = 0.6f;
    private float height = 1.8f;
    private double vX = 0;
    private double vY = 0;
    private double vZ = 0;
    private double pX = x;
    private double pY = y;
    private double pZ = z;
    private boolean onGround = true;
    private int jumpingCooldown = 0;

    public MovementSpoofer() {
        settings.add(reset);
    }

    @EventHandler
    private final Listener<TickEvent> onTick = new Listener<>(event -> {
        // Get acceleration speeds
        float flyingSpeed = 0.02f;
        float movementSpeed = 0.1f;
        if (mc.player.isSprinting()) {
            flyingSpeed *= 1.3f;
            movementSpeed *= 1.3;
        }
        // Get movement from input
        final Input input = mc.player.input;
        float forwards = input.pressingForward ? 1f : 0f;
        if (input.pressingBack) forwards--;
        float sideways = input.pressingLeft ? 1f : 0f;
        if (input.pressingRight) sideways--;
        height = 1.8f;
        if (mc.player.isInSneakingPose()) {
            height = 1.5f;
            forwards *= 0.3f;
            sideways *= 0.3f;
        }
        if (mc.player.isUsingItem()) {
            forwards *= 0.2f;
            sideways *= 0.2f;
        }
        if (mc.player.abilities.flying) {
            int vertical = input.jumping ? 1 : 0;
            if (input.sneaking) vertical--;
            vY += vertical * flyingSpeed * 3;
        }
        // Stopping
        if (Math.abs(vX) < 0.003) vX = 0;
        if (Math.abs(vY) < 0.003) vY = 0;
        if (Math.abs(vZ) < 0.003) vZ = 0;
        // Jumping
        if (mc.player.input.jumping) {
            if (--jumpingCooldown < 1 && onGround) {
                jumpingCooldown = 10;
                float jumpMult = getBlock().getJumpVelocityMultiplier();
                if (jumpMult == 1f) jumpMult = getBlockBelow(0.5 + Utils.EPSILON).getJumpVelocityMultiplier();
                vY = 0.42f * jumpMult;
                if (mc.player.isSprinting()) {
                    vX -= MathUtils.sinDeg(mc.player.yaw) * 0.2f;
                    vZ += MathUtils.cosDeg(mc.player.yaw) * 0.2f;
                }
            }
        } else jumpingCooldown = 0;
        // Get speed modifiers
        final float slipperiness = getBlockBelow(0.5 + Utils.EPSILON).getSlipperiness();
        final float horizontalMult = onGround ? slipperiness * 0.91f : 0.91f;
        final float speed = onGround ? movementSpeed * MathUtils.cube(0.6f / slipperiness) : flyingSpeed;
        // Convert input movement to velocity
        double forwardsD = forwards * Utils.PLAYER_DRAG;
        double sidewaysD = sideways * Utils.PLAYER_DRAG;
        double lSq = MathUtils.lengthSq(forwardsD, sidewaysD);
        if (lSq > 1) {
            double len = MathHelper.sqrt(lSq);
            forwardsD /= len;
            sidewaysD /= len;
        }
        float xAxis = (float) MathUtils.sinDeg(mc.player.yaw);
        float zAxis = (float) MathUtils.cosDeg(mc.player.yaw);
        double aX = sidewaysD * zAxis - forwardsD * xAxis;
        double aZ = forwardsD * zAxis + sidewaysD * xAxis;
        vX += aX * speed;
        vZ += aZ * speed;
        ClientPlayerEntity playerReplica = new ClientPlayerEntity(mc, mc.world, mc.getNetworkHandler(), mc.player.getStatHandler(), mc.player.getRecipeBook(), mc.player.isSneaking(), mc.player.isSprinting());
        playerReplica.copyFrom(mc.player);
        playerReplica.updatePosition(x, y, z);
        playerReplica.setOnGround(onGround);
        Vec3d movement = new Vec3d(vX, vY, vZ);
        // Sneaking
        if (playerReplica.isSneaking() && !playerReplica.abilities.flying)
            movement = CollisionReplica.adjustMovementForSneaking(playerReplica, movement);
        // Collision
        Vec3d collisionMovement = CollisionReplica.adjustMovementForCollisions(playerReplica, movement);
        // Apply velocity
        pX = x;
        pY = y;
        pZ = z;
        x += collisionMovement.x;
        y += collisionMovement.y;
        z += collisionMovement.z;
        if (Math.abs(vX - collisionMovement.x) > Utils.EPSILON) vX = 0;
        onGround = false;
        if (Math.abs(vY - collisionMovement.y) > Utils.EPSILON) {
            if (vY < 0) onGround = true;
            vY = 0;
        }
        if (Math.abs(vZ - collisionMovement.z) > Utils.EPSILON) vZ = 0;
        // Block movement multiplier
        float mult = getBlock().getVelocityMultiplier();
        if (mult == 1f) mult = getBlockBelow(0.5 + Utils.EPSILON).getVelocityMultiplier();
        vX *= mult;
        vZ *= mult;
        // Gravity
        vY += Utils.PLAYER_GRAVITY;
        // Global movement multiplier
        vX *= horizontalMult;
        vZ *= horizontalMult;
        vY *= Utils.PLAYER_DRAG;
    });

    private Block getBlockBelow(double yOffset) {
        BlockPos pos = new BlockPos(x, y - yOffset, z);
        return mc.world.getBlockState(pos).getBlock();
    }

    private Block getBlock() {
        return getBlockBelow(0);
    }

    @EventHandler
    private final Listener<WorldRenderEvent> onRender = new Listener<> (event -> {
        double X = MathHelper.lerp(event.tickDelta, pX, x);
        double Y = MathHelper.lerp(event.tickDelta, pY, y);
        double Z = MathHelper.lerp(event.tickDelta, pZ, z);
        OverlayProjector.lineBox(X, Y, Z, width, height, Color.Blue);
    });

    @Override
    protected void onActivate() {
        reset();
    }

    private void reset() {
        if (Utils.notInGame()) return;
        x = mc.player.getX() + 2;
        y = mc.player.getY();
        z = mc.player.getZ() + 2;
        width = mc.player.getWidth();
        height = mc.player.getHeight();
        vX = mc.player.getVelocity().x;
        vY = mc.player.getVelocity().y;
        vZ = mc.player.getVelocity().z;
    }

}
