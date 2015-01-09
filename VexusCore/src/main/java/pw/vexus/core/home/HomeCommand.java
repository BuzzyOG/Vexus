package pw.vexus.core.home;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.vexus.core.TeleMan;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CommandMeta(description = "Teleports you to your home!")
@CommandPermission("vexus.home")
public final class HomeCommand extends VexusCommand {
    public HomeCommand() {
        super("home");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Map<String, Location> homes = VexusCore.getInstance().getHomeManager().getHomes(player);
        if (homes.size() == 0) throw new ArgumentRequirementException("You need to set a home!");
        if (args.length < 1 && homes.size() > 1) throw new ArgumentRequirementException("You have not specified a home!");
        String home =  args.length < 1 && homes.size() == 1 ? homes.keySet().iterator().next() : args[0].toLowerCase();
        Location l = homes.get(home);
        if (l == null) throw new ArgumentRequirementException("This home has not been set!");
        player.sendMessage(VexusCore.getInstance().getFormat("teleport-home", new String[]{"<home>", home}));
        TeleMan.teleportPlayer(player, l);
    }

    @Override
    protected List<String> handleTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1 || !(sender instanceof Player)) return super.handleTabComplete(sender, command, alias, args);
        Map<String, Location> homes = VexusCore.getInstance().getHomeManager().getHomes(Core.getOnlinePlayer((Player) sender));
        String arg = args[0].toLowerCase();
        return homes.keySet().stream().filter(s -> s.toLowerCase().startsWith(arg)).collect(Collectors.toList());
    }
}
