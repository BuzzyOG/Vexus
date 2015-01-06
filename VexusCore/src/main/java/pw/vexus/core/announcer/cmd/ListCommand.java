package pw.vexus.core.announcer.cmd;

import com.google.common.collect.ImmutableList;
import net.cogzmc.core.modular.command.CommandException;
import org.bukkit.command.CommandSender;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

public final class ListCommand extends VexusCommand {
    public ListCommand() {
        super("list");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        ImmutableList<String> announcements = VexusCore.getInstance().getAnnouncerManager().getAnnouncements();
        sender.sendMessage(VexusCore.getInstance().getFormat("announcement-list-head", new String[]{"<x>", String.valueOf(announcements.size())}));
        int x = 1;
        for (String s : announcements) {
            sender.sendMessage(VexusCore.getInstance().getFormat("announcement-list-entry", new String[]{"<x>", String.valueOf(x)}, new String[]{"<announcement>", s}));
            x++;
        }
    }
}
