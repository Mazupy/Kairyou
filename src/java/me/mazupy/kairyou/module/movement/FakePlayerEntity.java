package me.mazupy.kairyou.module.movement;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.math.Vec3d;

import static me.mazupy.kairyou.Kairyou.MC;

public class FakePlayerEntity extends ClientPlayerEntity {
    
    public FakePlayerEntity(ClientPlayNetworkHandler networkHandler, Vec3d pos, float yaw, float pitch) {
        super(MC, MC.world, networkHandler, new StatHandler(), new ClientRecipeBook(), false, false);
        
        setPosition(pos.x, pos.y, pos.z);
        setRotation(yaw, pitch);
        input = new KeyboardInput(MC.options);
        NbtCompound data = new NbtCompound();
        MC.player.abilities.writeNbt(data);
        this.abilities.readNbt(data);
    }
    
    @Override
    public void tick() {
        updatePositionAndAngles(getX(), getY(), getZ(), MC.player.yaw, MC.player.pitch);
        super.tick();
    }
    
    @Override
    protected boolean isCamera() {
        return true;
    }
}
