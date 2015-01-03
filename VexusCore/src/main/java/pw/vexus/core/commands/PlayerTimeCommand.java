package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"ptime"}, description = "Set the time for a specific client")
@CommandPermission("vexus.ptime")
public final class PlayerTimeCommand extends VexusCommand {
    public PlayerTimeCommand() {
        super("playertime");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length == 0) throw new ArgumentRequirementException("You must specify a time!");
        CPlayer target;
        if (args.length == 1) target = player;
        else if (!player.hasPermission("vexus.ptime.others")) throw new PermissionException("You do not have permission to change the player time of other players!");
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("You have not specified a valid target!");

        Player bukkitPlayer = target.getBukkitPlayer();
        String operatorArg = args.length == 1 ? args[0] : args[1];
        boolean newState = !operatorArg.equalsIgnoreCase("off");
        if (!newState) bukkitPlayer.resetPlayerTime();
        else {
            try {
                bukkitPlayer.setPlayerTime(Long.valueOf(operatorArg), false);
            } catch (Exception e) {
                throw new ArgumentRequirementException("You have specified an invalid time!");
            }
        }

        player.sendMessage(VexusCore.getInstance().getFormat("changed-ptime", new String[]{"<target>", target.getName()}, new String[]{"<state>", newState ? "changed" : "reset"}));
    }
}
