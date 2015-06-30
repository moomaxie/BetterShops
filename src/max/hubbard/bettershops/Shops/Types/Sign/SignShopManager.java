package max.hubbard.bettershops.Shops.Types.Sign;

import max.hubbard.bettershops.Listeners.CreateShop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SignShopManager {

    private static HashMap<Sign, Material> signs = new HashMap<>();
    private static HashMap<Sign, Double> prices = new HashMap<>();
    private static HashMap<Sign, Integer> amts = new HashMap<>();
    private static HashMap<Sign, UUID> ids = new HashMap<>();
    private static HashMap<Sign, Boolean> sell = new HashMap<>();
    private static HashMap<Sign, Boolean> admin = new HashMap<>();
    private static HashMap<Sign, Chest> chests = new HashMap<>();
    private static HashMap<Sign, ItemStack> items = new HashMap<>();
    private static HashMap<Inventory, Sign> invs = new HashMap<>();

    public static HashMap<Sign, Material> getSigns() {
        return signs;
    }

    public static HashMap<Sign, Double> getPrices() {
        return prices;
    }

    public static HashMap<Sign, Integer> getAmounts() {
        return amts;
    }

    public static HashMap<Sign, UUID> getIds() {
        return ids;
    }

    public static boolean isSell(Sign s) {
        return sell.get(s);
    }

    public static boolean isAdmin(Sign s) {
        return admin.get(s);
    }

    public static boolean isOwner(Sign s, OfflinePlayer p) {
        return ids.get(s) == p.getUniqueId();
    }

    public static boolean isShopSign(Sign s) {
        return signs.containsKey(s);
    }

    public static Chest getChest(Sign s) {
        return chests.get(s);
    }

    public static Sign getSign(Inventory inv) {
        return invs.get(inv);
    }

    public static ItemStack getItem(Sign s) {
        return items.get(s);
    }

    public static void addSign(Sign sign, ItemStack m, double price, int amt, UUID id, boolean sel, boolean ad, Chest chest) {
        signs.put(sign, m.getType());
        prices.put(sign, price);
        amts.put(sign, amt);
        ids.put(sign, id);
        sell.put(sign, sel);
        admin.put(sign, ad);
        items.put(sign, m);
        if (chest != null) {
            chests.put(sign, chest);
            invs.put(chest.getInventory(), sign);
        }
    }

    public static void removeSign(Sign sign) {
        signs.remove(sign);
        prices.remove(sign);
        amts.remove(sign);
        ids.remove(sign);
        sell.remove(sign);
        admin.remove(sign);
        if (chests.containsKey(sign)) {
            invs.remove(chests.get(sign).getInventory());
            chests.remove(sign);
        }
        items.remove(sign);

    }

    public static int loadSignShops() {
        int shops = 0;
        for (World w : Bukkit.getWorlds()) {
            for (Chunk c : w.getLoadedChunks()) {
                for (BlockState st : c.getTileEntities()) {
                    if (st instanceof Sign) {
                        Sign s = (Sign) st;
                        Chest chest = null;
                        Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                        boolean admin = false;

                        if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
                            if (face.getState() instanceof Chest) {
                                chest = (Chest) face.getState();
                            }
                        } else {
                            admin = true;
                        }
                        String[] lines = s.getLines();

                        if (lines.length == 4 && lines[0] != null && lines[1] != null && lines[2] != null && lines[3] != null) {
                            if (lines[0].length() == 15 && lines[1].length() == 15 && lines[2].length() > 2 && lines[3].contains(":")) {
                                String id = lines[0] + lines[1];

                                id = id + lines[2].substring(0, 2);

                                id = id.replaceAll(
                                        "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                        "$1-$2-$3-$4-$5");

                                if (!CreateShop.isAlphaNumeric(id)) {
                                    break;
                                }
                                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(id));

                                if (p.hasPlayedBefore()) {

                                    String mat = lines[2].substring(3, lines[2].length());

                                    String[] split = lines[3].split(":");

                                    if (split.length == 5) {

                                        String[] r = mat.split("(?=\\p{Upper})");

                                        mat = r[0];
                                        for (int i = 0; i < r.length; i++) {
                                            if (i != 0)
                                                mat = mat + "_" + r[i];
                                        }

                                        mat = mat.toUpperCase();

                                        String q = split[0];
                                        try {
                                            Integer.parseInt(q);
                                        } catch (Exception e) {
                                            mat = mat + q.toUpperCase();
                                        }
                                        String sell = split[1];
                                        if (sell.equals("B")) {
                                            sell = "false";
                                        } else {
                                            sell = "true";
                                        }
                                        String data = split[2];
                                        String price = split[3];
                                        String amt = split[4];

                                        SignShopManager.addSign(s, new ItemStack(Material.valueOf(mat), 1, (byte) Integer.parseInt(data)), Double.parseDouble(price), Integer.parseInt(amt), p.getUniqueId(), Boolean.parseBoolean(sell), admin, chest);
                                        shops++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return shops;
    }
}
