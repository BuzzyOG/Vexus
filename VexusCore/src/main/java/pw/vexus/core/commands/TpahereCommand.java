package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandPermission("vexus.tpahere")
public final class TpahereCommand extends VexusCommand {
    public TpahereCommand() {
        super("tpahere");
    }

    @Override
    protected void handleCommand(final CPlayer sender, final String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You have not specified a player!");
        final CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target == null || !VanishCommand.canSee(target, sender)) throw new ArgumentRequirementException("The player you have specified does not exist!");
        Confirmer.confirm("Do you want to teleport to " + sender.getDisplayName() + "'s current location?", target, new Confirmer.ConfirmerCallback() {
            @Override
            public void call(boolean result, CPlayer pl) {
                if (!sender.isOnline()) return;
                if (!result) {
                    sender.sendMessage(VexusCore.getInstance().getFormat("tpa-denied"));
                    target.sendMessage(VexusCore.getInstance().getFormat("tpa-deny"));
                    return;
                }
                try {
                    TeleMan.teleportPlayer(target, sender.getBukkitPlayer().getLocation());
                } catch (TeleMan.TeleportException e) {
                    TpahereCommand.this.handleCommandException(e, args, sender.getBukkitPlayer());
                    return;
                }
                target.sendMessage(VexusCore.getInstance().getFormat("tpa-begin", new String[]{"<player>", sender.getDisplayName()}));
                target.sendMessage(VexusCore.getInstance().getFormat("tpa-accept"));
                sender.sendMessage(VexusCore.getInstance().getFormat("tpa-accepted"));
            }
        });
        sender.sendMessage(VexusCore.getInstance().getFormat("tpa-sent", new String[]{"<player>", target.getDisplayName()}));
    }
}
