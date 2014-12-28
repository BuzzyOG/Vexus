package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"flight"}, description = "Activate flight for yourself or the targeted player")
@CommandPermission("vexus.fly")
public final class FlyCommand extends ModuleCommand {
    public FlyCommand() {
        super("fly");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        Player targetPlayer = target.getBukkitPlayer();
        targetPlayer.setAllowFlight(!targetPlayer.getAllowFlight());
        player.sendMessage(VexusCore.getInstance().getFormat("flight-changed", new String[]{"<player>", target.getDisplayName()}, new String[]{"<state>", targetPlayer.getAllowFlight() ? "enabled" : "disabled"}));
    }
}
