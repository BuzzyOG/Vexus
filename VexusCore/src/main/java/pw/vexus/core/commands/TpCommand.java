package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandPermission("vexus.tp")
@CommandMeta(aliases = {"tp", "teleport"}, description = "Teleport to a player or teleport a player to another player")
public final class TpCommand extends VexusCommand {
    public TpCommand() {
        super("vexus:teleport");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length == 0) throw new ArgumentRequirementException("You need to specify a player to teleport to!");
        CPlayer target, teleportee;
        target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (args.length == 2) {
            teleportee = target;
            target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[1]);
        } else teleportee = player;
        if (teleportee == null || target == null) throw new ArgumentRequirementException("You have specified an invalid player!");

        teleportee.sendMessage(VexusCore.getInstance().getFormat("tpa-begin", new String[]{"<player>", target.getDisplayName()}));
        TeleMan.teleportPlayer(teleportee, target.getBukkitPlayer().getLocation());
    }
}
