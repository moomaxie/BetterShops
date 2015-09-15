package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.NPC.CitizensShop;
import max.hubbard.bettershops.Shops.Types.NPC.EntityInfo;
import max.hubbard.bettershops.Shops.Types.NPC.NPCManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CitizensStuff implements Listener {

    public static void deleteCitizensNPC(LivingEntity entity) {
        if (Core.useCitizens()) {
            if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                npc.destroy();
                CitizensAPI.getNPCRegistry().deregister(npc);
            }
        }
    }

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent e) {
        if (e.getNPC().getEntity() instanceof LivingEntity) {
            if (e.getNPC().getName() != null && e.getNPC().getName().contains("§a§l")) {
                Shop shop = ShopManager.fromString(e.getNPC().getName().substring(4));

                if (shop != null) {
                    if (shop.getNPCShop() == null) {
                        CitizensShop s = new CitizensShop(e.getNPC(), (LivingEntity) e.getNPC().getEntity(), shop);
                        NPCManager.addNPCShop(s);
                        s.removeChest();
                        shop.setObject("NPC", true);
                        if (shop.getObject("NPCInfo") != null) {
                            EntityInfo in = EntityInfo.fromString((String) shop.getObject("NPCInfo"));
                            s.setInfo(in);
                        }
                    } else {

                        if (shop.getNPCShop().getEntity() != null && shop.getNPCShop().getEntity().isValid() && !shop.getNPCShop().getEntity().equals(e.getNPC().getEntity())){
                            e.setCancelled(true);
                            deleteCitizensNPC((LivingEntity) e.getNPC().getEntity());
                        }

                    }
                }
            }
        }
    }
}
