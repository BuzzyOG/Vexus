package pw.vexus.core;

import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public final class TeleMan {
    public static void teleportPlayer(CPlayer player, Location newLocation) {
        teleportPlayer(player, newLocation, resolveTeleportTimeForPlayer(player));
    }

    private static int resolveTeleportTimeForPlayer(CPlayer player) {
        if (player.getBukkitPlayer().isOp() || player.hasPermission("vexus.teletime.bypass")) return 0;
        FileConfiguration config = VexusCore.getInstance().getConfig();
        int time = config.getInt("teleport-wait-time");
        ConfigurationSection teleportPermissions = config.getConfigurationSection("teleportPermissions");
        for (String s : teleportPermissions.getKeys(false)) {
            if (!player.hasPermission("vexus.teletime." + s)) continue;
            int newTime = teleportPermissions.getInt(s);
            if (time > newTime) time = newTime;
        }
        return time;
    }

    public static void teleportPlayer(CPlayer player, Location newLocation, Integer time) {
        if (time == 0) player.getBukkitPlayer().teleport(newLocation);
        else new TeleportTask(player, newLocation, time);
    }

    private static class TeleportTask implements Runnable, Listener {
        private final CPlayer player;
        private final Location target;
        private final Location initialLocation;
        private final Integer time;

        private int secondsPassed;
        private BukkitTask task;

        private TeleportTask(CPlayer player, Location target, Integer time) {
            this.player = player;
            this.target = target;
            this.initialLocation = player.getBukkitPlayer().getLocation().clone();
            this.time = time;

            task = Bukkit.getScheduler().runTaskTimer(VexusCore.getInstance(), this, 20, 0);
            secondsPassed = 0;

            VexusCore.getInstance().registerListener(this);
        }


        @Override
        public void run() {
            if (player.getBukkitPlayer().getLocation().distanceSquared(initialLocation) >= 4) {
                cancel();
                return;
            }
            if (secondsPassed == time) {
                player.getBukkitPlayer().teleport(target);
                task.cancel();
                return;
            }
            player.sendMessage(VexusCore.getInstance().getFormat("teleport-wait", new String[]{"<remain>", String.valueOf(time-secondsPassed)}));
            secondsPassed++;
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            if (!event.getPlayer().getUniqueId().equals(player.getUniqueIdentifier())) return;
            cancel();
        }

        private void cancel() {
            task.cancel();
            if (player.isOnline()) player.sendMessage(VexusCore.getInstance().getFormat("teleport-cancel"));
        }
    }
}
