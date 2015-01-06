package pw.vexus.core.announcer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import pw.vexus.core.VexusCore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
public final class AnnouncerManager {
    private final static String SPAWN_REGIONS_KEY = "spawn-regions", INTERVAL_KEY = "announce-length";

    private final File file;

    private final List<String> announcements;
    private final List<ProtectedRegion> regionsEnderBar;
    @Getter private final String prefix;

    @Getter private int interval;

    public AnnouncerManager(File file) throws FileNotFoundException {
        this.file = file;
        announcements = new ArrayList<>();
        JsonArray jsonElements = VexusCore.getGSON().fromJson(new FileReader(file), JsonArray.class);
        for (JsonElement jsonElement : jsonElements) announcements.add(jsonElement.getAsString());

        WorldGuardPlugin wgPlugin = VexusCore.getInstance().getWgPlugin();
        regionsEnderBar = new ArrayList<>();
        ConfigurationSection regionWorlds = VexusCore.getInstance().getConfig().getConfigurationSection(SPAWN_REGIONS_KEY);
        for (String w : regionWorlds.getKeys(false)) {
            World world = Bukkit.getWorld(w);
            if (world == null) continue;
            for (String s : regionWorlds.getStringList(w)) {
                ProtectedRegion region = wgPlugin.getRegionManager(world).getRegion(s);
                if (region == null) continue;
                regionsEnderBar.add(region);
            }
        }

        this.interval = VexusCore.getInstance().getConfig().getInt(INTERVAL_KEY);
        this.prefix = VexusCore.getInstance().getFormat("prefix", false);

        new Announcer();
    }

    public void removeAnnouncement(int i) {
        announcements.remove(i);
    }

    public void addAnnouncement(String s) {
        announcements.add(s);
    }

    public String getAnnouncement(int i) {
        return announcements.get(i);
    }

    public ImmutableList<String> getAnnouncements() {
        return ImmutableList.copyOf(announcements);
    }

    public ImmutableList<ProtectedRegion> getRegions() {
        return ImmutableList.copyOf(regionsEnderBar);
    }

    public void setInterval(int interval) {
        VexusCore.getInstance().getConfig().set(INTERVAL_KEY, interval);
        this.interval = interval;
    }

    public void save() throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(VexusCore.getGSON().toJson(announcements));
        fileWriter.flush();
        fileWriter.close();
    }
}
