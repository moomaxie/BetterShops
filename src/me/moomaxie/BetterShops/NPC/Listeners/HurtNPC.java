package me.moomaxie.BetterShops.NPC.Listeners;

import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.NPC.NPCs;
import me.moomaxie.BetterShops.NPC.ShopsNPC;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class HurtNPC implements Listener {

    @EventHandler
    public void onHurt(EntityDamageEvent e) {

        for (ShopsNPC npc : NPCs.getNPCs()) {
            if (e.getEntity().getType() == npc.getEntity().getType()) {
                LivingEntity ent = (LivingEntity) e.getEntity();


                if (ent.getCustomName() != null) {
                    if (ent.getCustomName().equals(npc.getEntity().getCustomName())) {

                        if (npc.getShop() != null) {
                            if (npc.getShop().isNPCShop()) {
                                e.setCancelled(true);
                                break;
                            }
                        } else {
                            ent.remove();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageByNPC(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player){
            if (e.getDamager() instanceof LivingEntity){
                if (((LivingEntity) e.getDamager()).getCustomName() != null){
                    Shop shop = ShopLimits.fromString(((LivingEntity) e.getDamager()).getCustomName().substring(4));
                    if (shop != null){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
