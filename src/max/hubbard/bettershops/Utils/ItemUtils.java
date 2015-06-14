package max.hubbard.bettershops.Utils;

import org.bukkit.inventory.ItemStack;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
                throw new RuntimeException("This method requires UTF-8 encoding support" , e);
            }
        }
        return stringBuilder.toString();
    }

    private static Map<String, Object> stringToMap(String input) {
        Map<String, Object> map = new HashMap<>();
        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"),
                        nameValue.length > 1 ? URLDecoder.decode(nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support" , e);
            }
        }

        return map;
    }
}
