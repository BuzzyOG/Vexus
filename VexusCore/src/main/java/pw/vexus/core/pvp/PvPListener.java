package pw.vexus.core.pvp;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import net.cogzmc.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import pw.vexus.core.VexusCore;

import java.util.Arrays;

public final class PvPListener implements Listener {
    private boolean failed = false;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Hologram hologram = HologramsAPI.createHologram(VexusCore.getInstance(), player.getLocation().clone().add(0, 1, 0));
        double damage;
        if (!failed) {
            try {
                damage = (double) event.getClass().getMethod("getFinalDamage").invoke(event);
            } catch (Exception e) {
                if (e instanceof NoSuchMethodException) failed = true;
                damage = event.getDamage();
            }
        } else damage = event.getDamage();
        char[] hearts = new char[(int) Math.round(damage/2)];
        Arrays.fill(hearts, '♥');
        hologram.insertTextLine(0, ChatColor.DARK_RED.toString() + "-" + new String(hearts));
        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.setVisibleByDefault(false);
        visibilityManager.showTo((Player) event.getDamager());
        visibilityManager.showTo(player);
        Bukkit.getScheduler().runTaskLater(VexusCore.getInstance(), hologram::delete, 60L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        VexusCore instance = VexusCore.getInstance();
        Hologram hologram = HologramsAPI.createHologram(instance, event.getEntity().getLocation());
        hologram.insertTextLine(0, instance.getFormat("tombstone.rip", false, new String[]{"<player>", Core.getOnlinePlayer(event.getEntity()).getDisplayName()}));
        //TODO expand upon this
        Bukkit.getScheduler().runTaskLater(VexusCore.getInstance(), hologram::delete, 200L);
    }
}
