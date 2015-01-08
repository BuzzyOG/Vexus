package pw.vexus.core.chat;

import net.cogzmc.core.Core;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatFeaturesListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Core.getOnlinePlayers().stream()
                .filter(cPlayer -> event.getMessage().contains(cPlayer.getName()) || event.getMessage().contains(cPlayer.getDisplayName()))
                .forEach(cPlayer -> cPlayer.playSoundForPlayer(Sound.ORB_PICKUP, 50f, 2f));
    }
}
