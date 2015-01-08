package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CooldownUnexpiredException;
import org.bukkit.entity.Player;
import pw.vexus.core.CooldownManager;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
import pw.vexus.core.pvp.PvPTagException;

@CommandMeta(description = "Feed yourself or the target player.")
@CommandPermission(value = "vexus.feed", isOpExempt = true)
public final class FeedCommand extends VexusCommand {
    public FeedCommand() {
        super("feed");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else if (!player.hasPermission("vexus.feed.others")) throw new PermissionException("You do not have permission to feed others!");
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        CooldownManager.testForPermissibleCooldown("feed", player);

        if (target == player && VexusCore.getInstance().getPvpTagManager().isPlayerTagged(player)) throw new PvPTagException();

        Player tPlayer = target.getBukkitPlayer();
        tPlayer.setFoodLevel(20);
        player.sendMessage(VexusCore.getInstance().getFormat("player-fed", new String[]{"<player>", target.getName()}));
    }
}
