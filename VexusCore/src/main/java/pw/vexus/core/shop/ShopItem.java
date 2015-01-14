package pw.vexus.core.shop;

import lombok.Value;
import org.bukkit.Material;

@Value class ShopItem {
    private Material item;
    private short dataValue;
    private String humanName;
    private double sell, buy;
}
