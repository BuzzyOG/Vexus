package pw.vexus.core.home;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandPermission("vexus.sethome")
@CommandMeta(description = "Sets a home where you are standing!")
public final class SetHomeCommand extends VexusCommand {
    public SetHomeCommand() {
        super("sethome");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        HomeManager homeManager = VexusCore.getInstance().getHomeManager();
        int maxHomes = HomeManager.getMaxHomes(player);
        if (maxHomes != -1 && homeManager.getHomes(player).size() >= maxHomes) throw new ArgumentRequirementException("You cannot set any more homes!");
        String name = (args.length == 0 ? "home" : args[0]).toLowerCase();
        homeManager.setHome(name, player.getBukkitPlayer().getLocation(), player);
        player.sendMessage(VexusCore.getInstance().getFormat("set-home", new String[]{"<home>", name}));
    }
}
