package me.moomaxie.BetterShops.Listeners.SellerOptions;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
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
public class OpenSellShop implements Listener {

    public static void openSellerShop(Inventory inv, Player p, Shop shop, int page) {

        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());
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

        if (!shop.isServerShop()) {
            optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner")+ " §a§l" + shop.getOwner().getName(),
                    MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
            options.setItemMeta(optionsMeta);
        } else {
            if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
                options.setItemMeta(optionsMeta);
            } else {
                optionsMeta.setLore(Arrays.asList( "§7" + shop.getDescription(), " ", MainGUI.getString("SellingShop"), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size(), " ", MainGUI.getString("OpenShopSettings"), " ", MainGUI.getString("TurnOffServerShop")));
                options.setItemMeta(optionsMeta);
            }
        }

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MainGUI.getString("Selling"));
        infoMeta.setLore(Arrays.asList(MainGUI.getString("ToggleShop"), MainGUI.getString("SearchOptions")));
        info.setItemMeta(infoMeta);

        ItemStack cart = new ItemStack(Material.CHEST);
        ItemMeta cartMeta = cart.getItemMeta();
        cartMeta.setDisplayName(MainGUI.getString("CheckoutDisplayName"));
        cartMeta.setLore(Arrays.asList(MainGUI.getString("CheckoutLore")));
        cart.setItemMeta(cartMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(MainGUI.getString("NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(MainGUI.getString("PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
        pg1.setItemMeta(pg1Meta);

        ItemStack pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg2Meta = pg2.getItemMeta();
        pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
        pg2.setItemMeta(pg2Meta);

        ItemStack pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta pg3Meta = pg3.getItemMeta();
        pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
        pg3.setItemMeta(pg3Meta);

        if (page == 1) {
            inv.setItem(8, arrow);
        }

        if (page == 2) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
            pg3.setItemMeta(pg3Meta);

            inv.setItem(0, barrow);
            inv.setItem(8, arrow);
        }

        if (page == 3) {
            pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg1Meta = pg1.getItemMeta();
            pg1Meta.setDisplayName(MainGUI.getString("Page") + " 1");
            pg1.setItemMeta(pg1Meta);

            pg2 = new ItemStack(Material.INK_SACK, 1, (byte) 8);
            pg2Meta = pg2.getItemMeta();
            pg2Meta.setDisplayName(MainGUI.getString("Page") + " 2");
            pg2.setItemMeta(pg2Meta);

            pg3 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
            pg3Meta = pg3.getItemMeta();
            pg3Meta.setDisplayName(MainGUI.getString("Page") + " 3");
            pg3.setItemMeta(pg3Meta);

            inv.setItem(0, barrow);
        }

        inv.setItem(3, info);
        inv.setItem(4, cart);
        inv.setItem(5, options);

        inv.setItem(12, pg1);
        inv.setItem(13, pg2);
        inv.setItem(14, pg3);

        for (ItemStack it : shop.getShopContents(true).keySet()) {
            int slot = shop.getShopContents(true).get(it);

            if (page == 1) {
                if (slot >= 18 && slot < 54) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = shop.getLore(it,true);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    lore.add(MainGUI.getString("AskingAmount") + " §7" + shop.getAmount(it, true));
                    lore.add(MainGUI.getString("AskingPrice") + " §7" + shop.getPrice(it, true));
                    lore.add(" ");
                    lore.add(MainGUI.getString("SellItem"));

                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    inv.setItem(slot, it);
                }
            } else if (page == 2) {
                if (slot >= 72 && slot < 108) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = shop.getLore(it,true);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    lore.add(MainGUI.getString("AskingAmount") + " §7" + shop.getAmount(it, true));
                    lore.add(MainGUI.getString("AskingPrice") + " §7" + shop.getPrice(it, true));
                    lore.add(" ");
                    lore.add(MainGUI.getString("SellItem"));
                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    slot = slot - 54;

                    inv.setItem(slot, it);
                }
            } else if (page == 3) {
                if (slot >= 126 && slot < 162) {
                    it.setAmount(1);
                    ItemMeta meta = it.getItemMeta();
                    List<String> lore;
                    if (meta.getLore() != null) {
                        lore = shop.getLore(it,true);
                    } else {
                        lore = new ArrayList<String>();
                    }

                    lore.add(MainGUI.getString("AskingAmount") + " §7" + shop.getAmount(it, true));
                    lore.add(MainGUI.getString("AskingPrice") + " §7" + shop.getPrice(it, true));
                    lore.add(" ");
                    lore.add(MainGUI.getString("SellItem"));
                    meta.setLore(lore);
                    it.setItemMeta(meta);

                    slot = slot - 108;

                    inv.setItem(slot, it);
                }
            }
        }

        if (!same)
            p.openInventory(inv);
    }
}
