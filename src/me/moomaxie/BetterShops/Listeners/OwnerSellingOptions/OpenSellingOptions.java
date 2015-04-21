package me.moomaxie.BetterShops.Listeners.OwnerSellingOptions;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.History;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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
public class OpenSellingOptions implements Listener {

    @EventHandler
    public void onSettings(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(MainGUI.getString("ShopHeader").length());

                    Shop shop = ShopManager.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ToggleShop"))) {

                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {

                            if (e.isRightClick()) {
                                if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Buying")) && Config.useSellingShop()) {
                                    openShopSellingOptions(e.getInventory(), p, shop, 1);
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Selling"))) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                }
                            }
                        } else {
                            if (e.isRightClick()) {
                                if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Buying")) && Config.useSellingShop()) {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                                } else if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Selling"))) {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void openShopSellingOptions(Inventory inv, Player p, Shop shop, int page) {

        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());
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
        if (shop.isOpen()) {
            optionsMeta.setDisplayName(MainGUI.getString("ShopInfoDisplayNameOpen"));
        } else {
            optionsMeta.setDisplayName(MainGUI.getString("ShopInfoDisplayNameClosed"));
        }

        if (Permissions.hasArrangePerm(p)) {
            optionsMeta.setLore(Arrays.asList("§a" + shop.getName(), "§7" + shop.getDescription(), " ", MainGUI.getString("SellingShop"), " ", MainGUI.getString("Owner") + " §a" + shop.getOwner().getName(),
                    MainGUI.getString("Keepers") + " §7" + shop.getManagers().size(), " ", MainGUI.getString("OpenShopSettings"), MainGUI.getString("ToggleOpenAndClosed"), MainGUI.getString("ManageItems"),
                    MainGUI.getString("AddItemToShop"), MainGUI.getString("ClickAddItem"), " ", MainGUI.getString("ArrangementMode")));
            options.setItemMeta(optionsMeta);
        } else {
            optionsMeta.setLore(Arrays.asList("§a" + shop.getName(), "§7" + shop.getDescription(), " ", MainGUI.getString("SellingShop"), " ", MainGUI.getString("Owner") + " §a" + shop.getOwner().getName(),
                    MainGUI.getString("Keepers") + " §7" + shop.getManagers().size(), " ", MainGUI.getString("OpenShopSettings"), MainGUI.getString("ToggleOpenAndClosed"), MainGUI.getString("ManageItems"),
                    MainGUI.getString("AddItemToShop"), MainGUI.getString("ClickAddItem")));
            options.setItemMeta(optionsMeta);
        }

        ItemStack keepers = new ItemStack(Material.NAME_TAG);
        ItemMeta keepersMeta = keepers.getItemMeta();
        keepersMeta.setDisplayName(MainGUI.getString("AddItem"));
        keepersMeta.setLore(Arrays.asList(MainGUI.getString("AddItemLore")));
        keepers.setItemMeta(keepersMeta);

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MainGUI.getString("Selling"));
        infoMeta.setLore(Arrays.asList(MainGUI.getString("ToggleShop")));
        info.setItemMeta(infoMeta);

        ItemStack history = new ItemStack(Material.EMERALD);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(History.getString("History"));
        historyMeta.setLore(Arrays.asList(History.getString("OpenHistory")));
        history.setItemMeta(historyMeta);

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
        pg1Meta.setDisplayName(MainGUI.getString("Page") + "§7 " + page);
        pg1.setItemMeta(pg1Meta);


        inv.setItem(3, info);
        inv.setItem(4, options);
        inv.setItem(5, keepers);
        inv.setItem(7, history);

        inv.setItem(13, pg1);


        if (page > 1) {
            inv.setItem(0, barrow);
        }


        inv.setItem(8, arrow);

        for (ShopItem it : shop.getShopItems(true)) {
            if (it.getPage() == page) {
                ItemStack itemStack = it.getItem().clone();
                List<String> lore = new ArrayList<>();
                ItemMeta meta = itemStack.getItemMeta();
                if (it.getLore() != null) {
                    for (String s : it.getLore()) {
                        lore.add(s);
                    }
                }
                if (it.isInfinite()) {
                    lore.add(MainGUI.getString("Stock") + " §7-");
                } else {
                    lore.add(MainGUI.getString("Stock") + " §7" + it.getStock());
                }
                lore.add(MainGUI.getString("AskingAmount") + " §7" + it.getAmount());
                if (!it.getLiveEco()) {
                    lore.add(MainGUI.getString("AskingPrice") + " §7" + it.getPriceAsString());
                } else {
                    if (it.getAdjustedPrice() != it.getOrigPrice()) {
                        lore.add(MainGUI.getString("AskingPrice") + " §c§m" + it.getOrigPrice() + " §a" + it.getAdjustedPriceAsString());
                    } else {
                        lore.add(MainGUI.getString("AskingPrice") + " §7" + it.getPriceAsString());
                    }
                }
                lore.add(" ");
                lore.add(MainGUI.getString("SellOptions"));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inv.setItem(it.getSlot(), itemStack);
            }
        }

        if (!same)
            p.openInventory(inv);
    }
}
