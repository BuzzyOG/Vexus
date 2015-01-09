package pw.vexus.core.warp;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            player.sendMessage(VexusCore.getInstance().getFormat("warp-list", new String[]{"<warps>", warps.size() == 0 ? "None" : Joiner.on(", ").join(warps)}));
            return;
        }
        String arg = args[0].toLowerCase();
        if (!warps.contains(arg)) throw new ArgumentRequirementException("That warp doesn't exist!");
        player.sendMessage(VexusCore.getInstance().getFormat("warp", new String[]{"<warp>", arg}));
        TeleMan.teleportPlayer(player, VexusCore.getInstance().getWarpManager().getWarp(arg));
    }

    @Override
    protected List<String> handleTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) return super.handleTabComplete(sender, command, alias, args);
        ImmutableList<String> warps = VexusCore.getInstance().getWarpManager().getWarps();
        if (args.length == 0) return warps;
        return warps.stream().filter(warp -> warp.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
    }
}
