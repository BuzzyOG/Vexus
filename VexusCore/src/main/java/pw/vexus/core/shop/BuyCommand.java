package pw.vexus.core.shop;

public final class BuyCommand extends AbstractShopCommand {
    public BuyCommand() {
        super("buy");
    }

    @Override
    protected ShopVillagerManager.StoreAction getAction() {
        return ShopVillagerManager.StoreAction.BUY;
    }
}
