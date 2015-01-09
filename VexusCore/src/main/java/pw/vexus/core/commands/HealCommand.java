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

import java.util.concurrent.TimeUnit;

@CommandMeta(description = "Heal yourself or the target player.")
@CommandPermission("vexus.heal")
public final class HealCommand extends VexusCommand {
    public HealCommand() {
        super("heal");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else if (!player.hasPermission("vexus.heal.others")) throw new PermissionException("You do not have permission to heal others!");
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        if (target == player && VexusCore.getInstance().getPvpTagManager().isPlayerTagged(player)) throw new PvPTagException();
        CooldownManager.testForPermissibleCooldown("heal", player);

        Player tPlayer = target.getBukkitPlayer();
        tPlayer.setHealth(tPlayer.getMaxHealth());
        tPlayer.setFoodLevel(20);
        player.sendMessage(VexusCore.getInstance().getFormat("player-healed", new String[]{"<player>", target.getName()}));
    }
}
