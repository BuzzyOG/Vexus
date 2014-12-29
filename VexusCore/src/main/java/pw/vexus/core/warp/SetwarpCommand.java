package pw.vexus.core.warp;

import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Set a warp by name")
@CommandPermission("vexus.setwarp")
public final class SetwarpCommand extends VexusCommand {
    public SetwarpCommand() {
        super("setwarp");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You have not specified a name for the warp!");
        VexusCore.getInstance().getWarpManager().setWarp(args[0], player.getBukkitPlayer().getLocation());
        player.sendMessage(VexusCore.getInstance().getFormat("set-warp", new String[]{"<warp>", args[0]}));
    }
}
