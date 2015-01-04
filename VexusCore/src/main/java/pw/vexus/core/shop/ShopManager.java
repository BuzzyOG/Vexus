package pw.vexus.core.shop;

import lombok.Value;
import org.bukkit.Material;
import pw.vexus.core.VexusCore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class ShopManager {
    private final Map<Material, ShopItem> items = new HashMap<>();

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
                int dataValue = idParts.length > 1 ? Integer.parseInt(idParts[1]) : 0;
                Material material = Material.getMaterial(id);
                items.put(material, new ShopItem(material, dataValue, split[1], Double.valueOf(split[2]), Double.valueOf(split[3])));
            } catch (Throwable t) {
                VexusCore.getInstance().getLogger().warning("Unable to parse line " + x);
                t.printStackTrace();
            }
        }
    }

    public ShopItem getItemFor(Material m) {
        return items.get(m);
    }

    @Value class ShopItem {
        private Material item;
        private int dataValue;
        private String humanName;
        private double sell, buy;
    }
}
