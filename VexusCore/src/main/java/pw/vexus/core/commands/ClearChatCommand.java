package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandPermission("vexus.cc")
@CommandMeta(aliases = {"clearchat"}, description = "Clear the chat.")
public class ClearChatCommand extends VexusCommand {

    public ClearChatCommand() {
        super("cc");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        for (CPlayer p : Core.getPlayerManager()) {
            p.clearChatAll();
            p.sendMessage(VexusCore.getInstance().getFormat("chat-cleared", new String[]{"<player>", player.getName()}));
        }
    }
}
