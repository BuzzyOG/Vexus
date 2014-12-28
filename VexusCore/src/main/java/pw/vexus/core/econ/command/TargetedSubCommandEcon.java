package pw.vexus.core.econ.command;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.command.CommandSender;

abstract class TargetedSubCommandEcon extends ModuleCommand {
    protected TargetedSubCommandEcon(String name) {
        super(name);
    }

    abstract void performAction(CommandSender sender, CPlayer target, String[] args) throws CommandException;

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You must specify a target!");
        String[] args2 = new String[args.length-1];
        System.arraycopy(args, 1, args2, 0, args.length-1);
        CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target == null) throw new ArgumentRequirementException("The target you specified is not online!");
        performAction(sender, target, args2);
    }
}
