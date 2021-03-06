package pw.vexus.core.econ.command;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.DatabaseConnectException;
import org.bukkit.Sound;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
import pw.vexus.core.commands.Confirmer;
import pw.vexus.core.commands.VanishCommand;
import pw.vexus.core.econ.EconomyManager;

@CommandMeta(description = "Used to send funds to players!", aliases = {"send"})
@CommandPermission("vexus.econ.pay")
public final class PayCommand extends VexusCommand {
    public PayCommand() {
        super("pay");
    }

    @Override
    protected void handleCommand(CPlayer sender, String[] args) throws CommandException {
        if (args.length < 2) throw new ArgumentRequirementException("You have not specified a player to pay and an amount!");
        CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target == null || !VanishCommand.canSee(target, sender)) throw new ArgumentRequirementException("The player you specified is invalid!");
        Double amount = Double.valueOf(args[1]);
        if (amount < 0) throw new ArgumentRequirementException("The value must be positive!");
        if (amount < 0.01) throw new ArgumentRequirementException("You cannot send this little.");
        if (VexusCore.getInstance().getEconomyManager().getBalance(sender) < amount) throw new ArgumentRequirementException("You do not have enough funds for this!");
        if (amount >= 1000) {
            Confirmer.confirm("Are you sure you want to send " + EconomyManager.format(amount) + " to " + target.getDisplayName(), sender, (result, player) -> {
                if (!result) sender.sendMessage(VexusCore.getInstance().getFormat("transaction-cancelled"));
                else try {
                    completePayment(target, sender, amount);
                } catch (ArgumentRequirementException e) {
                    handleCommandException(e, args, sender.getBukkitPlayer());
                }
            });
            return;
        }
        completePayment(target, sender, amount);
    }

    private void completePayment(CPlayer target, CPlayer sender, Double amount) throws ArgumentRequirementException {
        VexusCore instance = VexusCore.getInstance();
        EconomyManager economyManager = instance.getEconomyManager();
        try {
            economyManager.modifyBalance(target, amount);
            economyManager.modifyBalance(sender, -amount);
        } catch (DatabaseConnectException e) {
            e.printStackTrace();
            throw new ArgumentRequirementException("There was an error modifying the balance of one player. Please provide this error to an admin as soon as possible. " + e.getMessage());
        }
        String format = EconomyManager.format(amount);
        target.sendMessage(instance.getFormat("payment-from", new String[]{"<sender>", sender.getDisplayName()}, new String[]{"<amount>", format}));
        sender.sendMessage(instance.getFormat("payment-to", new String[]{"<target>", target.getDisplayName()}, new String[]{"<amount>", format}));
        target.playSoundForPlayer(Sound.LEVEL_UP);
    }
}
