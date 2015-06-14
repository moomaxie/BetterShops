package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Location;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class WorldGuardStuff {

    public static void allowMobs(Location l) {


        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : Core.getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l)) {
            r.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
        }
    }

    public static void denyMobs(Location l) {

        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : Core.getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l)) {
            r.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
        }
    }

    public static boolean checkNPCOverride(Shop shop) {
        if (Core.useWorldGuard() && (boolean) Config.getObject("NPCOverride")) {
            WorldGuardStuff.allowMobs(shop.getLocation());
        } else if (Core.useWorldGuard()) {

            if (!Core.getWorldGuard().getRegionManager(shop.getLocation().getWorld()).getApplicableRegions(shop.getLocation()).allows(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING)) {
                shop.setObject("NPC", false);
                return false;
            }
        }
        return true;
    }

    public static boolean checkCreateShop(Location l) {
        if (Core.useWorldGuard() && (boolean) Config.getObject("EnableAllowShopsFlag")) {

            if (Core.getWorldGuard().getRegionManager(l.getWorld()).getApplicableRegions(l).allows(com.sk89q.worldguard.protection.flags.DefaultFlag.ENABLE_SHOP)) {
                return true;
            }
        } else if (Core.useWorldGuard() && !(boolean) Config.getObject("EnableAllowShopsFlag")) {
            return true;
//                ApplicableRegionSet set = Core.getRegionSet(e.getBlock().getLocation());

//                if (!set.allows(com.sk89q.worldguard.protection.flags.DefaultFlag.ENABLE_SHOP)){
//                    wgCan = false;
//                }
        } else {
            return true;
        }
        return false;
    }

}
