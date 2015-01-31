package me.moomaxie.BetterShops.NPC;

import org.bukkit.Bukkit;

import java.util.List;

/**
 * ***********************************************************************
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ReturnNPC {

    static List<ShopsNPC> npcs;

    public static void startReturnNPC() {


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            public void run() {

                npcs = NPCs.getNPCs();

                for (ShopsNPC npc : npcs) {

                    if (npc.getShop() != null && npc.getShop().isNPCShop()) {

                        npc.returnNPC();
                    }
                }

            }
        }, 0L, 80L);
    }
}
