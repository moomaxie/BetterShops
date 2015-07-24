package max.hubbard.bettershops.Shops.Types.NPC;

import max.hubbard.bettershops.Core;
import org.bukkit.Bukkit;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ReturnNPC {

    public static void beginReturning() {
        if (!Core.useCitizens())
            Bukkit.getScheduler().runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                @Override
                public void run() {
                    for (ShopsNPC shopsNPC : NPCManager.getNPCShops()) {
                        shopsNPC.returnNPC();
                    }
                }
            }, 0L, 20L);
    }
}
