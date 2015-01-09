package pw.vexus.core.pvp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@EqualsAndHashCode(callSuper = false)
@Data
public final class PvPTagEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final CPlayer tagged;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
