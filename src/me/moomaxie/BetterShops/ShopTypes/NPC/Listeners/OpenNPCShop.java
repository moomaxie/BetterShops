package me.moomaxie.BetterShops.ShopTypes.NPC.Listeners;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.ShopTypes.NPC.NPCs;
import me.moomaxie.BetterShops.ShopTypes.NPC.ShopsNPC;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;

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

            if (ent.getCustomName() != null && ent.getCustomName().contains("§a§l")) {

                Shop shop = ShopManager.fromString(ent.getCustomName().substring(4));

                if (shop != null) {

                    if (!shop.isNPCShop() || shop.getNPCShop() == null) {
                        shop.setNPCShop(true);
                        ShopsNPC n = new ShopsNPC(ent, shop);
                        n.removeChest();
                        n.returnNPC();
                        NPCs.addNPC(n);
                    }
                    e.setCancelled(true);

                    Player p = e.getPlayer();


                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop() || shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) && !shop.isServerShop() || shop.getOwner() != null && shop.getOwner().getUniqueId().equals(p.getUniqueId()) && !shop.isServerShop()) {
                        if (shop.getShopItems(false).size() != 0 || shop.getShopItems(false).size() == 0 && shop.getShopItems(true).size() == 0) {
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
                            if (shop.getShopItems(false).size() != 0 || shop.getShopItems(false).size() == 0 && shop.getShopItems(true).size() == 0) {
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
                            if (shop.getShopItems(false).size() != 0 || shop.getShopItems(false).size() == 0 && shop.getShopItems(true).size() == 0) {
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
                            p.closeInventory();
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntity(ChunkLoadEvent e) {
        if (!e.isNewChunk()) {
            for (Entity ent : e.getChunk().getEntities()) {
                if (ent instanceof LivingEntity) {
                    if (ent.getCustomName() != null) {
                        if (ent.getCustomName().contains("§a§l")) {

                            Shop shop = ShopManager.fromString(ent.getCustomName().substring(4));

                            if (shop != null) {

                                if (!shop.isNPCShop() || shop.getNPCShop() == null) {
                                    shop.setNPCShop(true);
                                    ShopsNPC n = new ShopsNPC((LivingEntity) ent, shop);
                                    n.removeChest();
                                    n.returnNPC();
                                    NPCs.addNPC(n);
                                } else {
                                    shop.getNPCShop().entity.remove();
                                    shop.getNPCShop().entity = (LivingEntity) ent;
                                    shop.getNPCShop().returnNPC();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
