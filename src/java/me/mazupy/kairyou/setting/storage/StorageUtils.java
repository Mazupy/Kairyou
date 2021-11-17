package me.mazupy.kairyou.setting.storage;

import java.util.List;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public abstract class StorageUtils {
    
    public static <T extends ISavable> NbtCompound toTag(List<T> list) {
        NbtCompound tag = new NbtCompound();
        for (T v : list) tag.put(v.tagName(), v.toTag());
        return tag;
    }

    public static <T extends ISavable> void fromTag(NbtElement tag, List<T> list) {
        NbtCompound cTag = (NbtCompound) tag;
        // no for each loop because the reference is required
        for (int i = 0; i < list.size(); i++) {
            list.get(i).fromTag((NbtCompound) cTag.get(list.get(i).tagName()));
        }
    }

}
