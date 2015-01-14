package pw.vexus.core.shop;

import lombok.Value;
import org.bukkit.Material;

@Value class ShopItem {
    private Material item;
    private int dataValue;
    private String humanName;
    private double sell, buy;
}
