package pw.vexus.core.auction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AuctionManager {

    private int minBid;
    private int highestBid;
    private int time;
    private int quantity;

    private String ownerName;
    private String highestBidder;
    private String itemName;

    public void createNewAuction(Player auctionOwner, ItemStack item, Integer amount, String displayName, List<String> lore, Integer startBid) {
        ownerName = auctionOwner.getName();
        minBid = startBid;
        time = 60;
    }


}
