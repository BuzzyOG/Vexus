package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
@CommandPermission("vexus.tpa")
public final class TpaCommand extends VexusCommand {
    public TpaCommand() {
        super("tpa");
    }

    @Override
    protected void handleCommand(final CPlayer player, final String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You have not specified a player!");
        final CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target == null || !VanishCommand.canSee(target, player)) throw new ArgumentRequirementException("The player you have specified does not exist!");
        Confirmer.confirm("Do you want to allow " + player.getDisplayName() + " to teleport to you?", target, (result, pl) -> {
            if (!player.isOnline()) return;
            if (!result) {
                player.sendMessage(VexusCore.getInstance().getFormat("tpa-denied"));
                pl.sendMessage(VexusCore.getInstance().getFormat("tpa-deny"));
                return;
            }
            try {
                TeleMan.teleportPlayer(player, target.getBukkitPlayer().getLocation());
            } catch (TeleMan.TeleportException e) {
                TpaCommand.this.handleCommandException(e, args, player.getBukkitPlayer());
                return;
            }
            player.sendMessage(VexusCore.getInstance().getFormat("tpa-begin", new String[]{"<player>", target.getDisplayName()}));
            pl.sendMessage(VexusCore.getInstance().getFormat("tpa-accept"));
        });
        player.sendMessage(VexusCore.getInstance().getFormat("tpa-sent", new String[]{"<player>", target.getDisplayName()}));
    }
}
