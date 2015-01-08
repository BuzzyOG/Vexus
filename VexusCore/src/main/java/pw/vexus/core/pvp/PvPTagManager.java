package pw.vexus.core.pvp;

import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;
import pw.vexus.core.VexusCore;

import java.util.HashMap;

public final class PvPTagManager implements Listener {
    private final HashMap<CPlayer, PvPTag> tags = new HashMap<>();
    private final Integer tagTime;

    public PvPTagManager() {
        VexusCore.getInstance().registerListener(this);
        tagTime = VexusCore.getInstance().getConfig().getInt("pvp-tag-time")*20;
    }

    @EventHandler
    public void onPlayerPvPEngage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        CPlayer[] taggables = new CPlayer[]{Core.getOnlinePlayer((Player) event.getDamager()), Core.getOnlinePlayer((Player) event.getEntity())};
        for (CPlayer taggable : taggables) {
            if (tags.containsKey(taggable) || taggable.hasPermission("vexus.bypasspvptag")) continue;
            tags.put(taggable, new PvPTag(taggable));
            pvpTagStarted(taggable);
        }
    }

    public boolean isPlayerTagged(CPlayer player) {
        return tags.containsKey(player);
    }

    private void pvpTagEnded(CPlayer player) {
        tags.remove(player);
        player.playSoundForPlayer(Sound.NOTE_PIANO);
        player.sendMessage(VexusCore.getInstance().getFormat("pvp-tag-ended"));
    }

    private void pvpTagStarted(CPlayer player) {
        player.playSoundForPlayer(Sound.NOTE_BASS);
        player.sendMessage(VexusCore.getInstance().getFormat("pvp-tag-started"));
    }

    public final class PvPTag implements Listener, Runnable {
        private final CPlayer target;
        private BukkitTask pvpTagTask;

        public PvPTag(CPlayer target) {
            this.target = target;
            VexusCore.getInstance().registerListener(this);
            scheduleTag();
        }

        @EventHandler
        public void onPvP(EntityDamageByEntityEvent event) {
            Player bukkitPlayer = target.getBukkitPlayer();
            if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
            if (!(event.getDamager().equals(bukkitPlayer) || event.getEntity().equals(bukkitPlayer))) return;
            scheduleTag();
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            if (!event.getPlayer().equals(target.getBukkitPlayer())) return;
            event.getPlayer().setHealth(0); //kills the player
            endPvPTag();
        }

        @EventHandler
        public void onRespawn(PlayerDeathEvent event) {
            if (!event.getEntity().equals(target.getBukkitPlayer())) return;
            endPvPTag();
        }

        @Override
        public void run() {
            endPvPTag();
        }

        private void endPvPTag() {
            HandlerList.unregisterAll(this);
            pvpTagEnded(target);
            if (pvpTagTask != null) pvpTagTask.cancel();
        }

        private void scheduleTag() {
            if (pvpTagTask != null) pvpTagTask.cancel();
            pvpTagTask = Bukkit.getScheduler().runTaskLater(VexusCore.getInstance(), this, tagTime);
        }
    }
}
