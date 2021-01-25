package me.mazupy.kairyou.module.movement;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import me.mazupy.kairyou.utils.Utils;

public class CollisionReplica { // TODO: unpack and clean up (copied from minecraft yarn 1.16.3)
    public static Vec3d adjustMovementForCollisions(Entity entity, final Vec3d movement) {
        final Box lv = entity.getBoundingBox();
        final ShapeContext lv2 = ShapeContext.of(entity);
        final VoxelShape lv3 = entity.world.getWorldBorder().asVoxelShape();
        final Stream<VoxelShape> stream = VoxelShapes.matchesAnywhere(lv3, VoxelShapes.cuboid(lv.contract(Utils.EPSILON)), BooleanBiFunction.AND) ? Stream.empty() : Stream.of(lv3);
        final Stream<VoxelShape> stream2 = entity.world.getEntityCollisions(entity, lv.stretch(movement), (Predicate<Entity>)(arg -> true));
        final ReusableStream<VoxelShape> lv4 = new ReusableStream<VoxelShape>(Stream.concat(stream2, stream));
        final Vec3d lv5 = movement.lengthSquared() == 0 ? movement : adjustMovementForCollisions(entity, movement, lv, entity.world, lv2, lv4);
        final boolean bl = movement.x != lv5.x;
        final boolean bl2 = movement.y != lv5.y;
        final boolean bl3 = movement.z != lv5.z;
        final boolean bl4 = entity.isOnGround() || (bl2 && movement.y < 0);
        if (entity.stepHeight > 0f && bl4 && (bl || bl3)) {
            Vec3d lv6 = adjustMovementForCollisions(entity, new Vec3d(movement.x, entity.stepHeight, movement.z), lv, entity.world, lv2, lv4);
            final Vec3d lv7 = adjustMovementForCollisions(entity, new Vec3d(0, entity.stepHeight, 0), lv.stretch(movement.x, 0, movement.z), entity.world, lv2, lv4);
            if (lv7.y < entity.stepHeight) {
                final Vec3d lv8 = adjustMovementForCollisions(entity, new Vec3d(movement.x, 0, movement.z), lv.offset(lv7), entity.world, lv2, lv4).add(lv7);
                if (squaredHorizontalLength(lv8) > squaredHorizontalLength(lv6)) {
                    lv6 = lv8;
                }
            }
            if (squaredHorizontalLength(lv6) > squaredHorizontalLength(lv5)) {
                return lv6.add(adjustMovementForCollisions(entity, new Vec3d(0, -lv6.y + movement.y, 0), lv.offset(lv6), entity.world, lv2, lv4));
            }
        }
        return lv5;
    }
    
    private static double squaredHorizontalLength(final Vec3d vector) {
        return vector.x * vector.x + vector.z * vector.z;
    }
    
    private static Vec3d adjustMovementForCollisions(@Nullable final Entity entity, final Vec3d movement, final Box entityBoundingBox, final World world, final ShapeContext context, final ReusableStream<VoxelShape> collisions) {
        final boolean bl = movement.x == 0;
        final boolean bl2 = movement.y == 0;
        final boolean bl3 = movement.z == 0;
        if ((bl && bl2) || (bl && bl3) || (bl2 && bl3)) {
            return adjustSingleAxisMovementForCollisions(movement, entityBoundingBox, world, context, collisions);
        }
        final ReusableStream<VoxelShape> lv = new ReusableStream<VoxelShape>(Stream.concat(collisions.stream(), world.getBlockCollisions(entity, entityBoundingBox.stretch(movement))));
        return adjustMovementForCollisions(movement, entityBoundingBox, lv);
    }
    
    private static Vec3d adjustMovementForCollisions(final Vec3d movement, Box entityBoundingBox, final ReusableStream<VoxelShape> collisions) {
        double d = movement.x;
        double e = movement.y;
        double f = movement.z;
        if (e != 0) {
            e = VoxelShapes.calculateMaxOffset(Direction.Axis.Y, entityBoundingBox, collisions.stream(), e);
            if (e != 0) {
                entityBoundingBox = entityBoundingBox.offset(0, e, 0);
            }
        }
        final boolean bl = Math.abs(d) < Math.abs(f);
        if (bl && f != 0) {
            f = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, entityBoundingBox, collisions.stream(), f);
            if (f != 0) {
                entityBoundingBox = entityBoundingBox.offset(0, 0, f);
            }
        }
        if (d != 0) {
            d = VoxelShapes.calculateMaxOffset(Direction.Axis.X, entityBoundingBox, collisions.stream(), d);
            if (!bl && d != 0) {
                entityBoundingBox = entityBoundingBox.offset(d, 0, 0);
            }
        }
        if (!bl && f != 0) {
            f = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, entityBoundingBox, collisions.stream(), f);
        }
        return new Vec3d(d, e, f);
    }
    
    private static Vec3d adjustSingleAxisMovementForCollisions(final Vec3d movement, Box entityBoundingBox, final WorldView world, final ShapeContext context, final ReusableStream<VoxelShape> collisions) {
        double d = movement.x;
        double e = movement.y;
        double f = movement.z;
        if (e != 0) {
            e = VoxelShapes.calculatePushVelocity(Direction.Axis.Y, entityBoundingBox, world, e, context, collisions.stream());
            if (e != 0) {
                entityBoundingBox = entityBoundingBox.offset(0, e, 0);
            }
        }
        final boolean bl = Math.abs(d) < Math.abs(f);
        if (bl && f != 0) {
            f = VoxelShapes.calculatePushVelocity(Direction.Axis.Z, entityBoundingBox, world, f, context, collisions.stream());
            if (f != 0) {
                entityBoundingBox = entityBoundingBox.offset(0, 0, f);
            }
        }
        if (d != 0) {
            d = VoxelShapes.calculatePushVelocity(Direction.Axis.X, entityBoundingBox, world, d, context, collisions.stream());
            if (!bl && d != 0) {
                entityBoundingBox = entityBoundingBox.offset(d, 0, 0);
            }
        }
        if (!bl && f != 0) {
            f = VoxelShapes.calculatePushVelocity(Direction.Axis.Z, entityBoundingBox, world, f, context, collisions.stream());
        }
        return new Vec3d(d, e, f);
    }

    public static Vec3d adjustMovementForSneaking(Entity entity, Vec3d movement) {
        if (!canClip(entity)) return movement;
        double d = movement.x;
        double e = movement.z;
        final double f = 0.05;
        while (d != 0 && entity.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(d, -entity.stepHeight, 0))) {
            if (d < f && d >= -f) {
                d = 0;
            } else if (d > 0) {
                d -= f;
            } else {
                d += f;
            }
        }
        while (e != 0 && entity.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(0, -entity.stepHeight, e))) {
            if (e < f && e >= -f) {
                e = 0;
            } else if (e > 0) {
                e -= f;
            } else {
                e += f;
            }
        }
        while (d != 0 && e != 0 && entity.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(d, -entity.stepHeight, e))) {
            if (d < f && d >= -f) {
                d = 0;
            } else if (d > 0) {
                d -= f;
            } else {
                d += f;
            }
            if (e < f && e >= -f) {
                e = 0;
            } else if (e > 0) {
                e -= f;
            } else {
                e += f;
            }
        }
        return new Vec3d(d, movement.y, e);
    }
    
    private static boolean canClip(Entity entity) {
        return entity.isOnGround() || (entity.fallDistance < entity.stepHeight && 
                !entity.world.isSpaceEmpty(entity, entity.getBoundingBox().offset(0, entity.fallDistance - entity.stepHeight, 0)));
    }
}
