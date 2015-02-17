package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Stocks {

    //Collect Stock

    public static void collectStock(ItemStack ite, int amt, Player p, Shop shop) {

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (amt > 0) {

                if (shop.getStock(ite,true) != amt) {

                    int left = shop.getStock(ite,true) - amt;

                    collectAll(ite,shop,p);
                    removeItemsFromInventory(ite,p,shop,left);

                    shop.setStock(ite,left,true);

                } else {
                    collectAll(ite,shop,p);
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + "Must be greater than §c0");
            }
        } else {
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyKeeper"));
        }


    }

    public static void addAll(ItemStack ite, Shop shop, Player p) {
        int amt = 0;

        if (ite.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = ite.getItemMeta();

            List<String> lore = shop.getLore(ite);

            meta.setLore(lore);
            ite.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) ite.getItemMeta();
            List<String> lore = shop.getLore(ite);
            meta.setLore(lore);
            ite.setItemMeta(meta);
        }

        for (int i = 1; i < ite.getMaxStackSize() + 1; i++) {

            ite.setAmount(i);

            for (ItemStack it : p.getInventory().all(ite).values()) {
                amt = amt + it.getAmount();
                p.getInventory().remove(ite);
            }
        }


        shop.setStock(ite, shop.getStock(ite, false) + amt, false);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void addStock(ItemStack ite, int orig, Player p, Shop shop) {

        int o = orig;

        if (orig > 0) {

            int amt = 0;

            if (ite.getType() != Material.SKULL_ITEM) {
                ItemMeta meta = ite.getItemMeta();

                List<String> lore = shop.getLore(ite);

                meta.setLore(lore);
                ite.setItemMeta(meta);
            } else {
                SkullMeta meta = (SkullMeta) ite.getItemMeta();
                List<String> lore = shop.getLore(ite);
                meta.setLore(lore);
                ite.setItemMeta(meta);
            }

            for (int i = 1; i < ite.getMaxStackSize() + 1; i++) {

                ite.setAmount(i);

                for (ItemStack it : p.getInventory().all(ite).values()) {
                    amt = amt + it.getAmount();
                }
            }

            ite.setAmount(1);

            if (amt >= orig) {

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

            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NotEnoughItems"));
                return;
            }

        } else {
            p.sendMessage(Messages.getString("Prefix") + "Must be greater than §c0");
            return;
        }

        shop.setStock(ite, shop.getStock(ite, false) + orig, false);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void removeStock(ItemStack ite, int orig, Player p, Shop shop){

        int o = orig;

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (orig > 0) {

                if (shop.getStock(ite, false) >= orig) {

                    int stacks = orig / ite.getMaxStackSize();

                    for (int i = 0; i < stacks; i++) {
                        o = o - ite.getMaxStackSize();

                        ItemStack it = ite.clone();

                        it.setAmount(ite.getMaxStackSize());

                        p.getInventory().addItem(it);
                    }

                    if (o > 0) {
                        ItemStack it = ite.clone();

                        it.setAmount(o);
                        p.getInventory().addItem(it);
                    }

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("LowStock"));
                    return;
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + "Must be greater than §c0");
                return;
            }
        } else {
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyKeeper"));
            return;
        }
        shop.setStock(ite, shop.getStock(ite, false) - orig, false);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void removeAll(ItemStack ite, Shop shop, Player p) {
        int amt = shop.getStock(ite, false);

        int stacks = amt / ite.getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getMaxStackSize();

                ItemStack it = ite.clone();

                it.setAmount(ite.getMaxStackSize());

                p.getInventory().addItem(it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.clone();

            it.setAmount(amt);

            p.getInventory().addItem(it);

        }

        shop.setStock(ite, 0, false);


        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void removeAllOfDeletedItem(ItemStack ite, Shop shop, Player p, boolean sell) {
        int amt = shop.getStock(ite, sell);

        int stacks = amt / ite.getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getMaxStackSize();

                ItemStack it = ite.clone();

                it.setAmount(ite.getMaxStackSize());


                p.getInventory().addItem(it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.clone();

            it.setAmount(amt);

            p.getInventory().addItem(it);

        }
    }

    public static void collectAll(ItemStack ite, Shop shop, Player p) {
        int amt = shop.getStock(ite, true);

        int stacks = amt / ite.getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getMaxStackSize();

                ItemStack it = ite.clone();

                it.setAmount(ite.getMaxStackSize());

                p.getInventory().addItem(it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.clone();

            it.setAmount(amt);

            p.getInventory().addItem(it);

        }

        shop.setStock(ite, 0, true);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static int getNumberInInventory(ItemStack ite, Player p, Shop shop){

        int amt = 0;

        int a = ite.getAmount();

        if (ite.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = ite.getItemMeta();

            List<String> lore = shop.getLore(ite);

            meta.setLore(lore);
            ite.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) ite.getItemMeta();
            List<String> lore = shop.getLore(ite);
            meta.setLore(lore);
            ite.setItemMeta(meta);
        }

        for (int i = 1; i < ite.getMaxStackSize() + 1; i++) {

            ite.setAmount(i);

            for (ItemStack it : p.getInventory().all(ite).values()) {
                amt = amt + it.getAmount();
            }
        }

        ite.setAmount(a);

        return amt;
    }

    public static void removeItemsFromInventory(ItemStack ite, Player p, Shop shop, int orig){
        if (getNumberInInventory(ite,p,shop) >= orig){
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
}
