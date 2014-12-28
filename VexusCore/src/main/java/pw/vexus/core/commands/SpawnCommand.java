package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;

@CommandMeta(description = "Spawns the player")
public final class SpawnCommand extends VexusCommand {
    public SpawnCommand() {
        super("spawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        TeleMan.teleportPlayer(player, player.getBukkitPlayer().getWorld().getSpawnLocation());
    }
}
