package pw.vexus.core.home;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@EqualsAndHashCode(callSuper = false)
@Data
public final class SetHomeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final Location location;
    private final String name;
    private final CPlayer player;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
