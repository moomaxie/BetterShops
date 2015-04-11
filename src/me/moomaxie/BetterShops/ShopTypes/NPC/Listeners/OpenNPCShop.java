package me.moomaxie.BetterShops.ShopTypes.NPC.Listeners;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.ShopTypes.NPC.NPCs;
import me.moomaxie.BetterShops.ShopTypes.NPC.ShopsNPC;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpenNPCShop implements Listener {

    @EventHandler
    public void onOpen(PlayerInteractEntityEvent e) {

        if (e.getRightClicked() instanceof LivingEntity) {
            LivingEntity ent = (LivingEntity) e.getRightClicked();

            if (ent.getCustomName() != null) {

                Shop shop = ShopLimits.fromString(ent.getCustomName().substring(4));

                if (shop != null) {

                    if (!shop.isNPCShop()) {
                        shop.setNPCShop(true);
                        ShopsNPC n = new ShopsNPC(ent,shop);
                        n.returnNPC();
                        NPCs.addNPC(n);
                    }
                    e.setCancelled(true);

                    Player p = e.getPlayer();

                    if (shop.getOwner() != null) {
                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) && !shop.isServerShop() || shop.getOwner() != null && shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                            e.setCancelled(true);
                            if (shop.getShopItems(false).size() >= shop.getShopItems(true).size()) {
                                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                            } else {
                                if (Config.useSellingShop()) {
                                    OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                } else {
                                    OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                }
                            }
                        } else {
                            if (shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) || shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                e.setCancelled(true);
                                if (shop.getShopItems(false).size() >= shop.getShopItems(true).size()) {
                                    OpenShop.openShopItems(null, p, shop, 1);
                                } else {
                                    if (Config.useSellingShop()) {
                                        OpenSellShop.openSellerShop(null, p, shop, 1);
                                    } else {
                                        OpenShop.openShopItems(null, p, shop, 1);
                                    }
                                }
                            }
                        }

                        if (!shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) && !shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                            if (shop.isOpen()) {
                                e.setCancelled(true);
                                if (shop.getShopItems(false).size() >= shop.getShopItems(true).size()) {
                                    OpenShop.openShopItems(null, p, shop, 1);
                                } else {
                                    if (Config.useSellingShop()) {
                                        OpenSellShop.openSellerShop(null, p, shop, 1);
                                    } else {
                                        OpenShop.openShopItems(null, p, shop, 1);
                                    }
                                }
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("OpenShop"));
                            } else {
                                e.setCancelled(true);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                            }
                        }
                    }
                }
            }
        }
    }
}
