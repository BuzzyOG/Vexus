package pw.vexus.core.shop;

public final class SellCommand extends AbstractShopCommand {
    public SellCommand() {
        super("sell");
    }

    @Override
    protected ShopVillagerManager.StoreAction getAction() {
        return ShopVillagerManager.StoreAction.SELL;
    }
}
