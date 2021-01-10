package me.mazupy.kairyou.setting.storage;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public abstract class StorageUtils {
    
    public static <T extends ISavable> CompoundTag toTag(List<T> list) {
        CompoundTag tag = new CompoundTag();
        for (T v : list) tag.put(v.tagName(), v.toTag());
        return tag;
    }

    public static <T extends ISavable> void fromTag(Tag tag, List<T> list) {
        CompoundTag cTag = (CompoundTag) tag;
        // no for each loop because the reference is required
        for (int i = 0; i < list.size(); i++) {
            list.get(i).fromTag((CompoundTag) cTag.get(list.get(i).tagName()));
        }
    }

}
