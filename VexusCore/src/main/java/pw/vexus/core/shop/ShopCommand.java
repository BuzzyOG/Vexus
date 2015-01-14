package pw.vexus.core.shop;

import pw.vexus.core.VexusCommand;

public class ShopCommand extends VexusCommand {
    public ShopCommand() {
        super("shop", new SellCommand(), new BuyCommand(), new CreateVillagerCommand());
    }
}
