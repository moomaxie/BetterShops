package me.moomaxie.BetterShops.NPC;

import BetterShops.Dev.API.Events.NPCShopCreateEvent;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CreateNPCWG {
    public static boolean createNPC(EntityType e, Shop shop) {


        if (Core.useWorldGuard() && Config.useNPCOverride()) {
            allowMobs(shop.getLocation());
        } else if (Core.useWorldGuard()) {
            com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(shop.getLocation());

            if (!set.allows(DefaultFlag.MOB_SPAWNING)) {

                shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("WorldGuardDenyNPC"));
                shop.setNPCShop(false);
                return false;
            }
        }

        ShopsNPC npc = new ShopsNPC(e, shop);
        if (shop.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(-1, 0, 0).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, 1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        if (shop.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
            Sign s = (Sign) shop.getLocation().getBlock().getRelative(0, 0, -1).getState();

            if (s.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                if (s.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                    if (s.getLine(1).contains(MainGUI.getString("SignLine2"))) {
                        s.getBlock().setType(Material.AIR);
                    }
                }
            }
        }

        shop.getLocation().getBlock().setType(Material.AIR);

        NPCs.addNPC(npc);

        if (Core.useWorldGuard() && Config.useNPCOverride()) {
            denyMobs(shop.getLocation());
        }

        NPCShopCreateEvent en = new NPCShopCreateEvent(shop);

        Bukkit.getPluginManager().callEvent(en);

        return true;
    }

    public static void allowMobs(Location l) {
        com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(l);

        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : set) {
            r.setFlag(DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
        }
    }

    public static void denyMobs(Location l) {
        com.sk89q.worldguard.protection.ApplicableRegionSet set = Core.getRegionSet(l);

        for (com.sk89q.worldguard.protection.regions.ProtectedRegion r : set) {
            r.setFlag(DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
        }
    }
}
