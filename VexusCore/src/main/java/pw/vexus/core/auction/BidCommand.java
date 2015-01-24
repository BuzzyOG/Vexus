package pw.vexus.core.auction;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;

@CommandMeta(description = "Bid on an active auction")
@CommandPermission("vexus.bid")
public class BidCommand extends VexusCommand {
    public BidCommand() {
        super("bid");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {

    }
}
