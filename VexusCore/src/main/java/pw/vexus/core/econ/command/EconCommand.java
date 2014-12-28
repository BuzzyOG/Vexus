package pw.vexus.core.econ.command;

import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.modular.command.ModuleCommand;

@CommandMeta(aliases = {"eco"}, usage = "/eco [sub] [target] [args...]", description = "Manage the economy.")
@CommandPermission("vexus.econ.admin")
public final class EconCommand extends ModuleCommand {
    public EconCommand() {
        super("econ", new GiveCommand(), new ResetCommand());
    }

    @Override
    protected boolean isUsingSubCommandsOnly() {
        return true;
    }
}
