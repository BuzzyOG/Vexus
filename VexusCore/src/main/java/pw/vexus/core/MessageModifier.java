package pw.vexus.core;

import net.cogzmc.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.*;

public final class MessageModifier implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (Core.getOnlinePlayer(p).isFirstJoin()) event.setJoinMessage(VexusCore.getInstance().getFormat("player-first-join", new String[]{"<player>", p.getName()}));
        else event.setJoinMessage(VexusCore.getInstance().getFormat("player-join", new String[]{"<player>", p.getName()}));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(VexusCore.getInstance().getFormat("player-leave", new String[]{"<player>", event.getPlayer().getName()}));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(VexusCore.getInstance().getFormat("death-message", new String[]{"<message>", event.getDeathMessage()}));
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage(VexusCore.getInstance().getFormat("kick-message", new String[]{"<player>", event.getPlayer().getName()}));
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        List<String> motds = VexusCore.getInstance().getConfig().getStringList("motds");
        if (motds.size() == 0) return;
        event.setMotd(ChatColor.translateAlternateColorCodes('&', motds.get(Core.getRandom().nextInt(motds.size()))));
    }

}
