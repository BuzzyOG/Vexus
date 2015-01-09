package pw.vexus.core.commands;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.ChatColor;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

@CommandMeta(description = "Display the servers rules.")
@CommandPermission("vexus.rules")
public final class RulesCommand extends VexusCommand {
    private final List<String> rules;

    public RulesCommand(File file) throws FileNotFoundException {
        super("rules");
        rules = new BufferedReader(new FileReader(file))
                .lines()
                .map((string) -> ChatColor.translateAlternateColorCodes('&', string))
                .collect(Collectors.toList());
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        player.sendMessage(VexusCore.getInstance().getFormat("rules-title"));
        String rulesString = Joiner.on("/t").join(rules);
        player.sendMessage(VexusCore.getInstance().getFormat("rules-message", new String[]{"<rules>", rulesString}));
    }
}
