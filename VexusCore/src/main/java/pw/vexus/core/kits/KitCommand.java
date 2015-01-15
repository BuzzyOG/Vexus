package pw.vexus.core.kits;

import net.cogzmc.core.Core;
import net.cogzmc.core.effect.npc.ClickAction;
import net.cogzmc.core.gui.InventoryButton;
import net.cogzmc.core.gui.InventoryGraphicalInterface;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CooldownUnexpiredException;
import net.cogzmc.core.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joda.time.Duration;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommandMeta(aliases = {"kits", "k"}, description = "Allows players to give themselves kits", usage = "/kit [name]")
@CommandPermission("vexus.kit")
public final class KitCommand extends VexusCommand {
    public KitCommand() {
        super("kit");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length == 0) {
            getInterfaceFor(player).open(player);
            return;
        }
        String kitPartial = args[0];
        Kit useKit = null;
        for (Kit kit : VexusCore.getInstance().getKitManager().getKits()) {
            if (kit.getName().toLowerCase().startsWith(kitPartial)) useKit = kit;
        }
        if (useKit == null) throw new ArgumentRequirementException("Kit not found!");
        if (!useKit.hasPermission(player)) throw new PermissionException("You do not have permission for this!");
        CPlayer target = player;
        if (args.length > 1) target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[1]);
        if (target == null) throw new ArgumentRequirementException("The target player has not been found!");
        else if (!player.hasPermission("vexus.kit.others")) throw new PermissionException("You do not have permission for this!");
        attemptGiveKit(target, useKit, target != player);
    }

    @Override
    protected List<String> handleTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        if (args.length > 1) return super.handleTabComplete(sender, command, alias, args);
        Player player = (Player) sender;
        CPlayer onlinePlayer = Core.getOnlinePlayer(player);
        return VexusCore.getInstance().getKitManager().getKits().stream().filter((kit) -> kit.hasPermission(onlinePlayer) && kit.getName().toLowerCase().startsWith(args[0].toLowerCase())).map(Kit::getName).collect(Collectors.toList());
    }

    private static InventoryGraphicalInterface getInterfaceFor(CPlayer player) {
        final VexusCore instance = VexusCore.getInstance();
        List<Kit> kits = instance.getKitManager().getKits();
        int size = kits.size();
        InventoryGraphicalInterface anInterface = new InventoryGraphicalInterface(size + (9 - (size % 9)), instance.getFormat("kit-gui-title", false));
        for (Kit kit : kits) {
            if (!kit.hasPermission(player)) continue;
            boolean canUse1 = true;
            try {
                kit.checkCooldown(player, false);
            } catch (CooldownUnexpiredException e) {
                canUse1 = false;
            }
            final boolean finalCanUse = canUse1;
            ItemStack itemStack = new ItemStack(kit.getIconImage());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(instance.getFormat("kit-button-title", false, new String[]{"<name>", kit.getName()}));
            itemMeta.setLore(Arrays.asList(instance.getFormat("kit-lore-line", false, new String[]{"<canUse>", !finalCanUse ? ChatColor.RED + "no" : ChatColor.GREEN + "yes"})));
            itemStack.setItemMeta(itemMeta);
            anInterface.addButton(new InventoryButton(itemStack) {
                @Override
                protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {
                    attemptGiveKit(player, kit, false);
                }
            });
        }
        anInterface.updateInventory();
        return anInterface;
    }

    private static void attemptGiveKit(CPlayer player, Kit kit, boolean force) {
        VexusCore instance = VexusCore.getInstance();
        if (!force) {
            try {
                kit.checkCooldown(player);
            } catch (CooldownUnexpiredException e) {
                player.sendMessage(instance.getFormat("kit-cannot-use", new String[]{"<kit>", kit.getName()}, new String[]{"<timeRemain>", TimeUtils.formatDurationNicely(new Duration(TimeUnit.MILLISECONDS.convert(e.getTimeRemaining(), e.getTimeUnit())))}));
                player.playSoundForPlayer(Sound.NOTE_BASS);
                return;
            }
        }
        try {
            kit.giveToPlayer(player, force);
        } catch (KitGiveException e) {
            player.sendMessage(instance.getFormat("error", new String[]{"<error>", e.getMessage()}));
            player.playSoundForPlayer(Sound.NOTE_BASS);
        }

        player.sendMessage(VexusCore.getInstance().getFormat("kit-give", new String[]{"<kit>", kit.getName()}));
        player.playSoundForPlayer(Sound.ORB_PICKUP, 50f, 2f);
    }
}
