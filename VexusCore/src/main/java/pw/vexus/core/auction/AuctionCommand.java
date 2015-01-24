package pw.vexus.core.auction;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;

@CommandMeta(description = "Start a new auction!", aliases = {"auc"})
@CommandPermission("vexus.auction")
public class AuctionCommand extends VexusCommand {
    public AuctionCommand() {
        super("auction");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {

    }
}
