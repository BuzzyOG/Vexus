package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"gm"}, description = "Change the gamemode of yourself or the targeted player.")
@CommandPermission("vexus.gamemode")
public final class GamemodeCommand extends ModuleCommand {

    public GamemodeCommand() {
        super("gamemode");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        CPlayer target;
        if (args.length == 0) throw new ArgumentRequirementException("You did not specify a gamemode!");
        if (args.length == 1) target = player;
        else target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[1]);

        if (target == null) throw new ArgumentRequirementException("The player you specified is invalid!");

        Player tPlayer = target.getBukkitPlayer();
        String s = args[0].toLowerCase();

        GameMode mode = null;
        try {
            //noinspection deprecation
            mode = GameMode.getByValue(Integer.parseInt(s));
        } catch (NumberFormatException ignored) {}
        if (mode == null) {
            for (GameMode gameMode : GameMode.values()) {
                if (gameMode.name().toLowerCase().startsWith(s)) {
                    mode = gameMode;
                    break;
                }
            }
        }
        if (mode == null) throw new ArgumentRequirementException("Unknown gamemode!");
        tPlayer.setGameMode(mode);

        player.sendMessage(VexusCore.getInstance().getFormat("gamemode-change", new String[]{"<gamemode>", tPlayer.getGameMode().name().toLowerCase()}, new String[]{"<player>", target.getName()}));
    }
}
