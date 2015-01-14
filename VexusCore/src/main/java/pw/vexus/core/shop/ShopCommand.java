package pw.vexus.core.shop;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;

abstract class ShopCommand extends VexusCommand {
    protected ShopCommand(String name) {
        super(name);
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {

    }
}
