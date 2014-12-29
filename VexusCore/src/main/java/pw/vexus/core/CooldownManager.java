package pw.vexus.core;

import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CooldownUnexpiredException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public final class CooldownManager {
    public static void testForPermissibleCooldown(String key, CPlayer player) throws CooldownUnexpiredException {
        if (player.hasPermission("vexus.bypasscooldown") || player.hasPermission("vexus." + key + ".bypasscooldown") || player.getBukkitPlayer().isOp()) return;
        FileConfiguration config = VexusCore.getInstance().getConfig();
        if (!config.contains("cooldowns." + key)) return;
        ConfigurationSection configurationSection = config.getConfigurationSection("cooldowns." + key);
        Integer cooldown = null;
        for (String s : configurationSection.getKeys(false)) {
            int anInt = configurationSection.getInt(s);
            if (player.hasPermission("vexus." + key + "." + s) || player.hasPermission("vexus.all." + s) && (cooldown == null || cooldown > anInt))  cooldown = anInt;
        }
        if (cooldown == null) {
            if (configurationSection.contains("default")) cooldown = configurationSection.getInt("default");
            else return;
        }
        testForCooldown(key, player, cooldown.longValue());
    }

    public static void testForCooldown(String key, CPlayer player, Long length) throws CooldownUnexpiredException {
        String s = "vexusCooldown" + key;
        Long settingValue = player.getSettingValue(s, Long.class);
        if (settingValue != null && settingValue+(length*1000) > System.currentTimeMillis()) throw new CooldownUnexpiredException(settingValue+(length*1000)-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        player.storeSettingValue(s, System.currentTimeMillis());
    }
}
