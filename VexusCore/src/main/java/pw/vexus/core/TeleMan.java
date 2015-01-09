package pw.vexus.core;

import lombok.NonNull;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.FriendlyException;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import pw.vexus.core.specials.EnderBarManager;
import pw.vexus.core.specials.EnderBarPriorities;

public final class TeleMan {
    public static void teleportPlayer(CPlayer player, Location newLocation) throws TeleportException {
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

    public static void teleportPlayer(CPlayer player, @NonNull Location target, Integer time) throws TeleportException {
        if (VexusCore.getInstance().getPvpTagManager().isPlayerTagged(player)) throw new TeleportException("You cannot teleport while you are PvP tagged!");
        if (time == 0) doTeleport(player, target);
        else {
            if (player.getBukkitPlayer().getFallDistance() > 2.0f) throw new TeleportException("You cannot teleport while falling this quickly!");
            new TeleportTask(player, target, time);
        }
    }

    private static void doTeleport(CPlayer player, Location location) {
        player.getBukkitPlayer().teleport(location);
        player.playSoundForPlayer(Sound.ENDERMAN_TELEPORT);
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

            task = Bukkit.getScheduler().runTaskTimer(VexusCore.getInstance(), this, 0, 20);
            secondsPassed = 0;

            VexusCore.getInstance().registerListener(this);
        }

        @Override
        public void run() {
            if (player.getBukkitPlayer().getLocation().distanceSquared(initialLocation) >= 4) {
                cancel();
                return;
            }
            int i = time - secondsPassed;
            String s = String.valueOf(i);
            EnderBarManager.setStateForID(player, EnderBarPriorities.TELEPORT.getPriority(), VexusCore.getInstance().getFormat("teleport-ender-bar", false, new String[]{"<remain>", s}), (float)Math.max((float)i/time, 0.01));
            if (secondsPassed == time) {
                doTeleport(player, target);
                task.cancel();
                clean();
                return;
            }
            player.sendMessage(VexusCore.getInstance().getFormat("teleport-wait", new String[]{"<remain>", s}));

            secondsPassed++;
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            if (!event.getPlayer().getUniqueId().equals(player.getUniqueIdentifier())) return;
            cancel();
            clean();
        }

        private void clean() {
            HandlerList.unregisterAll(this);
            EnderBarManager.clearId(player, EnderBarPriorities.TELEPORT.getPriority());
        }

        private void cancel() {
            task.cancel();
            if (player.isOnline()) player.sendMessage(VexusCore.getInstance().getFormat("teleport-cancel"));
            clean();
        }
    }

    public static final class TeleportException extends CommandException implements FriendlyException {
        public TeleportException(String message) {
            super(message);
        }

        @Override
        public String getFriendlyMessage(ModuleCommand command) {
            return ChatColor.RED + getMessage();
        }
    }
}
