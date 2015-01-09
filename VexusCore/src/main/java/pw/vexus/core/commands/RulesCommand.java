package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.ChatColor;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@CommandMeta(description = "Display the servers rules.")
@CommandPermission("vexus.rules")
public final class RulesCommand extends VexusCommand {
    private final List<String> rules;

    public RulesCommand(File file) throws IOException {
        super("rules");
        if (!file.exists()) file.createNewFile();
        rules = new BufferedReader(new FileReader(file))
                .lines()
                .map((string) -> ChatColor.translateAlternateColorCodes('&', string))
                .collect(Collectors.toList());
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (rules.size() == 0) {
            player.sendMessage(VexusCore.getInstance().getFormat("no-rules"));
            return;
        }
        player.sendMessage(VexusCore.getInstance().getFormat("rules-title"));
        int x = 0;
        for (String rule : rules) {
            x++;
            player.sendMessage(VexusCore.getInstance().getFormat("rules-message", new String[]{"<rule>", rule}, new String[]{"<num>", String.format("%02d", x)}));
        }
    }
}
