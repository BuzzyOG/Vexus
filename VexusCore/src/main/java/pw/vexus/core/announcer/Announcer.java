package pw.vexus.core.announcer;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.vexus.core.VexusCore;
import pw.vexus.core.specials.EnderBarManager;
import pw.vexus.core.specials.EnderBarPriorities;

import java.util.*;

public final class Announcer implements Listener {
    private final List<CPlayer> activatedPlayers = new ArrayList<>();

    private int announcementIndex = 0;

    public Announcer() {
        Bukkit.getScheduler().runTaskTimer(VexusCore.getInstance(), new AnnounceBarTask(), 1L, 1L);
        VexusCore.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        VexusCore instance = VexusCore.getInstance();
        WorldGuardPlugin wgPlugin = instance.getWgPlugin();
        Player player = event.getPlayer();
        RegionManager regionManager = wgPlugin.getRegionManager(player.getWorld());
        ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(player.getLocation());
        //If they are in a region that announcer should be in ender bar, this will be false, otherwise it'll be true
        boolean inRegion = !Collections.disjoint(applicableRegions.getRegions(), instance.getAnnouncerManager().getRegions());
        CPlayer onlinePlayer = Core.getOnlinePlayer(player);
        //In the instance of a player being in a region, or not, while at the same time having the same state for being in our array, we don't really care. We only mean to align the states at all times.
        if (inRegion == activatedPlayers.contains(onlinePlayer)) return;

        if (inRegion) activatedPlayers.add(onlinePlayer);
        else {
            activatedPlayers.remove(onlinePlayer);
            EnderBarManager.clearId(onlinePlayer, EnderBarPriorities.ANNOUNCER_BAR.getPriority());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        activatedPlayers.remove(Core.getOnlinePlayer(event.getPlayer()));
    }

    private final class AnnounceBarTask implements Runnable {
        private String announcement;
        private int ticksPassed;
        private boolean started = false;

        @Override
        public void run() {
            AnnouncerManager announcerManager = VexusCore.getInstance().getAnnouncerManager();
            if (announcerManager.getAnnouncements().size() == 0) return;
            if (!started) {
                loadAnnouncement();
                announce();
                started = true;
            }
            int ticksPerAnnounce = announcerManager.getInterval();
            float v = (float) (ticksPerAnnounce - ticksPassed) / (float) ticksPerAnnounce;
            for (CPlayer activatedPlayer : activatedPlayers)
                EnderBarManager.setStateForID(activatedPlayer, EnderBarPriorities.ANNOUNCER_BAR.getPriority(), announcement, v);
            ticksPassed++;
            if (ticksPassed >= ticksPerAnnounce) nextAnnounce();
        }

        private void nextAnnounce() {
            announcementIndex = (announcementIndex + 1) % VexusCore.getInstance().getAnnouncerManager().getAnnouncements().size();
            ticksPassed = 0;
            loadAnnouncement();
            announce();
        }

        private void loadAnnouncement() {
            AnnouncerManager announcerManager = VexusCore.getInstance().getAnnouncerManager();
            announcement = ChatColor.translateAlternateColorCodes('&', announcerManager.getPrefix() + announcerManager.getAnnouncement(announcementIndex));
        }

        private void announce() {
            HashSet<CPlayer> cPlayers = new HashSet<>(Core.getOnlinePlayers());
            cPlayers.removeAll(activatedPlayers);
            for (CPlayer cPlayer : cPlayers) cPlayer.sendMessage(announcement);
            for (CPlayer activatedPlayer : activatedPlayers) activatedPlayer.playSoundForPlayer(Sound.CLICK);
        }
    }
}
