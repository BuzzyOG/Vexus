package pw.vexus.core.shop;

import org.bukkit.Material;
import pw.vexus.core.VexusCore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class ShopManager {
    private final Map<SellableItem, ShopItem> items = new HashMap<>();

    public ShopManager(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String s;
        int x = 0;
        while ((s = bufferedReader.readLine()) != null) {
            x++;
            try {
                String[] split = s.split(";");
                String[] idParts = split[0].split(":");
                int id = Integer.parseInt(idParts[0]);
                short dataValue = idParts.length > 1 ? Short.parseShort(idParts[1]) : 0;
                Material material = Material.getMaterial(id);
                items.put(new SellableItem(material, dataValue), new ShopItem(material, dataValue, split[1], Double.valueOf(split[2]), Double.valueOf(split[3])));
            } catch (Throwable t) {
                VexusCore.getInstance().getLogger().warning("Unable to parse line " + x);
                t.printStackTrace();
            }
        }
    }

    public ShopItem getItemFor(Material m, short dataValue) {
        return getItemFor(new SellableItem(m, dataValue));
    }

    public ShopItem getItemFor(SellableItem item) {
        return items.get(item);
    }

}
