package me.moomaxie.BetterShops.Listeners.ManagerOptions;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.LayoutArrangement.ShopRearranger;
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
import org.bukkit.inventory.Inventory;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OwnerPages implements Listener {

    public static int getPage(Inventory inv) {
        if (inv.getName().contains(MainGUI.getString("ShopHeader"))) {

            if (inv.getType() == InventoryType.CHEST) {

                if (inv.getItem(13) != null && inv.getItem(13).getItemMeta() != null && inv.getItem(13).getItemMeta().getDisplayName() != null && inv.getItem(13).getItemMeta().getDisplayName().contains(MainGUI.getString("Page"))) {

                    return Integer.parseInt(inv.getItem(13).getItemMeta().getDisplayName().substring(MainGUI.getString("Page").length() + 3));

                }
            }

        }
        return 1;
    }

    @EventHandler
    public void onSettingsClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);


                    if (e.getInventory().getItem(13) != null && e.getInventory().getItem(13).getItemMeta() != null && e.getInventory().getItem(13).getItemMeta().getDisplayName() != null && e.getInventory().getItem(13).getItemMeta().getDisplayName().contains(MainGUI.getString("Page"))) {


                        int page = Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getDisplayName().substring(MainGUI.getString("Page").length() + 3));

                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Buying"))) {

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page + 1);

                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, page + 1);
                                }
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenShopOptions.openShopOwnerOptionsInventory(e.getInventory(), p, shop, page - 1);

                                } else {
                                    OpenShop.openShopItems(e.getInventory(), p, shop, page - 1);
                                }
                            }
                        }

                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("ArrangeBuying"))) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                ShopRearranger.openShopArranger(e.getInventory(),p,shop,page + 1, false);
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                ShopRearranger.openShopArranger(e.getInventory(),p,shop,page - 1, false);
                            }
                        }

                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("ArrangeSelling"))) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                ShopRearranger.openShopArranger(e.getInventory(),p,shop,page + 1, true);
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                ShopRearranger.openShopArranger(e.getInventory(),p,shop,page - 1, true);
                            }
                        }

                        if (e.getInventory().getItem(3).getItemMeta().getDisplayName() != null && e.getInventory().getItem(3).getItemMeta().getDisplayName().equals(MainGUI.getString("Selling"))) {

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, page + 1);

                                } else {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, page + 1);

                                }
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                                if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                                    OpenSellingOptions.openShopSellingOptions(e.getInventory(), p, shop, page - 1);
                                } else {
                                    OpenSellShop.openSellerShop(e.getInventory(), p, shop, page - 1);

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
