package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.NPC.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class NPCOpen implements Listener {

    @EventHandler
    public void onOpen(final PlayerInteractEntityEvent e) {

        if (e.getRightClicked() instanceof LivingEntity) {
            final LivingEntity ent = (LivingEntity) e.getRightClicked();

            if (ent.getCustomName() != null && ent.getCustomName().contains("§a§l")) {

                final Shop shop = ShopManager.fromString(ent.getCustomName().substring(4));

                if (shop != null) {

                    e.setCancelled(true);

                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {

                            if (!shop.isNPCShop() || shop.getNPCShop() == null) {
                                shop.setObject("NPC", true);

                                try {
                                    EntityInfo info = EntityInfo.getInfo(ent);
                                    if (Core.useCitizens()) {
                                        ShopsNPC n = new CitizensShop(info, shop);
                                        n.spawn();
                                        n.removeChest();
                                        n.returnNPC();
                                        NPCManager.addNPCShop(n);
                                    } else {
                                        ShopsNPC n = new NPCShop(info.getType(), info.getLore(), shop, info.isBaby(), info.isSheared(), info.isVillagerZombie());
                                        n.removeChest();
                                        n.returnNPC();
                                        NPCManager.addNPCShop(n);
                                    }

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            e.setCancelled(true);

                            Player p = e.getPlayer();

                            if (!shop.getBlacklist().contains(p)) {
                                Opener.open(p,shop);
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotAllowed"));
                            }
                        }
                    });
                }
            }
        }
    }
}
