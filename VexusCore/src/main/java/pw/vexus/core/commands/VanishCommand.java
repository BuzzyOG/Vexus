package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pw.vexus.core.VexusCommand;

public final class VanishCommand extends VexusCommand {

    public static final class VanishListener implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            CPlayer onlinePlayer = Core.getOnlinePlayer(event.getPlayer());
            Boolean vanished = onlinePlayer.getSettingValue("vanished", Boolean.class);
            if (vanished == null || !vanished) return;

        }
    }

    public static void vanish()
}
