package pw.vexus.core.chat;

import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CooldownUnexpiredException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.vexus.core.CooldownManager;
import pw.vexus.core.VexusCore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public enum ChatFilter {
    MESSAGE_SPAM((player, message) -> {
        try {
            CooldownManager.testForPermissibleCooldown("chatspam", player);
        } catch (CooldownUnexpiredException e) {
            throw new ChatFilterException("You're sending messages too quickly, wait " + TimeUnit.SECONDS.convert(e.getTimeRemaining(), e.getTimeUnit()) + " seconds.");
        }
    }),
    SIMILAR_WORD(new FilterOperator() {
        private final Map<CPlayer, String> lastMessages = new HashMap<>();

        {
            VexusCore.getInstance().registerListener(new SimilarWordListener());
        }

        @Override
        public void operate(CPlayer player, String message) throws ChatFilterException {
            if (lastMessages.containsKey(player) && lastMessages.get(player).trim().equalsIgnoreCase(message))
                throw new ChatFilterException("Your message was similar to your last!");
            lastMessages.put(player, message);
        }

        class SimilarWordListener implements Listener {
            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                lastMessages.remove(Core.getOnlinePlayer(event.getPlayer()));
            }
        }
    }),
    WORD_LENGTH((player, message) -> {
        message = message.trim();
        String[] split = message.split(" ");
        if (split.length < 2 && split[0].length() < 3) {
            for (String smallWord : ChatConstants.SMALL_WORDS) {
                if (message.equalsIgnoreCase(smallWord)) return;
            }
            throw new ChatFilterException("That message is too short!");
        }
        int count = 0;
        for (String s : split) {
            if (s.length() == 1) count++;
            if (count > 3) throw new ChatFilterException("You have too many small words!");
        }
    });

    private final FilterOperator operator;

    ChatFilter(FilterOperator operator) {
        this.operator = operator;
    }

    public static void runFilters(CPlayer player, String message) throws ChatFilterException {
        if (player.hasPermission("vexus.bypassfilters")) return;
        for (ChatFilter chatFilter : ChatFilter.values()) chatFilter.operator.operate(player, message);
    }

    private static interface FilterOperator {
        void operate(CPlayer player, String message) throws ChatFilterException;
    }

    public static class ChatFilterException extends Exception {
        public ChatFilterException(String message) {
            super(message);
        }
    }
}
