package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
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

    public static void collectStock(ShopItem ite, int amt, Player p, Shop shop) {

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (amt > 0) {

                if (ite.getStock() != amt) {

                    int left = ite.getStock() - amt;

                    collectAll(ite, shop, p);
                    removeItemsFromInventory(ite, p, shop, left);

                    ite.setStock(left);

                } else {
                    collectAll(ite, shop, p);
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
            }
        } else {
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyKeeper"));
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

        ite.setStock(ite.getStock() + amt);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void addStock(ShopItem ite, int orig, Player p, Shop shop) {

        int o = orig;

        if (orig > 0) {

            int amt = 0;

            for (int i = 1; i < ite.getItem().getMaxStackSize() + 1; i++) {

                ite.setAmount(i);

                for (ItemStack it : p.getInventory().all(ite.getItem()).values()) {
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

            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NotEnoughItems"));
                return;
            }

        } else {
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
            return;
        }
        ite.setStock(ite.getStock() + orig);

        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void removeStock(ShopItem ite, int orig, Player p, Shop shop) {

        int o = orig;

        if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

            if (orig > 0) {

                if (ite.getStock() >= orig) {

                    int stacks = orig / ite.getItem().getMaxStackSize();

                    for (int i = 0; i < stacks; i++) {
                        o = o - ite.getItem().getMaxStackSize();

                        ItemStack it = ite.getItem().clone();

                        it.setAmount(ite.getItem().getMaxStackSize());

                        p.getInventory().addItem(it);
                    }

                    if (o > 0) {
                        ItemStack it = ite.getItem().clone();

                        it.setAmount(o);
                        p.getInventory().addItem(it);
                    }

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("LowStock"));
                    return;
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
                return;
            }
        } else {
            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyKeeper"));
            return;
        }
        ite.setStock(ite.getStock() - orig);

        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
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

        ite.setStock(0);


        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static void removeAllOfDeletedItem(ShopItem ite, Shop shop, Player p, boolean sell) {
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
    }

    public static void throwItemsOnGround(ShopItem ite){
        int amt = ite.getStock();

        int stacks = amt / ite.getItem().getMaxStackSize();

        if (stacks > 0) {
            for (int i = 0; i < stacks; i++) {
                amt = amt - ite.getItem().getMaxStackSize();

                ItemStack it = ite.getItem().clone();

                it.setAmount(ite.getItem().getMaxStackSize());


                ite.getShop().getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(),it);
            }
        }

        if (amt > 0) {
            ItemStack it = ite.getItem().clone();

            it.setAmount(amt);

            ite.getShop().getLocation().getWorld().dropItemNaturally(ite.getShop().getLocation(),it);

        }
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

        ite.setStock(0);
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeStock"));
    }

    public static int getNumberInInventory(ItemStack ite, Player p, Shop shop) {

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

    public static void removeItemsFromInventory(ShopItem ite, Player p, Shop shop, int orig) {
        if (getNumberInInventory(ite.getItem(), p, shop) >= orig) {
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
}
