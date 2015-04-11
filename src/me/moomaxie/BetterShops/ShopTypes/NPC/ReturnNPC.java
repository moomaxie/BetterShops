package me.moomaxie.BetterShops.ShopTypes.NPC;

import org.bukkit.Bukkit;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ReturnNPC {

    public static void startReturnNPC() {


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            public void run() {

                for (ShopsNPC npc : NPCs.getNPCs()) {

                    if (npc.getShop() != null) {
                        npc.returnNPC();
                    } else {
                        NPCs.removeNPC(npc);
                    }
                }

            }
        }, 0L, 80L);
    }
}
