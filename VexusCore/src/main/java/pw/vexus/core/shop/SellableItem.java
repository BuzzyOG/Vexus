package pw.vexus.core.shop;

import lombok.Value;
import org.bukkit.Material;

/**
* Created by Electric on 1/14/2015.
*/
@Value
class SellableItem {
    private Material material;
    private byte dataValue;
}
