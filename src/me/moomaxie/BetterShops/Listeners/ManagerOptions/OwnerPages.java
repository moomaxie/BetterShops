package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * ***********************************************************************
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OwnerPages implements Listener {

    @EventHandler
    public void onSettingsClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    int page = 1;

                    if (e.getInventory().getItem(12) != null && e.getInventory().getItem(13) != null && e.getInventory().getItem(14) != null) {

                        if (e.getInventory().getItem(12).getData().getData() == (byte) 10) {
                            page = 1;
                        }
                        if (e.getInventory().getItem(13).getData().getData() == (byte) 10) {
                            page = 2;
                        }
                        if (e.getInventory().getItem(14).getData().getData() == (byte) 10) {
                            page = 3;
                        }

                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Buying"))) {


                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    if (page == 1) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 2);
                                    }
                                    if (page == 2) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 3) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                    }
                                } else {
                                    if (page == 1) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 2);
                                    }
                                    if (page == 2) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 3) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                    }

                                }
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    if (page == 1) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 2) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                    }
                                    if (page == 3) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 2);
                                    }
                                } else {
                                    if (page == 1) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 2) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                    }
                                    if (page == 3) {
                                        OpenShop.openShopItems(e.getInventory(), p, shop, 2);
                                    }
                                }
                            }

                            if (e.getSlot() == 12 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 1")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 1);
                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, 1);
                                }
                            }
                            if (e.getSlot() == 13 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 2")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 2);
                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, 2);
                                }
                            }
                            if (e.getSlot() == 14 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 3")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, 3);
                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, 3);
                                }
                            }
                        }
                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Selling"))) {

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    if (page == 1) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 2);
                                    }
                                    if (page == 2) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 3) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                    }
                                } else {
                                    if (page == 1) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 2);
                                    }
                                    if (page == 2) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 3) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                                    }

                                }
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    if (page == 1) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 2) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                    }
                                    if (page == 3) {
                                        OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 2);
                                    }
                                } else {
                                    if (page == 1) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 3);
                                    }
                                    if (page == 2) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                                    }
                                    if (page == 3) {
                                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 2);
                                    }
                                }
                            }

                            if (e.getSlot() == 12 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 1")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 1);
                                } else {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                                }
                            }
                            if (e.getSlot() == 13 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 2")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 2);
                                } else {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, 2);
                                }
                            }
                            if (e.getSlot() == 14 && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("Page") + " 3")) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, 3);
                                } else {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
