package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Stocks {

    //Collect Stock

    public static void collectStock(ShopItem ite, int amt, Player p, Shop shop) {

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (amt > 0) {

                if (ite.getStock() != amt) {

                    int left = ite.getStock() - amt;

                    collectAll(ite, shop, p);
                    removeItemsFromInventory(ite, p, shop, left);

                    ite.setObject("Stock", left);

                } else {
                    collectAll(ite, shop, p);
                }
            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Zero"));
            }
        } else {
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyKeeper"));
        }


    }

    public static void addAll(ShopItem ite, Shop shop, Player p) {
        int amt = 0;

        ItemStack copy = ite.getItem().clone();

        for (int i = 1; i < ite.getItem().getMaxStackSize() + 1; i++) {

            copy.setAmount(i);

            for (ItemStack it : p.getInventory().all(copy).values()) {
                amt = amt + it.getAmount();
                p.getInventory().remove(copy);
            }
        }

        ite.setObject("Stock", ite.getStock() + amt);
        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeStock"));
    }

    public static void addStock(ShopItem ite, int orig, Player p, Shop shop) {

        if (orig > 0) {

            int amt = getNumberInInventory(ite, p, shop);

            if (amt >= orig) {

                removeItemsFromInventory(ite, p, shop, orig);

            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotEnoughItems"));
                return;
            }

        } else {
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Zero"));
            return;
        }
        ite.setObject("Stock", ite.getStock() + orig);

        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeStock"));
    }

    public static void removeStock(ShopItem ite, int orig, Player p, Shop shop) {

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (orig > 0) {

                if (ite.getStock() >= orig) {

                    addItemsToInventory(ite, p, orig);

                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                    return;
                }
            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Zero"));
                return;
            }
        } else {
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyKeeper"));
            return;
        }
        ite.setObject("Stock", ite.getStock() - orig);

        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeStock"));
    }

    public static void removeAll(ShopItem ite, Shop shop, Player p) {
        int amt = ite.getStock();

        int stacks = amt / ite.getItem().getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getItem().getMaxStackSize();

                ItemStack it = ite.getItem().clone();

                it.setAmount(ite.getItem().getMaxStackSize());

                p.getInventory().addItem(it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.getItem().clone();

            it.setAmount(amt);

            p.getInventory().addItem(it);

        }

        ite.setObject("Stock", 0);


        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeStock"));
    }

    public static void removeAllOfDeletedItem(ShopItem ite, Shop shop, Player p, boolean sell) {
        int amt = ite.getStock();

        addItemsToInventory(ite, p, amt);
    }

    public static void throwItemsOnGround(final ShopItem ite) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int amt = ite.getStock();

                int stacks = amt / ite.getItem().getMaxStackSize();

                if (stacks > 0) {
                    for (int i = 0; i < stacks; i++) {
                        amt = amt - ite.getItem().getMaxStackSize();

                        ItemStack it = ite.getItem().clone();

                        it.setAmount(ite.getItem().getMaxStackSize());


                        ite.getShop().getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(), it);
                    }
                }

                if (amt > 0) {
                    ItemStack it = ite.getItem().clone();

                    it.setAmount(amt);

                    ite.getShop().getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(), it);

                }
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public static void throwItemsOnGround(final ItemStack ite, final int amt, final Location l) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int stacks = amt / ite.getMaxStackSize();

                int c = amt;

                if (stacks > 0) {
                    for (int i = 0; i < stacks; i++) {
                        c = c - ite.getMaxStackSize();

                        ItemStack it = ite.clone();

                        it.setAmount(ite.getMaxStackSize());


                        l.getWorld().dropItemNaturally(l, it);
                    }
                }

                if (c > 0) {
                    ItemStack it = ite.clone();

                    it.setAmount(c);

                    l.getWorld().dropItemNaturally(l, it);

                }
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public static void collectAll(ShopItem ite, Shop shop, Player p) {
        int amt = ite.getStock();

        int stacks = amt / ite.getItem().getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getItem().getMaxStackSize();

                ItemStack it = ite.getItem().clone();

                it.setAmount(ite.getItem().getMaxStackSize());

                p.getInventory().addItem(it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.getItem().clone();

            it.setAmount(amt);

            p.getInventory().addItem(it);

        }

        ite.setObject("Stock", 0);
        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeStock"));
    }

    public static int getNumberInInventory(ShopItem it, Player p, Shop shop) {

        int amt = 0;

        ItemStack ite = it.getItem();

        int a = ite.getAmount();

        if (ite.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = ite.getItemMeta();

            List<String> lore = it.getLore();

            meta.setLore(lore);
            ite.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) ite.getItemMeta();
            List<String> lore = it.getLore();
            meta.setLore(lore);
            ite.setItemMeta(meta);
        }

        for (int i = 1; i < ite.getMaxStackSize() + 1; i++) {

            ite.setAmount(i);

            for (ItemStack itt : p.getInventory().all(ite).values()) {
                amt = amt + itt.getAmount();
            }
        }

        ite.setAmount(a);

        return amt;
    }

    public static int getNumberInInventory(ItemStack it, Player p) {

        int amt = 0;

        int a = it.getAmount();

        for (int i = 1; i < it.getMaxStackSize() + 1; i++) {

            it.setAmount(i);

            for (ItemStack itt : p.getInventory().all(it).values()) {
                amt = amt + itt.getAmount();
            }
        }
        it.setAmount(a);

        return amt;
    }

    public static int getNumberInInventory(ItemStack it, Chest p) {

        int amt = 0;

        int a = it.getAmount();

        for (int i = 1; i < it.getMaxStackSize() + 1; i++) {

            it.setAmount(i);

            for (ItemStack itt : p.getInventory().all(it).values()) {
                amt = amt + itt.getAmount();
            }
        }
        it.setAmount(a);


        return amt;
    }

    public static void addItemsToInventory(final ShopItem ite, final Player p, final int amt) {

        new BukkitRunnable() {

            @Override
            public void run() {

                int stacks = amt / ite.getItem().getMaxStackSize();

                int c = amt;

                if (stacks > 0) {
                    for (int i = 0; i < stacks; i++) {
                        c = c - ite.getItem().getMaxStackSize();

                        ItemStack it = ite.getItem().clone();

                        it.setAmount(ite.getItem().getMaxStackSize());

                        HashMap<Integer, ItemStack> carl = p.getInventory().addItem(it);

                        if (carl.size() > 0) {
                            for (ItemStack item : carl.values()) {
                                p.getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(), item);
                            }
                        }
                    }
                }

                if (c > 0) {
                    ItemStack it = ite.getItem().clone();

                    it.setAmount(c);

                    HashMap<Integer, ItemStack> carl = p.getInventory().addItem(it);

                    if (carl.size() > 0) {
                        for (ItemStack item : carl.values()) {
                            p.getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(), item);
                        }
                    }

                }
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public static void addItemsToInventory(final ItemStack ite, final Player p, final int amt) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int stacks = amt / ite.getMaxStackSize();

                int c = amt;
                if (stacks > 0) {
                    for (int i = 0; i < stacks; i++) {
                        c = c - ite.getMaxStackSize();

                        ItemStack it = ite.clone();

                        it.setAmount(ite.getMaxStackSize());

                        if (p.getInventory().firstEmpty() != -1) {

                            p.getInventory().addItem(it);
                        } else {
                            p.getLocation().getWorld().dropItem(p.getLocation().add(0, 1, 0), it);
                        }
                    }
                }

                if (c > 0) {
                    ItemStack it = ite.clone();

                    it.setAmount(c);

                    HashMap<Integer, ItemStack> carl = p.getInventory().addItem(it);

                    if (carl.size() > 0) {
                        for (ItemStack item : carl.values()) {
                            p.getLocation().getWorld().dropItem(p.getLocation().add(0,1,0), item);
                        }
                    }

                }
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public static void addItemsToInventory(final ItemStack ite, final Chest p, final int amt) {
        new BukkitRunnable() {

            @Override
            public void run() {
                int stacks = amt / ite.getMaxStackSize();

                int c = amt;

                if (stacks > 0) {
                    for (int i = 0; i < stacks; i++) {
                        c = c - ite.getMaxStackSize();

                        ItemStack it = ite.clone();

                        it.setAmount(ite.getMaxStackSize());

                        if (p.getInventory().firstEmpty() != -1) {

                            p.getInventory().addItem(it);
                        } else {
                            p.getLocation().getWorld().dropItem(p.getLocation().add(0,1,0), it);
                        }
                    }
                }

                if (c > 0) {
                    ItemStack it = ite.clone();

                    it.setAmount(c);

                    HashMap<Integer, ItemStack> carl = p.getInventory().addItem(it);

                    if (carl.size() > 0) {
                        for (ItemStack item : carl.values()) {
                            p.getLocation().getWorld().dropItem(p.getLocation().add(0,1,0), item);
                        }
                    }

                }
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    public static void removeItemsFromInventory(ShopItem ite, Player p, Shop shop, int orig) {
        if (getNumberInInventory(ite, p, shop) >= orig) {
            int o = orig;

            if (o > 0) {
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack it = p.getInventory().getItem(i);

                    if (it != null) {

                        if (o > 0) {

                            int u = it.getAmount();

                            it.setAmount(1);


                            if (it.equals(ite.getItem()) || it.toString().equals(ite.getItem().toString()) && it.getData().getData() == ite.getData() && it.getDurability() == ite.getDurability()) {

                                it.setAmount(u);

                                if (it.getAmount() > o) {
                                    if (o > 0) {
                                        int y = o;
                                        o = o - it.getAmount();
                                        it.setAmount(it.getAmount() - y);
                                    } else {
                                        break;
                                    }
                                } else if (it.getAmount() <= o) {
                                    if (o > 0) {
                                        o = o - it.getAmount();

                                        p.getInventory().setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                it.setAmount(u);
                            }

                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void removeItemsFromInventory(ItemStack ite, Chest p, int orig) {
        if (getNumberInInventory(ite, p) >= orig) {
            int o = orig;

            if (o > 0) {
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack it = p.getInventory().getItem(i);

                    if (it != null) {

                        if (o > 0) {

                            int u = it.getAmount();

                            it.setAmount(1);


                            if (it.equals(ite) || it.toString().equals(ite.toString()) && it.getData().getData() == ite.getData().getData() && it.getDurability() == ite.getDurability()) {

                                it.setAmount(u);

                                if (it.getAmount() > o) {
                                    if (o > 0) {
                                        int y = o;
                                        o = o - it.getAmount();
                                        it.setAmount(it.getAmount() - y);
                                    } else {
                                        break;
                                    }
                                } else if (it.getAmount() <= o) {
                                    if (o > 0) {
                                        o = o - it.getAmount();

                                        p.getInventory().setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                it.setAmount(u);
                            }

                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void removeItemsFromInventory(ItemStack ite, Player p, int orig) {
        if (getNumberInInventory(ite, p) >= orig) {
            int o = orig;

            if (o > 0) {
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack it = p.getInventory().getItem(i);

                    if (it != null) {

                        if (o > 0) {

                            int u = it.getAmount();

                            it.setAmount(1);


                            if (it.equals(ite) || it.toString().equals(ite.toString()) && it.getData().getData() == ite.getData().getData() && it.getDurability() == ite.getDurability()) {

                                it.setAmount(u);

                                if (it.getAmount() > o) {
                                    if (o > 0) {
                                        int y = o;
                                        o = o - it.getAmount();
                                        it.setAmount(it.getAmount() - y);
                                    } else {
                                        break;
                                    }
                                } else if (it.getAmount() <= o) {
                                    if (o > 0) {
                                        o = o - it.getAmount();

                                        p.getInventory().setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                it.setAmount(u);
                            }

                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean canAdd(ItemStack item, Inventory inv, int amt) {

        ItemStack itemToAdd = new ItemStack(item.getType(), amt, item.getData().getData());
        int freeSpace = 0;
        for (ItemStack i : inv) {
            if (i == null) {
                freeSpace += itemToAdd.getType().getMaxStackSize();
            } else if (i.getType() == itemToAdd.getType()) {
                freeSpace += i.getType().getMaxStackSize() - i.getAmount();
            }
        }
        return itemToAdd.getAmount() <= freeSpace;
    }
}