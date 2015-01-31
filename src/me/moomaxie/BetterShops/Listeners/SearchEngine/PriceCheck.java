package me.moomaxie.BetterShops.Listeners.SearchEngine;

import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright me.moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class PriceCheck {

    public static void searchByPrice(Inventory inv,Player p, Shop shop, double id, boolean sell) {


        openShopWithSearch(inv,p, shop, 1, sell, id);
    }

    private static void openShopWithSearch(Inventory inv,Player p, Shop shop, int page, boolean sell, double id) {

        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, "§7[Shop]" + " §a" + shop.getName());
        } else {
            inv.clear();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName("§a§l" + shop.getName());

        optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", "§e§lOwner: §a" + shop.getOwner().getName(),
                "§eKeepers: §7" + shop.getManagers().size()));
        options.setItemMeta(optionsMeta);

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        if (sell) {
            infoMeta.setDisplayName("§e§lSelling");
        } else {
            infoMeta.setDisplayName("§e§lBuying");
        }
        infoMeta.setLore(Arrays.asList("§e§lRight Click §7to toggle §aBuying/Selling", "§e§lLeft Click §7to open §aSearch Options"));
        info.setItemMeta(infoMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("§e§lNext Page");
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName("§e§lPrevious Page");
        barrow.setItemMeta(barrowMeta);

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName("§dPage 1");
        pg1.setItemMeta(pg1Meta);

        ItemStack pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg2Meta = pg2.getItemMeta();
        pg2Meta.setDisplayName("§dPage 2");
        pg2.setItemMeta(pg2Meta);

        ItemStack pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg3Meta = pg3.getItemMeta();
        pg3Meta.setDisplayName("§dPage 3");
        pg3.setItemMeta(pg3Meta);

        if (page == 1) {
            inv.setItem(8, arrow);
        }

        if (page == 2) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName("§dPage 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName("§dPage 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName("§dPage 3");
            pg3.setItemMeta(pg3Meta);

            inv.setItem(0, barrow);
            inv.setItem(8, arrow);
        }

        if (page == 3) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName("§dPage 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName("§dPage 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName("§dPage 3");
            pg3.setItemMeta(pg3Meta);

            inv.setItem(0, barrow);
        }

        inv.setItem(3, info);
        inv.setItem(5, options);

        inv.setItem(12, pg1);
        inv.setItem(13, pg2);
        inv.setItem(14, pg3);

        for (ItemStack it : shop.getShopContents(sell).keySet()) {

            if (shop.getPrice(it, sell) == id) {

                if (page == 1) {
                    if (inv.firstEmpty() > 0) {
                        ItemMeta meta = it.getItemMeta();
                        List<String> lore;
                        if (meta.getLore() != null) {
                            lore = meta.getLore();
                        } else {
                            lore = new ArrayList<String>();
                        }

                        if (sell) {

                            lore.add("§eAsking Amount: §7" + shop.getAmount(it, true));
                            lore.add("§eAsking Price: §7" + shop.getPrice(it, true));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to sell");
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        } else {
                            if (shop.isInfinite(it, false)) {
                                lore.add("§eStock: §7-");
                            } else {
                                lore.add("§eStock: §7" + shop.getStock(it, false));
                            }
                            lore.add("§eAmount: §7" + shop.getAmount(it, false));
                            lore.add("§ePrice: §7" + shop.getPrice(it, false));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to buy");

                            if (shop.getManagers().contains(p)) {
                                lore.add("§e§lRight Click §7to manage");
                            }
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        }

                        inv.setItem(inv.firstEmpty(), it);
                    }
                } else if (page == 2) {
                    if (inv.firstEmpty() > 0) {
                        ItemMeta meta = it.getItemMeta();
                        List<String> lore;
                        if (meta.getLore() != null) {
                            lore = meta.getLore();
                        } else {
                            lore = new ArrayList<String>();
                        }

                        if (sell) {

                            lore.add("§eAsking Amount: §7" + shop.getAmount(it, true));
                            lore.add("§eAsking Price: §7" + shop.getPrice(it, true));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to sell");
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        } else {
                            if (shop.isInfinite(it, false)) {
                                lore.add("§eStock: §7-");
                            } else {
                                lore.add("§eStock: §7" + shop.getStock(it, false));
                            }
                            lore.add("§eAmount: §7" + shop.getAmount(it, false));
                            lore.add("§ePrice: §7" + shop.getPrice(it, false));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to buy");

                            if (shop.getManagers().contains(p)) {
                                lore.add("§e§lRight Click §7to manage");
                            }
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        }


                        inv.setItem(inv.firstEmpty(), it);
                    }
                } else if (page == 3) {
                    if (inv.firstEmpty() > 0) {
                        ItemMeta meta = it.getItemMeta();
                        List<String> lore;
                        if (meta.getLore() != null) {
                            lore = meta.getLore();
                        } else {
                            lore = new ArrayList<String>();
                        }

                        if (sell) {

                            lore.add("§eAsking Amount: §7" + shop.getAmount(it, true));
                            lore.add("§eAsking Price: §7" + shop.getPrice(it, true));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to sell");
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        } else {
                            if (shop.isInfinite(it, false)) {
                                lore.add("§eStock: §7-");
                            } else {
                                lore.add("§eStock: §7" + shop.getStock(it, false));
                            }
                            lore.add("§eAmount: §7" + shop.getAmount(it, false));
                            lore.add("§ePrice: §7" + shop.getPrice(it, false));
                            lore.add(" ");
                            lore.add("§e§lLeft Click §7to buy");

                            if (shop.getManagers().contains(p)) {
                                lore.add("§e§lRight Click §7to manage");
                            }
                            meta.setLore(lore);
                            it.setItemMeta(meta);
                        }


                        inv.setItem(inv.firstEmpty(), it);
                    }
                }
            }
        }

        if (!same)
            p.openInventory(inv);
    }
}
