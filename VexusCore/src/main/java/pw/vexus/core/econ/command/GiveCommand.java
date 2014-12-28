package pw.vexus.core.econ.command;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.DatabaseConnectException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCore;

public final class GiveCommand extends TargetedSubCommandEcon{
    public GiveCommand() {
        super("give");
    }

    @Override
    void performAction(CommandSender sender, CPlayer target, String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You must supply an amount!");
        Double amount;
        try {
            amount = Double.valueOf(args[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentRequirementException("You have specified an invalid number!");
        }
        try {
            VexusCore.getInstance().getEconomyManager().modifyBalance(target, amount);
        } catch (DatabaseConnectException e) {
            throw new ArgumentRequirementException("Error connecting to the database to modify the player's balance!");
        }
        sender.sendMessage(VexusCore.getInstance().getFormat("give-success", new String[]{"<amount>", amount.toString()}, new String[]{"<target>", target.getDisplayName()}));
    }
}
