package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"clear"}, description = "Clear your or the target player's inventory.")
@CommandPermission("vexus.clear")
public final class ClearInvCommand extends VexusCommand {
    public ClearInvCommand() {
        super("ci");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else if (!player.hasPermission("vexus.clear.others")) throw new PermissionException("You do not have permission to clear other's inventories!");
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("That player you specified is invalid!");
        Player tPlayer = target.getBukkitPlayer();
        tPlayer.getInventory().clear();
        player.sendMessage(VexusCore.getInstance().getFormat("inventory-cleared", new String[]{"<target>", target.getName()}));
    }
}
