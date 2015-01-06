package pw.vexus.core.warp;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pw.vexus.core.VexusCore;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class WarpManager {
    private final WarpDataFile dataFile;
    private final File tiedFile;

    public WarpManager(File file) throws IOException {
        tiedFile = file;
        if (!file.exists()) {
            dataFile = new WarpDataFile();
            dataFile.warps = new HashMap<>();
            file.createNewFile();
            return;
        }
        dataFile = VexusCore.getGSON().fromJson(new FileReader(file), WarpDataFile.class);
    }

    private static class WarpDataFile {
        private Map<String, Location> warps;
    }

    public void setWarp(String name, Location location) {
        dataFile.warps.put(name.toLowerCase(), location);
    }

    public void delWarp(String name) {
        dataFile.warps.remove(name);
    }

    public Location getWarp(String name) {
        return dataFile.warps.get(name.toLowerCase());
    }

    public ImmutableList<String> getWarps() {
        return ImmutableList.copyOf(dataFile.warps.keySet());
    }

    public void save() throws IOException {
        String s = VexusCore.getGSON().toJson(dataFile);
        FileWriter fileWriter = new FileWriter(tiedFile);
        fileWriter.write(s);
        fileWriter.flush();
        fileWriter.close();
    }
}
