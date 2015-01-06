package pw.vexus.core.announcer.cmd;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

public final class IntervalCommand extends VexusCommand {
    public IntervalCommand() {
        super("interval");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) throw new ArgumentRequirementException("You need to specify an interval!");
        int i;
        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentRequirementException("You need to type a number.");
        }
        VexusCore.getInstance().getAnnouncerManager().setInterval(i);
        sender.sendMessage(VexusCore.getInstance().getFormat("announcer-interval-change", new String[]{"<interval>", String.valueOf(i)}));
    }
}
