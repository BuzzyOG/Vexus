package pw.vexus.core.econ.command;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCore;
import pw.vexus.core.econ.EconomyManager;

@CommandMeta(description = "Check your balance or the balance of another user!", aliases = {"money", "bal", "b"}, usage = "/bal [target]")
@CommandPermission("vexus.econ.balance")
public final class BalanceCommand extends ModuleCommand {
    public BalanceCommand() {
        super("balance");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        VexusCore instance = VexusCore.getInstance();
        CPlayer target;
        if (args.length > 0) target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        else if (sender instanceof Player) target = Core.getOnlinePlayer((Player) sender);
        else throw new ArgumentRequirementException("You have not specified enough arguments! (You must specify a player if you are not one!)");
        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        EconomyManager economyManager = instance.getEconomyManager();
        sender.sendMessage(instance.getFormat("balance-response",
                new String[]{"<target>", target.getDisplayName()},
                new String[]{"<balance>", String.valueOf(economyManager.getBalance(target))})
        );
    }
}
