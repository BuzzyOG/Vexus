package pw.vexus.core.pvp;

import com.google.common.base.CaseFormat;
import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pw.vexus.core.VexusCore;
import pw.vexus.core.specials.EnderBarManager;
import pw.vexus.core.specials.EnderBarPriorities;

public final class EngageEnderBar implements Listener {
    @EventHandler
    public void onPvPUntag(PvPUntagEvent event) {
        EnderBarManager.clearId(event.getTagged(), EnderBarPriorities.PVP.getPriority());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        CPlayer one = Core.getOnlinePlayer((Player) event.getEntity()), two = Core.getOnlinePlayer(((Player) event.getDamager()));
        PvPTagManager pvpTagManager = VexusCore.getInstance().getPvpTagManager();
        if (pvpTagManager.isPlayerTagged(one)) updateForPlayer(one, two);
        if (pvpTagManager.isPlayerTagged(two)) updateForPlayer(two, one);
    }

    private static void updateForPlayer(CPlayer player, CPlayer opponent) {
        Player bukkitPlayer = opponent.getBukkitPlayer();
        EnderBarManager.setStateForID(player, EnderBarPriorities.PVP.getPriority(), getTitleForEvent(opponent), (float)bukkitPlayer.getHealth()/(float)bukkitPlayer.getMaxHealth());
    }

    private static String getTitleForEvent(CPlayer opponent) {
        Player bukkitPlayer = opponent.getBukkitPlayer();
        return VexusCore.getInstance().getFormat("pvp-ender-bar", false,
                new String[]{"<health>", String.format("%02.01f", bukkitPlayer.getHealth())},
                new String[]{"<holding>", bukkitPlayer.getItemInHand().getType().name().replaceAll("_", " ").toLowerCase()},
                new String[]{"<name>", opponent.getDisplayName()}
        );
    }
}
