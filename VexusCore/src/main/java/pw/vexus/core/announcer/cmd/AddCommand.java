package pw.vexus.core.announcer.cmd;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

public final class AddCommand extends VexusCommand {
    public AddCommand() {
        super("add");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) throw new ArgumentRequirementException("You have not specified an announcement");
        String announcement = Joiner.on(' ').join(args);
        VexusCore.getInstance().getAnnouncerManager().addAnnouncement(announcement);
        sender.sendMessage(VexusCore.getInstance().getFormat("announcement-added", new String[]{"<announce>", announcement}));
    }
}
