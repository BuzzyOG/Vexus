package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Feed yourself or the target player.")
@CommandPermission(value = "vexus.feed", isOpExempt = true)
public class FeedCommand extends VexusCommand {
    public FeedCommand() {
        super("feed");
    }

    //TODO Needs cooldown!

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");

        Player tPlayer = target.getBukkitPlayer();
        tPlayer.setFoodLevel(20);
        player.sendMessage(VexusCore.getInstance().getFormat("player-fed", new String[]{"<player>", target.getName()}));
    }
}
