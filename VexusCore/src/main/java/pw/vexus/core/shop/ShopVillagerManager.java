package pw.vexus.core.shop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.cogzmc.core.effect.npc.ClickAction;
import net.cogzmc.core.effect.npc.mobs.MobNPCVillager;
import net.cogzmc.core.gui.InventoryButton;
import net.cogzmc.core.gui.InventoryGraphicalInterface;
import net.cogzmc.core.modular.command.EmptyHandlerException;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.util.Point;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.vexus.core.VexusCore;
import pw.vexus.core.econ.EconomyManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ShopVillagerManager {
    private final File locationFile;
    private final List<ShopVillagerData> villagerData = new ArrayList<>();

    public ShopVillagerManager(File locationFile) throws IOException {
        this.locationFile = locationFile;
        villagerData.addAll(Arrays.asList(VexusCore.getGSON().fromJson(new FileReader(locationFile), ShopVillagerData[].class)));
        villagerData.stream().forEach(ShopVillagerData::spawn);
    }

    public void addVillager(Location location) {
        villagerData.add(new ShopVillagerData(location, new SellableItem[]{}, "New Shop Villager", "New Shop Villager"));
    }

    public void save() throws IOException {
        FileWriter fileWriter = new FileWriter(locationFile);
        fileWriter.write(VexusCore.getGSON().toJson(villagerData));
        fileWriter.flush();
        fileWriter.close();
    }

    @Data @Setter(AccessLevel.NONE) class ShopVillagerData {
        private final Location location;
        private final SellableItem[] soldItems;
        private final String title, villagerName;

        private transient MobNPCVillager villager;

        void spawn() {
            villager = new MobNPCVillager(Point.of(location), location.getWorld(), null, ChatColor.translateAlternateColorCodes('&', villagerName));
            InventoryGraphicalInterface interfaceFor = getInterfaceFor();
            villager.registerObserver((player, mob, action) -> interfaceFor.open(player));
            villager.spawn();
        }

        InventoryGraphicalInterface getInterfaceFor() {
            if (soldItems.length > 54) throw new IllegalStateException("This is unsupported at this time!");
            InventoryGraphicalInterface anInterface = new InventoryGraphicalInterface(54, title);
            VexusCore instance = VexusCore.getInstance();
            ShopManager shopManager = instance.getShopManager();
            for (SellableItem soldItem : soldItems) {
                ShopItem itemFor = shopManager.getItemFor(soldItem);
                ItemStack itemStack = new ItemStack(soldItem.getMaterial());
                itemStack.setDurability(soldItem.getDataValue());
                List<String> lore = new ArrayList<>();
                lore.add(instance.getFormat("shop-item-name", false, new String[]{"<name>", itemFor.getHumanName()}));
                lore.add("");
                lore.add(instance.getFormat("shop-buy-price", false, new String[]{"<price>", EconomyManager.format(itemFor.getBuy())}));
                lore.add(instance.getFormat("shop-sell-price", false, new String[]{"<price>", EconomyManager.format(itemFor.getSell())}));
                lore.add("");
                lore.add(instance.getFormat("shop-right-buy", false));
                lore.add(instance.getFormat("shop-left-sell", false));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                anInterface.addButton(new InventoryButton(itemStack) {
                    @Override
                    protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {
                        StoreAction storeAction = action == ClickAction.RIGHT_CLICK ? StoreAction.BUY : StoreAction.SELL;
                        anInterface.close(player);
                        InventoryGraphicalInterface quantityInterface = new InventoryGraphicalInterface(9, instance.getFormat("shop-quantity-select", false, new String[]{"<item>", itemFor.getHumanName()}));
                        for (int x = 0; x < 9;  x++) {
                            int i = ((int) Math.pow(2, x));
                            ItemStack stack = new ItemStack(itemFor.getItem(), i);
                            stack.setDurability((short) itemFor.getDataValue());
                            ItemMeta itemMeta1 = stack.getItemMeta();
                            itemMeta1.setLore(Arrays.asList());
                            //TODO complete this
                        }
                    }
                });
            }
            return anInterface;
        }
    }

    private enum StoreAction {
        BUY,
        SELL;
    }
}
