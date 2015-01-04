package pw.vexus.core.home;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
import pw.vexus.core.commands.Confirmer;

@CommandMeta(description = "Delete a home")
@CommandPermission("vexus.delhome")
public final class DelHomeCommand extends VexusCommand {
    public DelHomeCommand() {
        super("delhome");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You have not specified a home to delete!");
        final String home = args[0].toLowerCase();
        if (!VexusCore.getInstance().getHomeManager().getHomes(player).containsKey(home)) throw new ArgumentRequirementException("That home does not exist!");
        Confirmer.confirm("Are you sure you want to delete the home " + home + "?", player, new Confirmer.ConfirmerCallback() {
            @Override
            public void call(boolean result, CPlayer player) {
                if (!result) return;
                VexusCore.getInstance().getHomeManager().delHome(home, player);
                player.sendMessage(VexusCore.getInstance().getFormat("deleted-home", new String[]{"<home>", home}));
            }
        });
    }
}
