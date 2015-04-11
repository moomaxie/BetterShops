package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.Blacklist;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddItemManager implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAddItem(final InventoryClickEvent e) {

        if (e.getInventory().getType() == InventoryType.CHEST) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                ItemStack ite = e.getCurrentItem();

                final Player p = (Player) e.getWhoClicked();

                if (e.isLeftClick()) {
                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("Stock")) && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("Amount")) && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("Price"))) {
                        return;
                    }

                    if (p.getInventory().contains(ite) && e.getRawSlot() >= 54) {

                        if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory().getName().contains(MainGUI.getString("ShopHeader"))) {

                            String name = p.getOpenInventory().getTopInventory().getName();
                            name = name.substring(11);

                            final Shop shop = ShopLimits.fromString(p, name);

                            int slot = e.getSlot();

                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {

                                if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Buying"))) {

                                    ItemStack item = ite.clone();

                                    item.setAmount(1);

                                    if (!Blacklist.isBlacklisted(item) || Blacklist.isBlacklisted(item) && Permissions.hasBlacklistPerm(p)) {

                                        if (ShopItem.fromItemStack(shop, item, false) == null) {
                                            int page = shop.getNextAvailablePage(false);
                                            int s = shop.getNextSlotForPage(page, false);

                                            ShopItem sItem = shop.createShopItem(item, s, page, false);

                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                            int am = ite.getAmount() - 1;

                                            if (shop.isServerShop()) {
                                                OpenShop.openShopItems(e.getInventory(), p, shop, page);
                                            } else {
                                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page);
                                            }


                                            if (am > 0) {
                                                ite.setAmount(am);
                                            } else {
                                                p.getInventory().setItem(slot, new ItemStack(Material.AIR));
                                            }
                                        } else {

                                            ShopItem shopItem = ShopItem.fromItemStack(shop, item, false);
                                            Stocks.addAll(shopItem, shop, p);
                                            if (shop.isServerShop()) {
                                                OpenShop.openShopItems(e.getInventory(), p, shop, shopItem.getPage());
                                            } else {
                                                OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, shopItem.getPage());
                                            }
                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);
                                        }
                                    } else {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("Blacklist"));
                                    }
                                } else if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Selling")) && Config.useSellingShop()) {

                                    ItemStack item = ite.clone();

                                    item.setAmount(1);

                                    if (!Blacklist.isBlacklisted(item) || Blacklist.isBlacklisted(item) && Permissions.hasBlacklistPerm(p)) {

                                        if (ShopItem.fromItemStack(shop, item, true) == null) {
                                            int page = shop.getNextAvailablePage(true);
                                            int s = shop.getNextSlotForPage(page, true);

                                            ShopItem sItem = shop.createShopItem(item, s, page, true);

                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                            if (shop.isServerShop()) {
                                                OpenSellShop.openSellerShop(e.getInventory(), p, shop, page);
                                            } else {
                                                OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, page);
                                            }

                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AlreadyHave"));
                                        }
                                    } else {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("Blacklist"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
