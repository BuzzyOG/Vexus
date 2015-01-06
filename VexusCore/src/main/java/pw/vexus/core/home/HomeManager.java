package pw.vexus.core.home;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CPlayerConnectionListener;
import net.cogzmc.core.player.CPlayerJoinException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pw.vexus.core.VexusCore;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public final class HomeManager implements CPlayerConnectionListener {
    private Map<CPlayer, Map<String, Location>> homes = new HashMap<>();

    public HomeManager() {
        Core.getPlayerManager().registerCPlayerConnectionListener(this);
    }

    public Map<String, Location> getHomes(CPlayer player) {
        return homes.get(player);
    }
    public void setHome(String name, Location location, CPlayer player) {
        SetHomeEvent setHomeEvent = new SetHomeEvent(location, name, player);
        Bukkit.getPluginManager().callEvent(setHomeEvent);
        if (setHomeEvent.isCancelled()) throw new RuntimeException("Could not set home here!");
        homes.get(player).put(name, location);
    }
    public void delHome(String name, CPlayer player) {
        homes.get(player).remove(name);
    }

    @Override
    public void onPlayerLogin(CPlayer player, InetAddress address) throws CPlayerJoinException {
        //noinspection unchecked
        Map<String, Map<String, Object>> object = player.getSettingValue("vexusHomes", HashMap.class);
        if (object == null) object = new HashMap<>();
        Map<String, Location> homez = new HashMap<>();
        for (String s : object.keySet()) homez.put(s, getLocationForObject(object.get(s)));
        homes.put(player, homez);
    }

    @Override
    public void onPlayerDisconnect(CPlayer player) {
        savePlayer(player);
        homes.remove(player);
    }

    private void savePlayer(CPlayer player) {
        BasicDBObject object = new BasicDBObject();
        for (String s : homes.get(player).keySet()) object.put(s, getObjectForLocation(homes.get(player).get(s)));
        player.storeSettingValue("vexusHomes", object);
    }

    private final static String X_KEY = "x", Y_KEY = "y", Z_KEY = "z", PITCH_KEY = "p", YAW_KEY ="q", WORLD_KEY = "w";

    public static DBObject getObjectForLocation(Location location) {
        return BasicDBObjectBuilder.start().add(X_KEY, location.getX()).add(Y_KEY, location.getY()).add(Z_KEY, location.getZ()).add(PITCH_KEY, location.getPitch()).add(YAW_KEY, location.getYaw()).add(WORLD_KEY, location.getWorld().getName()).get();
    }

    public static Location getLocationForObject(Map object) {
        return new Location(Bukkit.getWorld((String) object.get(WORLD_KEY)), (double) object.get(X_KEY), (double) object.get(Y_KEY), (double) object.get(Z_KEY), ((Double) object.get(YAW_KEY)).floatValue(), ((Double) object.get(PITCH_KEY)).floatValue());
    }

    public static int getMaxHomes(CPlayer player) {
        if (player.hasPermission("vexus.homes.unlimited") || player.getBukkitPlayer().isOp()) return -1;
        FileConfiguration config = VexusCore.getInstance().getConfig();
        ConfigurationSection homes1 = config.getConfigurationSection("homes");
        for (String s : homes1.getKeys(false)) if (player.hasPermission("vexus.homes." + s)) return homes1.getInt(s);
        return config.getInt("default-homes", 1);
    }

    public void save() {
        for (CPlayer cPlayer : Core.getOnlinePlayers()) savePlayer(cPlayer);
    }
}
