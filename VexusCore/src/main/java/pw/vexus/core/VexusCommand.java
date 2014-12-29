package pw.vexus.core;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.FriendlyException;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CooldownUnexpiredException;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public abstract class VexusCommand extends ModuleCommand {
    protected VexusCommand(String name) {
        super(name);
    }

    protected VexusCommand(String name, ModuleCommand... subCommands) {
        super(name, subCommands);
    }

    @Override
    protected void handleCommandException(CommandException ex, String[] args, CommandSender sender) {
        if (ex instanceof CooldownUnexpiredException) {
            CooldownUnexpiredException e = ((CooldownUnexpiredException) ex);
            sender.sendMessage(VexusCore.getInstance().getFormat("cooldown", new String[]{"<time>", String.valueOf(e.getTimeUnit().convert(e.getTimeRemaining(), TimeUnit.MINUTES))}));
        }
        else sender.sendMessage(VexusCore.getInstance().getFormat("error", new String[]{"<error>", (ex instanceof FriendlyException) ? ((FriendlyException) ex).getFriendlyMessage(this) : ex.getMessage()}));
        if (sender instanceof Player) Core.getOnlinePlayer((Player) sender).playSoundForPlayer(Sound.NOTE_BASS);
    }
}
