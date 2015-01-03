package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.ArrayList;
import java.util.List;

@CommandMeta(description = "Toggles vanish for you", aliases = {"v", "van"})
@CommandPermission("vexus.vanish")
public final class VanishCommand extends VexusCommand {
    private static VanishCommand instance;

    public static boolean isVanished(CPlayer player) {
        return instance.manager.isVanished(player);
    }

    public static boolean canSee(CPlayer target, CPlayer seer) {
        return !(isVanished(target) && !canBypassVanish(seer));
    }

    public static boolean canBypassVanish(CPlayer player) {
        return player.hasPermission("vexus.vanish.allvisibile");
    }

    private final VanishManager manager = new VanishManager();

    public VanishCommand() {
        super("vanish");
        instance = this;
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        boolean b = manager.toggleVanish(player);
        player.sendMessage(VexusCore.getInstance().getFormat("vanish-toggled", new String[]{"<state>", b ? "enabled" : "disabled"}));
        player.playSoundForPlayer(Sound.CREEPER_DEATH);
    }

    public static final class VanishManager implements Listener {
        private final List<CPlayer> vanished = new ArrayList<>();

        public VanishManager() {
            VexusCore.getInstance().registerListener(this);
        }

        public boolean isVanished(CPlayer player) {
            return vanished.contains(player);
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            CPlayer onlinePlayer = Core.getOnlinePlayer(event.getPlayer());
            for (CPlayer cPlayer : vanished) hideForPlayer(cPlayer, onlinePlayer);
            Boolean vanished = onlinePlayer.getSettingValue("vanished", Boolean.class);
            if (vanished == null || !vanished) return;
            vanish(onlinePlayer);
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            CPlayer onlinePlayer = Core.getOnlinePlayer(event.getPlayer());
            vanished.remove(onlinePlayer);
        }

        @EventHandler
        public void onPlayerPvP(EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player && isVanished(Core.getOnlinePlayer((Player) event.getDamager())) && event.getEntity() instanceof Player) event.setCancelled(true);
        }

        public void vanish(CPlayer player) {
            vanished.add(player);
            for (CPlayer cPlayer : Core.getOnlinePlayers()) hideForPlayer(player, cPlayer);
            player.storeSettingValue("vanished", true);
        }

        public void unvanish(CPlayer player) {
            vanished.remove(player);
            Player bukkitPlayer = player.getBukkitPlayer();
            for (CPlayer cPlayer : Core.getOnlinePlayers()) cPlayer.getBukkitPlayer().showPlayer(bukkitPlayer);
            player.storeSettingValue("vanished", false);
        }

        public boolean toggleVanish(CPlayer player) {
            boolean vanished1 = isVanished(player);
            if (vanished1) unvanish(player);
            else vanish(player);
            return !vanished1;
        }

        private void hideForPlayer(CPlayer hidden, CPlayer target) {
            if (canBypassVanish(target)) return;
            target.getBukkitPlayer().hidePlayer(hidden.getBukkitPlayer());
        }
    }
}
