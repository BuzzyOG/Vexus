package pw.vexus.core.econ.command;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.DatabaseConnectException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Resets a player's balance to the default value")
public final class ResetCommand extends TargetedSubCommandEcon {
    public ResetCommand() {
        super("reset");
    }

    @Override
    void performAction(CommandSender sender, CPlayer target, String[] args) throws CommandException {
        try {
            VexusCore.getInstance().getEconomyManager().resetBalance(target);
        } catch (DatabaseConnectException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in database! Check log", e);
        }
        sender.sendMessage(VexusCore.getInstance().getFormat("balance-reset", new String[]{"<target>", target.getName()}));
    }
}
