package me.mazupy.kairyou.module.movement;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.AxisCycleDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Utils;

public class CollisionReplica { // TODO: player replica entity can probably be replaced / omitted
    
    public static boolean canClip(Entity entity) {
        return entity.fallDistance < entity.stepHeight && !isBlockSpaceEmpty(entity, 0, entity.fallDistance - entity.stepHeight, 0);
    }

    public static Vec3d adjustMovementForSneaking(Entity entity, Vec3d movement) {
        double x = movement.x;
        double z = movement.z;
        final double STEP = 0.05;
        while (x != 0 && isBlockSpaceEmpty(entity, x, -entity.stepHeight, 0)) {
            if (-STEP < x && x < STEP) x = 0;
            else if (x > 0) x -= STEP;
            else x += STEP;
        }
        while (z != 0 && isBlockSpaceEmpty(entity, 0, -entity.stepHeight, z)) {
            if (-STEP < z && z < STEP) z = 0;
            else if (z > 0) z -= STEP;
            else z += STEP;
        }
        return new Vec3d(x, movement.y, z);
    }
    
    private static boolean isBlockSpaceEmpty(Entity entity, double xOffset, double yOffset, double zOffset) {
        final Box box = entity.getBoundingBox().offset(xOffset, yOffset, zOffset);
        return entity.world.getBlockCollisions(entity, box).allMatch(VoxelShape::isEmpty);
    }
    
    public static Vec3d adjustMovementForCollisions(Entity entity, Box boundingBox, final Vec3d movement) { // TODO: unpack and clean up from this point forward (copied from minecraft yarn 1.16.3)
        final VoxelShape lv3 = entity.world.getWorldBorder().asVoxelShape();
        final Stream<VoxelShape> stream = VoxelShapes.matchesAnywhere(lv3, VoxelShapes.cuboid(boundingBox.contract(Utils.EPSILON)), BooleanBiFunction.AND) ? Stream.empty() : Stream.of(lv3);
        final Stream<VoxelShape> stream2 = entity.world.getEntityCollisions(entity, boundingBox.stretch(movement), (Predicate<Entity>)(arg -> true));
        final ReusableStream<VoxelShape> lv4 = new ReusableStream<VoxelShape>(Stream.concat(stream2, stream));
        final Vec3d lv5 = movement.lengthSquared() == 0 ? movement : adjustMovementForCollisions(entity, movement, boundingBox, entity.world, lv4);
        final boolean bl = movement.x != lv5.x;
        final boolean bl2 = movement.y != lv5.y;
        final boolean bl3 = movement.z != lv5.z;
        final boolean bl4 = entity.isOnGround() || (bl2 && movement.y < 0);
        if (entity.stepHeight > 0f && bl4 && (bl || bl3)) {
            Vec3d lv6 = adjustMovementForCollisions(entity, new Vec3d(movement.x, entity.stepHeight, movement.z), boundingBox, entity.world, lv4);
            final Vec3d lv7 = adjustMovementForCollisions(entity, new Vec3d(0, entity.stepHeight, 0), boundingBox.stretch(movement.x, 0, movement.z), entity.world, lv4);
            if (lv7.y < entity.stepHeight) {
                final Vec3d lv8 = adjustMovementForCollisions(entity, new Vec3d(movement.x, 0, movement.z), boundingBox.offset(lv7), entity.world, lv4).add(lv7);
                if (squaredHorizontalLength(lv8) > squaredHorizontalLength(lv6)) {
                    lv6 = lv8;
                }
            }
            if (squaredHorizontalLength(lv6) > squaredHorizontalLength(lv5)) {
                return lv6.add(adjustMovementForCollisions(entity, new Vec3d(0, -lv6.y + movement.y, 0), boundingBox.offset(lv6), entity.world, lv4));
            }
        }
        return lv5;
    }
    
    private static double squaredHorizontalLength(final Vec3d vector) {
        return MathUtils.lengthSq(vector.x, vector.z);
    }
    
    private static Vec3d adjustMovementForCollisions(@Nullable final Entity entity, final Vec3d movement, final Box entityBoundingBox, final World world, final ReusableStream<VoxelShape> collisions) {
        final boolean noX = movement.x == 0;
        final boolean noY = movement.y == 0;
        final boolean noZ = movement.z == 0;
        if ((noX && noY) || (noX && noZ) || (noY && noZ)) {
            return adjustSingleAxisMovementForCollisions(movement, entityBoundingBox, world, collisions);
        }
        final ReusableStream<VoxelShape> lv = new ReusableStream<VoxelShape>(Stream.concat(collisions.stream(), world.getBlockCollisions(entity, entityBoundingBox.stretch(movement))));
        return adjustMovementForCollisions(movement, entityBoundingBox, lv);
    }
    
    private static Vec3d adjustMovementForCollisions(final Vec3d movement, Box entityBoundingBox, final ReusableStream<VoxelShape> collisions) {
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        if (y != 0) {
            y = VoxelShapes.calculateMaxOffset(Direction.Axis.Y, entityBoundingBox, collisions.stream(), y);
            entityBoundingBox = entityBoundingBox.offset(0, y, 0);
        }
        final boolean zDominated = Math.abs(x) < Math.abs(z);
        if (zDominated) {
            z = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, entityBoundingBox, collisions.stream(), z);
            entityBoundingBox = entityBoundingBox.offset(0, 0, z);
        }
        if (x != 0) {
            x = VoxelShapes.calculateMaxOffset(Direction.Axis.X, entityBoundingBox, collisions.stream(), x);
            if (!zDominated && x != 0) {
                entityBoundingBox = entityBoundingBox.offset(x, 0, 0);
            }
        }
        if (!zDominated && z != 0) {
            z = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, entityBoundingBox, collisions.stream(), z);
        }
        return new Vec3d(x, y, z);
    }
    
    private static Vec3d adjustSingleAxisMovementForCollisions(final Vec3d movement, Box entityBoundingBox, final WorldView world, final ReusableStream<VoxelShape> collisions) {
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        if (y != 0) {
            y = calculatePushVelocity(entityBoundingBox, world, y, Direction.Axis.Y, collisions.stream());
            entityBoundingBox = entityBoundingBox.offset(0, y, 0);
        }
        final boolean zDominated = Math.abs(x) < Math.abs(z);
        if (zDominated) {
            z = calculatePushVelocity(entityBoundingBox, world, z, Direction.Axis.Z, collisions.stream());
            entityBoundingBox = entityBoundingBox.offset(0, 0, z);
        }
        if (x != 0) {
            x = calculatePushVelocity(entityBoundingBox, world, x, Direction.Axis.X, collisions.stream());
            if (!zDominated && x != 0) {
                entityBoundingBox = entityBoundingBox.offset(x, 0, 0);
            }
        }
        if (!zDominated && z != 0) {
            z = calculatePushVelocity(entityBoundingBox, world, z, Direction.Axis.Z, collisions.stream());
        }
        return new Vec3d(x, y, z);
    }

    private static double calculatePushVelocity(Box box, WorldView world, double initial, Direction.Axis axis, Stream<VoxelShape> shapes) {
        AxisCycleDirection direction = AxisCycleDirection.between(axis, Direction.Axis.Z);
        if (!(box.getXLength() < Utils.EPSILON) && !(box.getYLength() < Utils.EPSILON) && !(box.getZLength() < Utils.EPSILON)) {
            if (Math.abs(initial) < Utils.EPSILON) return 0;
            
            AxisCycleDirection axisCycleDirection = direction.opposite();
            Direction.Axis axis1 = axisCycleDirection.cycle(Direction.Axis.X);
            Direction.Axis axis2 = axisCycleDirection.cycle(Direction.Axis.Y);
            Direction.Axis axis3 = axisCycleDirection.cycle(Direction.Axis.Z);
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int axis1min = MathUtils.floor(box.getMin(axis1) - Utils.EPSILON) - 1;
            int axis1max = MathUtils.floor(box.getMax(axis1) + Utils.EPSILON) + 1;
            int axis2min = MathUtils.floor(box.getMin(axis2) - Utils.EPSILON) - 1;
            int axis2max = MathUtils.floor(box.getMax(axis2) + Utils.EPSILON) + 1;
            double axis3min = box.getMin(axis3) - Utils.EPSILON;
            double axis3max = box.getMax(axis3) + Utils.EPSILON;
            boolean positiveInitial = initial > 0;
            int m = positiveInitial ? MathUtils.floor(box.getMax(axis3) - Utils.EPSILON) - 1 : MathUtils.floor(box.getMin(axis3) + Utils.EPSILON) + 1;
            int n = clamp(initial, axis3min, axis3max);
            int o = positiveInitial ? 1 : -1;
            int p = m;

            while (true) {
                if (positiveInitial) {
                    if (p > n) break;
                } else if (p < n) break;

                for (int i1 = axis1min; i1 <= axis1max; ++i1) {
                    for (int i2 = axis2min; i2 <= axis2max; ++i2) {
                        int s = 0;
                        if (i1 == axis1min || i1 == axis1max) {
                            ++s;
                        }

                        if (i2 == axis2min || i2 == axis2max) {
                            ++s;
                        }

                        if (p == m || p == n) {
                            ++s;
                        }

                        if (s < 3) {
                            mutable.set(axisCycleDirection, i1, i2, p);
                            BlockState blockState = world.getBlockState(mutable);
                            if ((s != 1 || blockState.exceedsCube()) && (s != 2 || blockState.isOf(Blocks.MOVING_PISTON))) {
                                initial = blockState.getCollisionShape(world, mutable, ShapeContext.absent())
                                        .calculateMaxDistance(axis3, box.offset(-mutable.getX(), -mutable.getY(), -mutable.getZ()), initial);
                                
                                if (Math.abs(initial) < Utils.EPSILON) return 0;

                                n = clamp(initial, axis3min, axis3max);
                            }
                        }
                    }
                }

                p += o;
            }

            double[] ds = new double[] { initial };
            shapes.forEach((voxelShape) -> {
                ds[0] = voxelShape.calculateMaxDistance(axis3, box, ds[0]);
            });
            return ds[0];
        } else {
            return initial;
        }
    }
    
    private static int clamp(double value, double min, double max) {
        return value > 0 ? MathUtils.floor(max + value) + 1 : MathUtils.floor(min + value) - 1;
    }
}
