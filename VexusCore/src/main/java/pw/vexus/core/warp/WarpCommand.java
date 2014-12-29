package pw.vexus.core.warp;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Warps you to a specified warp")
@CommandPermission("vexus.warp")
public final class WarpCommand extends VexusCommand {
    public WarpCommand() {
        super("warp");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        ImmutableList<String> warps = VexusCore.getInstance().getWarpManager().getWarps();
        if (args.length == 0) {
            player.sendMessage(VexusCore.getInstance().getFormat("warp-list", new String[]{"<warps>", warps.size() == 0 ? "None" : Joiner.on(',').join(warps)}));
            return;
        }
        String arg = args[0].toLowerCase();
        if (!warps.contains(arg)) throw new ArgumentRequirementException("That warp doesn't exist!");
        player.sendMessage(VexusCore.getInstance().getFormat("warp", new String[]{"<warp>", arg}));
        TeleMan.teleportPlayer(player, VexusCore.getInstance().getWarpManager().getWarp(arg));
    }
}
