package me.moomaxie.BetterShops.NPC.Listeners;

import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.NPC.NPCs;
import me.moomaxie.BetterShops.NPC.ShopsNPC;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * ***********************************************************************
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpenNPCShop implements Listener {

    @EventHandler
    public void onOpen(PlayerInteractEntityEvent e) {
        for (ShopsNPC npc : NPCs.getNPCs()) {
            if (e.getRightClicked().getType() == npc.getEntity().getType()) {
                LivingEntity ent = (LivingEntity) e.getRightClicked();


                if (ent.getCustomName() != null) {
                    if (ent.getCustomName().equals(npc.getEntity().getCustomName())) {

                        if (npc.getShop().isNPCShop()) {
                            e.setCancelled(true);

                            Player p = e.getPlayer();

                            if (npc.getShop().getOwner() != null) {

                                if (npc.getShop().getOwner().getUniqueId().equals(p.getUniqueId()) && !npc.getShop().isServerShop() || npc.getShop().getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) && !npc.getShop().isServerShop() || npc.getShop().getOwner() != null && npc.getShop().getOwner().getUniqueId().equals(p.getUniqueId()) && !npc.getShop().isServerShop()) {
                                    e.setCancelled(true);
                                    if (npc.getShop().getShopContents(false).size() >= npc.getShop().getShopContents(true).size()) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(null, p, npc.getShop(), 1);
                                    } else {
                                        OpenSellingOptions.openShopSellingOptions(null, p, npc.getShop(), 1);
                                    }
                                } else {
                                    if (npc.getShop().getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) || npc.getShop().getOwner().getUniqueId().equals(p.getUniqueId())) {
                                        e.setCancelled(true);
                                        if (npc.getShop().getShopContents(false).size() >= npc.getShop().getShopContents(true).size()) {
                                            OpenShop.openShopItems(null, p, npc.getShop(), 1);
                                        } else {
                                            OpenSellShop.openSellerShop(null, p, npc.getShop(), 1);
                                        }
                                    }
                                }

                                if (!npc.getShop().getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) && !npc.getShop().getOwner().getUniqueId().equals(p.getUniqueId())) {
                                    if (npc.getShop().isOpen()) {
                                        e.setCancelled(true);
                                        if (npc.getShop().getShopContents(false).size() >= npc.getShop().getShopContents(true).size()) {
                                            OpenShop.openShopItems(null, p, npc.getShop(), 1);
                                        } else {
                                            OpenSellShop.openSellerShop(null, p, npc.getShop(), 1);
                                        }
                                        p.sendMessage(Messages.getPrefix() + Messages.getOpenShopMessage());
                                    } else {
                                        e.setCancelled(true);
                                        p.sendMessage(Messages.getPrefix() + "Shop Is §cClosed");
                                    }
                                }


                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
