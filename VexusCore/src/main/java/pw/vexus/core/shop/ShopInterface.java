package pw.vexus.core.shop;

import net.cogzmc.core.effect.npc.ClickAction;
import net.cogzmc.core.gui.InventoryButton;
import net.cogzmc.core.gui.InventoryGraphicalInterface;
import net.cogzmc.core.modular.command.EmptyHandlerException;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CPlayerConnectionListener;
import net.cogzmc.core.player.CPlayerJoinException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class ShopInterface implements CPlayerConnectionListener {
    private final List<Material> soldItems = new LinkedList<>();
    private final InventoryGraphicalInterface shopGui;
    private final CPlayer player;

    public ShopInterface(String title, CPlayer player) {
        shopGui = new InventoryGraphicalInterface(54, title);
        this.player = player;
    }

    @Override
    public void onPlayerLogin(CPlayer player, InetAddress address) throws CPlayerJoinException {}

    @Override
    public void onPlayerDisconnect(CPlayer player) {
        //TODO close shit
    }

    private class PurchaseItemButton extends InventoryButton {
        public PurchaseItemButton(Material itemForSale) {
            super(getItemStackFor(itemForSale));
        }

        @Override
        protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {

        }
    }

    private static ItemStack getItemStackFor(Material type) {
        ItemStack itemStack = new ItemStack(type, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList("Noah", "is", "a", "queer"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
