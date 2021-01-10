package me.mazupy.kairyou.setting.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;

public abstract class StorageManager {

    private static final String TEMP_FILE_ENDING = ".tmp";
    private static final String SAVE_FILE_ENDING = ".nbt";
    private static final String modulesName = "modules";

    public static void save() {
        Collection<Module> modules = ModuleManager.INSTANCE.getModules();

        if (!save(modules, modulesName))
            error("saving", modulesName);
    }

    public static void load() {
        Collection<Module> modules = ModuleManager.INSTANCE.getModules();

        if (!load(modules, modulesName))
            error("loading", modulesName);
    }

    private static <T extends ISavable> boolean save(Collection<T> savables, String fileName) {
        CompoundTag tag = new CompoundTag();
        for (T savable : savables) tag.put(savable.tagName(), savable.toTag());
        
        File folder = getKairyouFolder();
        if (!folder.isDirectory()) return false;

        File tmpFile = new File(folder, fileName + TEMP_FILE_ENDING);
        File nbtFile = new File(folder, fileName + SAVE_FILE_ENDING);
        if (!saveCompoundTag(tag, tmpFile)) return false;
        if (nbtFile.exists()) nbtFile.delete();
        return tmpFile.renameTo(nbtFile);
    }

    private static <T extends ISavable> boolean load(Collection<T> loadables, String fileName) {
        File folder = getKairyouFolder();
        if (!folder.isDirectory()) return false;

        File nbtFile = new File(folder, fileName + SAVE_FILE_ENDING);
        if (nbtFile.isDirectory()) return false;
        if (!nbtFile.exists()) return true;

        CompoundTag tag = loadCompoundTag(nbtFile);
        if (tag == null) return false;

        try {
            for (T loadable : loadables) {
                loadable.fromTag((CompoundTag) tag.get(loadable.tagName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static File getKairyouFolder() {
        File kairyouFolder = new File("config/kairyou/" + Kairyou.VERSION);
        kairyouFolder.mkdirs();
        return kairyouFolder;
    }

    private static boolean saveCompoundTag(CompoundTag tag, File file) {
        try {
            NbtIo.writeCompressed(tag, file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static CompoundTag loadCompoundTag(File file) {
        try {
            CompoundTag tag = NbtIo.readCompressed(file);
            return tag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void error(String during, String fileName) {
        System.out.println("[ERROR]: Error while " + during + " " + fileName + "; old save should be retained");
    }

}
