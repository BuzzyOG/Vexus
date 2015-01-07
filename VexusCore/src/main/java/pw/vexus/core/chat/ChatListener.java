package pw.vexus.core.chat;

import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pw.vexus.core.VexusCore;

public final class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        CPlayer onlinePlayer = Core.getOnlinePlayer(event.getPlayer());
        try {
            ChatFilter.runFilters(onlinePlayer, event.getMessage());
        } catch (ChatFilter.ChatFilterException e) {
            event.setCancelled(true);
            onlinePlayer.sendMessage(VexusCore.getInstance().getFormat("chat-filtered", new String[]{"<error>", e.getMessage()}));
            onlinePlayer.playSoundForPlayer(Sound.NOTE_BASS);
        }
    }
}
