package pw.vexus.core.kits;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.vexus.core.VexusCore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class KitManager {
    @Getter(AccessLevel.PACKAGE) private final List<Kit> kits = new ArrayList<>();

    public KitManager(File kitsDirectory) throws IOException {
        if (!kitsDirectory.exists() && !kitsDirectory.isDirectory() && !kitsDirectory.mkdirs()) throw new IOException("Cannot create a directory for kits!");
        for (String s : kitsDirectory.list((directory, name) -> name.endsWith(".json"))) {
            try {
                Kit kit = VexusCore.getGSON().fromJson(new FileReader(new File(kitsDirectory, s)), Kit.class);
                kits.add(kit);
                VexusCore.getInstance().getLogger().info("Loaded kit " + s + ".json with " + kit.toString());
            } catch (Exception e) {
                e.printStackTrace();
                VexusCore.getInstance().getLogger().severe("Unable to read file " + s + " for kit usage");
            }
        }
    }

    @Data
    @Setter(AccessLevel.NONE)
    final class RawKitItem {
        private String type, title;
        private Integer quantity, dataValue;

        private List<String> lore;
        private List<RawKitEnchant> enchants;

        @Data @Setter(AccessLevel.NONE) final class RawKitEnchant {
            private String enchant;
            private Integer level = 1;
        }

        ItemStack getStack() {
            ItemStack itemStack = new ItemStack(Material.valueOf(type.toUpperCase()));
            if (dataValue != null) itemStack.setDurability(dataValue.byteValue());
            if (quantity != null) itemStack.setAmount(quantity);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (title != null) itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
            if (lore != null) itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            if (enchants != null) {
                for (RawKitEnchant enchant : enchants)
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(enchant.getEnchant()), enchant.getLevel());
            }
            return itemStack;
        }
    }
}
