package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import pw.vexus.core.DataJSONFile;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(description = "Spawns the player")
public final class SpawnCommand extends VexusCommand {
    public SpawnCommand() {
        super("spawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        DataJSONFile dataJSONFile = VexusCore.getInstance().getDataJSONFile();
        TeleMan.teleportPlayer(player, dataJSONFile.getSpawn().getLocation(Bukkit.getWorld(dataJSONFile.getSpawnWorld())));
    }
}
