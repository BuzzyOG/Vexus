package pw.vexus.core.econ.command;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.DatabaseConnectException;
import org.bukkit.Sound;
import pw.vexus.core.VexusCore;
import pw.vexus.core.econ.EconomyManager;

@CommandMeta(description = "Used to send funds to players!", aliases = {"send"})
@CommandPermission("vexus.econ.pay")
public final class PayCommand extends ModuleCommand {
    public PayCommand() {
        super("pay");
    }

    @Override
    protected void handleCommand(CPlayer sender, String[] args) throws CommandException {
        if (args.length < 2) throw new ArgumentRequirementException("You have not specified a player to pay and an amount!");
        CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        Double amount = Double.valueOf(args[1]);
        if (amount < 0) throw new ArgumentRequirementException("The value must be positive!");
        VexusCore instance = VexusCore.getInstance();
        EconomyManager economyManager = instance.getEconomyManager();
        if (economyManager.getBalance(sender) < amount) throw new ArgumentRequirementException("You do not have enough funds for this!");
        try {
            economyManager.modifyBalance(target, amount);
            economyManager.modifyBalance(sender, -amount);
        } catch (DatabaseConnectException e) {
            e.printStackTrace();
            throw new ArgumentRequirementException("There was an error modifying the balance of one player. Please provide this error to an admin as soon as possible. " + e.getMessage());
        }
        target.sendMessage(instance.getFormat("payment-from", new String[]{"<sender>", sender.getDisplayName()}, new String[]{"<amount>", String.valueOf(amount)}));
        sender.sendMessage(instance.getFormat("payment-to", new String[]{"<target>", target.getDisplayName()}, new String[]{"<amount>", String.valueOf(amount)}));
        target.playSoundForPlayer(Sound.LEVEL_UP);
    }
}
