package pw.vexus.core.pvp;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.FriendlyException;
import net.cogzmc.core.modular.command.ModuleCommand;
import org.bukkit.ChatColor;

public final class PvPTagException extends CommandException implements FriendlyException {
    public PvPTagException() {
        super("You are currently PvP tagged and cannot do that!");
    }

    @Override
    public String getFriendlyMessage(ModuleCommand command) {
        return ChatColor.RED + getMessage();
    }
}
