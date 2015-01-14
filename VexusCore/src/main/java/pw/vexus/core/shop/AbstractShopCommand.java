package pw.vexus.core.shop;

import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Material;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;
import pw.vexus.core.commands.Confirmer;
import pw.vexus.core.econ.EconomyManager;

abstract class AbstractShopCommand extends VexusCommand {
    protected AbstractShopCommand(String name) {
        super(name);
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length < 2) throw new ArgumentRequirementException("You must specify an item and a quantity of that item to purchase!");
        String item = args[0];
        Integer quantity;
        try {
            quantity = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new ArgumentRequirementException("The quantity you have specified is invalid!");
        }
        String[] parts = item.split(":");
        if (parts.length > 1) item = parts[0];
        Material material;
        try {
            material = Material.valueOf(item.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentRequirementException("Item not found!");
        }
        ShopVillagerManager.StoreAction action = getAction();
        ShopItem itemFor = VexusCore.getInstance().getShopManager().getItemFor(new SellableItem(material, parts.length > 1 ? Short.parseShort(parts[1]) : (short) 0));
        if (itemFor == null) throw new ArgumentRequirementException("The item you specified is not in the shop!");

        double price = (action == ShopVillagerManager.StoreAction.BUY ? itemFor.getBuy() : itemFor.getSell()) * quantity;
        Confirmer.confirm("Are you sure you want to " + action.name().toLowerCase() + " " + itemFor.getHumanName() + " for " + EconomyManager.format(price), player, (result, p) -> {
            try {
                ShopVillagerManager.performTransaction(itemFor, quantity, player, action);
            } catch (ShopVillagerManager.TransactionException e) {
                e.printStackTrace();
            }
        });

    }

    protected abstract ShopVillagerManager.StoreAction getAction();
}
