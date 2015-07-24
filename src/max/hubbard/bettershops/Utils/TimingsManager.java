package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;

import java.util.Date;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class TimingsManager {

    Shop shop;

    public TimingsManager(Shop shop) {
        this.shop = shop;
    }

    public void startTime() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
//                for (Shop shop : ShopManager.getShops()) {
                try {
                    for (ShopItem item : shop.getShopItems()) {
                        if (item.isAutoStock()) {
                            if (item.getAutoStockTiming().update()) {
                                item.setObject("Stock", item.getAutoStockTiming().getObject());
                                item.getAutoStockTiming().refreshTime();
                                item.setObject("AutoStock", item.getAutoStockTiming().toString());
                            }
                        }

                        if (item.isTransCooldown()) {
                            if (item.getTransCooldownTiming().update()) {
                                item.setObject("Cooldowns", "");
                                item.getTransCooldownTiming().refreshTime();
                                item.setObject("TransCool", item.getTransCooldownTiming().toString());
                            }
                        }

                    }

                    if (Config.getObject("RemoveAfter") != null && (int) Config.getObject("RemoveAfter") != 0) {
                        if (shop.getShopItems().size() == 0) {
                            if (shop.getObject("Removal") == null || shop.getObject("Removal") != null && shop.getObject("Removal").equals("")) {
                                shop.setObject("Removal", new Date().getTime());
                            }
                        } else {
                            if (shop.getObject("Removal") == null || shop.getObject("Removal") != null && !shop.getObject("Removal").equals("")) {
                                shop.setObject("Removal", "");
                            }
                        }
                        if (shop.getObject("Removal") != null) {

                            if (!shop.getObject("Removal").equals("")) {
                                Date dt2 = new Date();
                                Date dt1 = new Date(Long.valueOf((String) shop.getObject("Removal")));

                                int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));

                                if (diffInDays >= (int) Config.getObject("RemoveAfter")) {
                                    if (!shop.isNPCShop() && !shop.isHoloShop()) {

                                        if (shop.getSign() != null) {
                                            Sign sign = shop.getSign();
                                            sign.setLine(0, "");
                                            sign.setLine(1, "");
                                            sign.setLine(2, "");
                                            sign.setLine(3, "");
                                            sign.update();
                                        }
                                    }
                                    ShopDeleter.deleteShopExternally(shop);
                                }
                            }

                        } else {
                            shop.setObject("Removal", "");
                        }
                    }
                } catch (Exception ignored) {

                }
            }
//            }
        }, 0L, 20L);
    }
}
