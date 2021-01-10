package me.mazupy.kairyou.setting.storage;

import net.minecraft.nbt.CompoundTag;

public interface ISavable {

    String tagName();

    CompoundTag toTag();
    
    void fromTag(CompoundTag tag);

}
