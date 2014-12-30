package pw.vexus.core.commands;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.Collection;

@CommandMeta(description = "Lists all online players.")
@CommandPermission("vexus.list")
public class ListCommand extends VexusCommand {

    public ListCommand() {
        super("list");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        int online = Core.getOnlinePlayers().size();
        String o = Integer.toString(online);

        int max = Bukkit.getMaxPlayers();
        String m = Integer.toString(max);

        player.sendMessage(VexusCore.getInstance().getFormat("players-online", new String[]{"<online>", o}, new String[]{"max", m}));

        Collection<CPlayer> onlinePlayers = Core.getOnlinePlayers();
        String list = Joiner.on(",").join(onlinePlayers);

        player.sendMessage(VexusCore.getInstance().getFormat("player-list", new String[]{"<list>", list}));

    }
}
