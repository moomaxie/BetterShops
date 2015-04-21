package me.moomaxie.BetterShops.ShopTypes.NPC.Listeners;

import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.ShopTypes.NPC.NPCs;
import me.moomaxie.BetterShops.ShopTypes.NPC.ShopsNPC;
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

        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity ent = (LivingEntity) e.getEntity();

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

                }
            }
        }
    }

    @EventHandler
    public void onDamageByNPC(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player){
            if (e.getDamager() instanceof LivingEntity){
                if (((LivingEntity) e.getDamager()).getCustomName() != null && ((LivingEntity) e.getDamager()).getCustomName().contains("§a§l")){
                    Shop shop = ShopManager.fromString(((LivingEntity) e.getDamager()).getCustomName().substring(4));
                    if (shop != null){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
