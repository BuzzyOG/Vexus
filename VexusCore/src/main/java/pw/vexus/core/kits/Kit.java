package pw.vexus.core.kits;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CooldownUnexpiredException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pw.vexus.core.CooldownManager;

import java.util.List;

@Value
//value implies final
public final class Kit {
    @Getter(AccessLevel.PACKAGE) private List<KitManager.RawKitItem> items;
    private String name;
    private Long cooldownSeconds;
    private Material iconImage;

    public boolean hasPermission(CPlayer player) {
        return player.hasPermission("vexus.kit." + name);
    }

    public void checkCooldown(CPlayer player) throws CooldownUnexpiredException {
        checkCooldown(player, true);
    }

    public void giveToPlayer(CPlayer player) throws KitGiveException {
        giveToPlayer(player, false);
    }

    public void giveToPlayer(CPlayer player, boolean force) throws KitGiveException {
        Player bukkitPlayer = player.getBukkitPlayer();
        PlayerInventory inventory = bukkitPlayer.getInventory();
        if (!force) {
            int free = 0;
            for (int x = 0; x < 36; x++) free += (inventory.getItem(x) == null ? 1 : 0);
            if (free < items.size()) throw new KitGiveException("You do not have enough room!");
        }
        for (KitManager.RawKitItem item : items) inventory.addItem(item.getStack());
    }

    public void checkCooldown(CPlayer player, boolean b) throws CooldownUnexpiredException {
        CooldownManager.testForCooldown("kit_" + name, player, cooldownSeconds, b);
    }
}
