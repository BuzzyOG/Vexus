package pw.vexus.core.warp;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class WarpManager {
    private static final String X_KEY = "x", Y_KEY = "y", Z_KEY = "z", PITCH_KEY = "p", YAW_KEY = "ya", WORLD_KEY = "world";

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Location.class, new JsonSerializer<Location>() {
        @Override
        public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(X_KEY, jsonSerializationContext.serialize(location.getX()));
            jsonObject.add(Y_KEY, jsonSerializationContext.serialize(location.getY()));
            jsonObject.add(Z_KEY, jsonSerializationContext.serialize(location.getZ()));
            jsonObject.add(PITCH_KEY, jsonSerializationContext.serialize(location.getYaw()));
            jsonObject.add(YAW_KEY, jsonSerializationContext.serialize(location.getPitch()));
            jsonObject.add(WORLD_KEY, jsonSerializationContext.serialize(location.getWorld().getName()));
            return jsonObject;
        }
    }).registerTypeAdapter(Location.class, new JsonDeserializer<Location>() {
        @Override
        public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            return new Location(
                    Bukkit.getWorld(asJsonObject.get(WORLD_KEY).getAsString()),
                    asJsonObject.get(X_KEY).getAsDouble(),
                    asJsonObject.get(Y_KEY).getAsDouble(),
                    asJsonObject.get(Z_KEY).getAsDouble(),
                    asJsonObject.get(YAW_KEY).getAsFloat(),
                    asJsonObject.get(PITCH_KEY).getAsFloat()
            );
        }
    }).create();

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
        dataFile = GSON.fromJson(new FileReader(file), WarpDataFile.class);
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
        String s = GSON.toJson(dataFile);
        FileWriter fileWriter = new FileWriter(tiedFile);
        fileWriter.write(s);
        fileWriter.flush();
        fileWriter.close();
    }
}
