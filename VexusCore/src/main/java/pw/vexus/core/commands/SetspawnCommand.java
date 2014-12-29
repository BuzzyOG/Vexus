package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.util.Point;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandPermission("vexus.setspawn")
public final class SetspawnCommand extends VexusCommand {
    public SetspawnCommand() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Point point = player.getPoint();
        player.getBukkitPlayer().getWorld().setSpawnLocation(point.getX().intValue(), point.getY().intValue(), point.getZ().intValue());
        player.sendMessage(VexusCore.getInstance().getFormat("set-spawn"));
    }
}
