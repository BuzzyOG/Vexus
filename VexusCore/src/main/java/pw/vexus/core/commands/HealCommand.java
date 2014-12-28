package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Heal yourself or the target player.")
@CommandPermission("vexus.heal")
public final class HealCommand extends VexusCommand {
    public HealCommand() {
        super("heal");
    }

    //TODO Needs cooldown!

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");
        Player tPlayer = target.getBukkitPlayer();
        tPlayer.setHealth(tPlayer.getMaxHealth());
        tPlayer.setFoodLevel(20);
        player.sendMessage(VexusCore.getInstance().getFormat("player-healed", new String[]{"<player>", target.getName()}));
    }
}
