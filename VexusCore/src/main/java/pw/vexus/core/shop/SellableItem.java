package pw.vexus.core.shop;

import lombok.Value;
import org.bukkit.Material;

@Value
class SellableItem {
    private Material material;
    private byte dataValue;
}
