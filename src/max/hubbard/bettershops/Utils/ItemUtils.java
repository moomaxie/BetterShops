package max.hubbard.bettershops.Utils;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ItemUtils {

    public static boolean compare(ItemStack it, ItemStack item) {
        return item.isSimilar(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability();
    }

    public static String toString(ItemStack i) {
        return mapToString(i.serialize());
    }

    public static ItemStack fromString(String s) {

        Map<String, Object> map = stringToMap(s);

        for (String s1 : map.keySet()) {
            if (s1.contains("damage")) {
                map.put(s1, Integer.valueOf((String) map.get(s1)));
                break;
            }
        }

        return ItemStack.deserialize(map);
    }

    private static String mapToString(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key).toString();
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return stringBuilder.toString();
    }

    public static Map<String, Object> stringToMap(String input) {
        Map<String, Object> map = new HashMap<>();
        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"),
                        nameValue.length > 1 ? URLDecoder.decode(nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return map;
    }

    public static String getEnchants(ItemStack i) {
        List<String> e = new ArrayList<String>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for (Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    public static String deserialize(ItemStack i) {
        String[] parts = new String[6];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        parts[3] = i.getItemMeta().getDisplayName();
        parts[4] = String.valueOf(i.getData().getData());
        parts[5] = getEnchants(i);
        return StringUtils.join(parts, ";");
    }

    public ItemStack deserial(String p) {

        String[] a = p.split(";");
        ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
        i.setDurability((short) Integer.parseInt(a[2]));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(a[3]);
        i.setItemMeta(meta);
        MaterialData data = i.getData();
        data.setData((byte) Integer.parseInt(a[4]));
        i.setData(data);
        if (a.length > 5) {
            String[] parts = a[5].split(",");
            for (String s : parts) {
                String label = s.split(":")[0];
                String amplifier = s.split(":")[1];
                Enchantment type = Enchantment.getByName(label);
                if (type == null)
                    continue;
                int f;
                try {
                    f = Integer.parseInt(amplifier);
                } catch (Exception ex) {
                    continue;
                }
                i.addEnchantment(type, f);
            }
        }
        return i;
    }

    public static Map<String, Object> deserialize(String s) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        s = s.substring(1, s.length() - 1);           //remove curly brackets
        String[] keyValuePairs = s.split(",");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            String g = entry[1];
            if (entry.length > 2) {
                for (int i = 2; i < entry.length; i++) {
                    g = g + "=" + entry[i];
                }
            }

            map.put(entry[0].trim(), g.trim());

        }

        return map;
    }
}
