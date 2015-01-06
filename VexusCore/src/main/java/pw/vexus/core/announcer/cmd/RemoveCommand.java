package pw.vexus.core.announcer.cmd;

import com.google.common.collect.ImmutableList;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
import pw.vexus.core.announcer.AnnouncerManager;

public final class RemoveCommand extends VexusCommand {
    public RemoveCommand() {
        super("remove");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new ArgumentRequirementException("You have not specified an item to remove!");
        int i;
        try {
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentRequirementException("You need to type a number!");
        }
        AnnouncerManager announcerManager = VexusCore.getInstance().getAnnouncerManager();
        ImmutableList<String> announcements = announcerManager.getAnnouncements();
        i--;
        if (i >= announcements.size() || i < 0) throw new ArgumentRequirementException("You need to specify an announcement that exists!");
        announcerManager.removeAnnouncement(i);
        sender.sendMessage(VexusCore.getInstance().getFormat("announcement-removed", new String[]{"<x>", String.valueOf(i)}));
    }
}
