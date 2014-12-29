package pw.vexus.core.warp;

import com.google.common.collect.ImmutableList;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

public final class DelWarpCommand extends VexusCommand {
    public DelWarpCommand() {
        super("delwarp");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length == 0) throw new ArgumentRequirementException("You need to specify a warp!");
        String arg = args[0];
        WarpManager warpManager = VexusCore.getInstance().getWarpManager();
        ImmutableList<String> warps = warpManager.getWarps();
        if (!warps.contains(arg)) throw new ArgumentRequirementException("This warp does not exist!");
        warpManager.delWarp(arg);
        player.sendMessage(VexusCore.getInstance().getFormat("del-warp", new String[]{"<warp>", arg}));
    }
}
