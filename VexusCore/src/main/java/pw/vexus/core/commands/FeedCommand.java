package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;

@CommandPermission(value = "vexus.feed", isOpExempt = true)
public class FeedCommand extends VexusCommand {
    public FeedCommand() {
        super("feed");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {

    }
}
