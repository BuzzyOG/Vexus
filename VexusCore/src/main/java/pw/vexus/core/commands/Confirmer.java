package pw.vexus.core.commands;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Confirmer {
    private final static Map<UUID, ConfrimerCallback> pendingQuestions = new HashMap<>();

    public static void confirm(String prompt, CPlayer player, ConfrimerCallback callback) {
        pendingQuestions.put(player.getUniqueIdentifier(), callback);
        VexusCore instance = VexusCore.getInstance();
        player.sendMessage(instance.getFormat("conf.question", new String[]{"<prompt>", prompt}), instance.getFormat("conf.allow"), instance.getFormat("conf.deny"));
    }

    public static class ConfirmerDriver implements Listener {
        public ConfirmerDriver() {
            VexusCore.getInstance().registerCommand(new YesCommand());
            VexusCore.getInstance().registerCommand(new NoCommand());
            VexusCore.getInstance().registerListener(this);
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            pendingQuestions.remove(event.getPlayer().getUniqueId());
        }
    }

    public static interface ConfrimerCallback {
        void call(boolean result, CPlayer player);
    }

    private static abstract class ConfirmCommand extends VexusCommand {
        protected ConfirmCommand(String name) {
            super(name);
        }

        protected abstract boolean getRelevantAnswer();

        @Override
        protected void handleCommand(CPlayer player, String[] args) throws CommandException {
            if (!pendingQuestions.containsKey(player.getUniqueIdentifier()))
                throw new ArgumentRequirementException("You have nothing to respond to!");
            pendingQuestions.get(player.getUniqueIdentifier()).call(getRelevantAnswer(), player);
            pendingQuestions.remove(player.getUniqueIdentifier());
        }
    }

    @CommandMeta(aliases = {"confirm", "y"})
    public static class YesCommand extends ConfirmCommand {
        public YesCommand() {
            super("yes");
        }

        @Override
        protected boolean getRelevantAnswer() {
            return true;
        }
    }

    @CommandMeta(aliases = {"deny", "n"})
    public static class NoCommand extends ConfirmCommand {
        public NoCommand() {
            super("no");
        }

        @Override
        protected boolean getRelevantAnswer() {
            return false;
        }
    }
}
