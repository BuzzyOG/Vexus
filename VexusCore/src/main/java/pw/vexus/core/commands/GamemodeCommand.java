package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = "gm", description = "Change the gamemode of yourself or the targeted player.")
@CommandPermission("vexus.gamemode")
public class GamemodeCommand extends ModuleCommand {

    public GamemodeCommand() {
        super("gamemode");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        int mode = Integer.parseInt(args[0]);
        if (args.length == 1) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[1]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");

        Player tPlayer = target.getBukkitPlayer();
        if (mode == 0) tPlayer.setGameMode(GameMode.SURVIVAL);
        else if (mode == 1) tPlayer.setGameMode(GameMode.CREATIVE);
        else if (mode == 2) tPlayer.setGameMode(GameMode.ADVENTURE);
        else player.sendMessage(VexusCore.getInstance().getFormat("gamemode-invalid"));

        player.sendMessage(VexusCore.getInstance().getFormat("gamemode-change", new String[]{"<gamemode>", tPlayer.getGameMode().name().toLowerCase()}, new String[]{"<player>", target.getName()}));
    }
}
