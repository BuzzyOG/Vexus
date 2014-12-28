package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"gm"}, description = "Change the gamemode of yourself or the targeted player.")
@CommandPermission("vexus.gamemode")
public class GamemodeCommand extends ModuleCommand {

    public GamemodeCommand() {
        super("gamemode");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) throw new ArgumentRequirementException("You did not specify a gamemode!");

        Integer mode;
        try {
            mode = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentRequirementException("Unknown gamemode!");
        }

        if (!(mode >= 0 && mode <= 2)) throw new ArgumentRequirementException("Unknown gamemode!");

        if (args.length == 1) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[1]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");

        Player tPlayer = target.getBukkitPlayer();
        tPlayer.setGameMode(GameMode.values()[mode]);

        player.sendMessage(VexusCore.getInstance().getFormat("gamemode-change", new String[]{"<gamemode>", tPlayer.getGameMode().name().toLowerCase()}, new String[]{"<player>", target.getName()}));
    }
}
