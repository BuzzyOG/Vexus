package pw.vexus.core.announcer.cmd;

import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import pw.vexus.core.VexusCommand;

@CommandMeta(description = "Allows you to manage the announcer!", aliases = {"announce", "ann"})
@CommandPermission("vexus.announcer")
public final class AnnouncerCommand extends VexusCommand {
    public AnnouncerCommand() {
        super("announcer", new AddCommand(), new ListCommand(), new RemoveCommand(), new IntervalCommand());
    }

    @Override
    protected boolean isUsingSubCommandsOnly() {
        return true;
    }
}
