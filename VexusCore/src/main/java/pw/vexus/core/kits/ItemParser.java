package pw.vexus.core.kits;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemParser {
    public static ItemStack fromString(String[] split) {
        if (split.length < 1) throw new IllegalArgumentException("You have not specified enough information to form an item");
        String s1 = split[0];
        short data = 0;
        if (s1.contains(":")) {
            String[] split1 = s1.split(":");
            data = Short.valueOf(split1[1]);
            s1 = split1[0];
        }
        ItemStack itemStack = new ItemStack(Material.valueOf(s1));
        itemStack.setDurability(data);
        if (split.length == 1) return itemStack;
        itemStack.setAmount(Integer.valueOf(split[1]));
        if (split.length == 2) return itemStack;
        List<String> lore = new ArrayList<>();
        String name = null;
        for (int i = 2; i < split.length; i++) {
            char[] chars = split[i].toCharArray();
            if (chars.length != 2 || chars[0] != '-') break;
            int end = i;
            while (end < split.length && split[end].charAt(0) != '-') end++;
            String value = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(Arrays.copyOfRange(split, i + 1, end)));
            switch (chars[1]) {
                case 'l':
                    lore.add(value);
                    break;
                case 'n':
                    if (name != null) throw new IllegalArgumentException("Name is defined twice!");
                    name = value;
                    break;
                case 'e':
                    String[] split1 = value.split(":");
                    int level = 1;
                    if (split1.length != 1) level = Integer.valueOf(split1[1]);
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(split1[0]), level);
                    break;
                default:
                    throw new IllegalArgumentException("Ending parse due to bad argument!");
            }
            i = end;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        if (name != null) itemMeta.setDisplayName(name);
        return itemStack;
    }

    public static ItemStack fromString(String s) {
        return fromString(s.split(" "));
    }
}
