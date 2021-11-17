package me.mazupy.kairyou.setting.storage;

import net.minecraft.nbt.NbtCompound;

public interface ISavable {

    String tagName();

    NbtCompound toTag();
    
    void fromTag(NbtCompound tag);

}
