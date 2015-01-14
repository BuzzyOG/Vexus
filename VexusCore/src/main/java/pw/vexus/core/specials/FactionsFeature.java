package pw.vexus.core.specials;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsHomeTeleport;
import com.massivecraft.massivecore.ps.PS;
import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCore;
import pw.vexus.core.home.SetHomeEvent;

import java.util.HashMap;
import java.util.Map;

public final class FactionsFeature implements Listener {
    private final Map<CPlayer, Faction> displayingFactions = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        PS chunkFrom = PS.valueOf(event.getFrom()).getChunk(true);
        PS chunkTo = PS.valueOf(event.getTo()).getChunk(true);
        if (chunkFrom.equals(chunkTo)) return;

        Player player = event.getPlayer();

        CPlayer onlinePlayer = Core.getOnlinePlayer(player);
        MPlayer mPlayer = MPlayer.get(player);

        Faction factionTo = BoardColl.get().getFactionAt(chunkTo);
        Faction faction = displayingFactions.get(onlinePlayer);
        if (faction != null && factionTo.equals(faction)) return;

        EnderBarManager.setStateForID(
                onlinePlayer,
                EnderBarPriorities.REGION_INFO.getPriority(),
                VexusCore.getInstance().getFormat("region-info", false,
                        new String[]{"<name>", factionTo.getName(mPlayer)},
                        new String[]{"<desc>", factionTo.getDescription()}),
                ((float) (factionTo.getPower() / ((float) factionTo.getPowerMax()))));

        displayingFactions.put(onlinePlayer, factionTo);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        displayingFactions.remove(Core.getOnlinePlayer(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHomeTeleport(EventFactionsHomeTeleport event) {
        event.setCancelled(true);
        Location location = event.getMSender().getFaction().getHome().asBukkitLocation();
        try {
            TeleMan.teleportPlayer(Core.getOnlinePlayer(event.getMSender().getPlayer()), location);
        } catch (TeleMan.TeleportException e) {
            event.getMSender().sendMessage(VexusCore.getInstance().getFormat("error", new String[]{"<error>", e.getMessage()}));
        }
    }

    @EventHandler
    public void onSethome(SetHomeEvent event) {
        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(event.getLocation()));
        MPlayer mPlayer = MPlayer.get(event.getPlayer().getBukkitPlayer());
        if (!factionAt.isNormal() || mPlayer.getFaction().equals(factionAt)) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(VexusCore.getInstance().getFormat("error", new String[]{"<error>", "You cannot set home in this area!"}));
    }
}
