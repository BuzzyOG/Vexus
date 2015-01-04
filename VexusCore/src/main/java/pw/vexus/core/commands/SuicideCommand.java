package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Commit suicide.")
@CommandPermission("vexus.suicide")
public final class SuicideCommand extends ModuleCommand {
    public SuicideCommand() {
        super("suicide");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        player.getBukkitPlayer().setHealth(0);
        for (CPlayer p : Core.getOnlinePlayers()) {
            p.sendMessage(VexusCore.getInstance().getFormat("suicide-message", new String[]{"<player>", player.getName()}));
        }
    }
}
