package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.Stocks;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopMaintainer implements Listener {

    @EventHandler
    public void addHopper(final InventoryMoveItemEvent e) {
        if (e.getDestination().getHolder() instanceof Chest) {
            final Chest chest = (Chest) e.getDestination().getHolder();
            Shop shop = ShopManager.fromLocation(chest.getLocation());
            if (shop != null) {
                ShopItem item;
                if (shop instanceof FileShop) {
                    item = FileShopItem.fromItemStack(shop, e.getItem(), false);
                } else {
                    item = SQLShopItem.fromItemStack(shop, e.getItem(), false);
                }


                if (item != null) {

                    item.setObject("Stock", item.getStock() + e.getItem().getAmount());
                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {
                            Stocks.removeItemsFromInventory(e.getItem(), chest, e.getItem().getAmount());
                        }
                    });
                }
            }
        }
    }
}
