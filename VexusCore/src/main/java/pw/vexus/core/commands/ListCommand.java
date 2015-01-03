package pw.vexus.core.commands;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CommandMeta(description = "Lists all online players.")
@CommandPermission("vexus.list")
public final class ListCommand extends VexusCommand {

    public ListCommand() {
        super("list");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Collection<CPlayer> onlinePlayers = Core.getOnlinePlayers();

        int count = 0;
        for (CPlayer onlinePlayer : onlinePlayers) {
            if (!VanishCommand.canSee(onlinePlayer, player)) continue;
            count++;
        }
        player.sendMessage(VexusCore.getInstance().getFormat("players-online", new String[]{"<online>", String.valueOf(count)}, new String[]{"<max>", String.valueOf(Bukkit.getMaxPlayers())}));

        List<String> names = new ArrayList<>();
        for (CPlayer cPlayer : onlinePlayers) {
            if (!VanishCommand.canSee(cPlayer, player)) continue;
            String chatColor = cPlayer.getChatColor();
            String chatColor1 = cPlayer.getPrimaryGroup().getChatColor();
            names.add((ChatColor.translateAlternateColorCodes('&', chatColor == null ? chatColor1 == null ? "" : chatColor1 : chatColor)) + cPlayer.getName());
        }
        player.sendMessage(VexusCore.getInstance().getFormat("player-list", new String[]{"<list>", Joiner.on(ChatColor.GRAY + ", " + ChatColor.GREEN).join(names)}));

    }
}
