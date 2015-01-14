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
import net.cogzmc.core.player.DatabaseConnectException;
import net.cogzmc.core.util.Point;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import pw.vexus.core.VexusCore;
import pw.vexus.core.commands.Confirmer;
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

        //Anonymous classception
        InventoryGraphicalInterface getInterfaceFor() {
            //level one, the inventory interface for the actual shop
            if (soldItems.length > 54) throw new UnsupportedOperationException();
            InventoryGraphicalInterface shopInterface = new InventoryGraphicalInterface(54, title);
            VexusCore instance = VexusCore.getInstance();
            ShopManager shopManager = instance.getShopManager();
            for (SellableItem soldItem : soldItems) { //one item for each of the items sold
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
                shopInterface.addButton(new InventoryButton(itemStack) { //level 2, once you click we create another interface
                    @Override
                    protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {
                        StoreAction storeAction = action == ClickAction.RIGHT_CLICK ? StoreAction.BUY : StoreAction.SELL;
                        shopInterface.close(player);
                        int size = 9;
                        //interface 2
                        InventoryGraphicalInterface quantityInterface = new InventoryGraphicalInterface(size, instance.getFormat("shop-quantity-select", false, new String[]{"<item>", itemFor.getHumanName()}, new String[]{"<action>", storeAction.name().toLowerCase()}));
                        for (int x = 1; x < size; x++) { //for each of the powers of 2 up to 8
                            int i = ((int) Math.pow(2, x));
                            ItemStack stack = new ItemStack(itemFor.getItem(), i);
                            stack.setDurability((short) itemFor.getDataValue());
                            ItemMeta itemMeta1 = stack.getItemMeta();
                            double price = (storeAction == StoreAction.BUY ? itemFor.getBuy() : itemFor.getSell()) * i;
                            String format = EconomyManager.format(price);
                            itemMeta1.setDisplayName(instance.getFormat("shop-quantity-item", false, new String[]{"<quantity>", String.valueOf(i)}, new String[]{"<item>", itemFor.getHumanName()}, new String[]{"<price>", format}));
                            stack.setItemMeta(itemMeta1);
                            stack.setAmount(i);
                            final int iFinal = i;
                            quantityInterface.addButton(new InventoryButton(stack) {
                                @Override
                                protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {
                                    quantityInterface.close(player);
                                    Confirmer.confirm("Are you sure you want to " + storeAction.name().toLowerCase() + " " + iFinal + " for " + format, player, (result, p) -> {
                                        shopInterface.open(player);
                                        if (result) {
                                            try {
                                                performTransaction(itemFor, iFinal, player, storeAction);
                                            } catch (TransactionException e) {
                                                player.sendMessage(instance.getFormat("error", new String[]{"<error>", e.getMessage()}));
                                                player.playSoundForPlayer(Sound.NOTE_BASS);
                                            }
                                        }
                                        else {
                                            player.sendMessage(instance.getFormat("error", new String[]{"<error>", "Transaction aborted."}));
                                            player.playSoundForPlayer(Sound.NOTE_BASS);
                                        }
                                    });
                                }
                            }, x);
                        }
                        ItemStack backButtonStack = new ItemStack(Material.WOOL);
                        //noinspection deprecation
                        backButtonStack.setDurability(DyeColor.RED.getWoolData());
                        ItemMeta itemMeta1 = backButtonStack.getItemMeta();
                        itemMeta1.setDisplayName(instance.getFormat("shop-back-button", false));
                        backButtonStack.setItemMeta(itemMeta1);
                        quantityInterface.addButton(new InventoryButton(backButtonStack) {
                            @Override
                            protected void onPlayerClick(CPlayer player, ClickAction action) throws EmptyHandlerException {
                                quantityInterface.close(player);
                                shopInterface.open(player);
                            }
                        }, 0);
                        quantityInterface.updateInventory();
                        quantityInterface.open(player);
                    }
                });
            }
            return shopInterface;
        }
    }

    public enum StoreAction {
        BUY,
        SELL
    }

    static class TransactionException extends Exception {
        public TransactionException(String m) {
            super(m);
        }
    }

    public static void performTransaction(ShopItem itemFor, int quantity, CPlayer player, StoreAction storeAction) throws TransactionException {
        double price = (storeAction == StoreAction.BUY ? itemFor.getBuy() : itemFor.getSell()) * quantity;
        VexusCore instance = VexusCore.getInstance();
        PlayerInventory inventory = player.getBukkitPlayer().getInventory();
        if (storeAction == StoreAction.BUY) {
            EconomyManager economyManager = instance.getEconomyManager();
            if (economyManager.getBalance(player) < price) throw new TransactionException("You do not have enough funds to purchase this!");
            try {
                economyManager.modifyBalance(player, -price);
            } catch (DatabaseConnectException e) {
                e.printStackTrace();
                throw new TransactionException("Unable to modify your balance in the database");
            }
            ItemStack itemStack = new ItemStack(itemFor.getItem());
            itemStack.setDurability(itemFor.getDataValue());
            itemStack.setAmount(quantity);
            inventory.addItem(itemStack);
        } else {
            int count = 0;
            for (int i = 0; i < 36; i++) {
                ItemStack item = inventory.getItem(i);
                if (item.getType() == itemFor.getItem() && item.getDurability() == itemFor.getDataValue()) count += item.getAmount();
            }
            if (count < quantity) throw new TransactionException("You don't have enough in your inventory to sell!");
            try {
                instance.getEconomyManager().modifyBalance(player, price);
            } catch (DatabaseConnectException e) {
                e.printStackTrace();
                throw new TransactionException("Failed to add funds to your balance!");
            }
            for (int i = 0, quantityRemain = quantity; i < 36 && quantityRemain > 0; i++) {
                ItemStack item = inventory.getItem(i);
                if (!(item.getType() == itemFor.getItem() && item.getDurability() == itemFor.getDataValue())) return;
                if (item.getAmount() > quantityRemain) {
                    item.setAmount(item.getAmount()-quantityRemain);
                    inventory.setItem(i, item);
                }
                else inventory.setItem(i, null);
                quantityRemain -= item.getAmount();
            }
        }
        player.sendMessage(instance.getFormat("shop-made-transaction", new String[]{"<action>", storeAction == StoreAction.BUY ? "bought" : "sold"}, new String[]{"<item>", itemFor.getHumanName()}, new String[]{"<quantity>", String.valueOf(quantity)}, new String[]{"<price>", EconomyManager.format(price)}));
        player.playSoundForPlayer(Sound.LEVEL_UP);
    }
}
