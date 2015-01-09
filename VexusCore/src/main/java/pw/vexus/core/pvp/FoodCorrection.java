package pw.vexus.core.pvp;

import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public final class FoodCorrection implements Listener {
    private final List<CPlayer> toggleList = new ArrayList<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        toggleList.remove(Core.getOnlinePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player entity = (Player) event.getEntity();
        if (entity.getFoodLevel()-event.getFoodLevel() < 0) return;
        CPlayer onlinePlayer = Core.getOnlinePlayer(entity);
        if (toggleList.contains(onlinePlayer)) {
            event.setCancelled(true);
            toggleList.remove(onlinePlayer);
        }
        else toggleList.add(onlinePlayer);
    }
}
