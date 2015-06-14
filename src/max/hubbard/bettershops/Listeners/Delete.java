package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.ShopDeleter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Delete implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();


        if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {


            Shop shop = ShopManager.fromLocation(e.getBlock().getLocation());


            if (shop != null) {
                OfflinePlayer owner = shop.getOwner();
                if (owner == p || p.isOp() || (boolean) Config.getObject("Permissions") && Permissions.hasBreakPerm(p)) {

                    if ((boolean) Config.getObject("DeleteByBreak")) {

                        e.setCancelled(false);
                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                        ShopDeleter.deleteShopExternally(shop);


                    } else {
                        e.setCancelled(true);
                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyDeleteShop"));
                        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                            Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DenyDeleteShop"));

                        }
                    }
                } else {
                    e.setCancelled(true);
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyDeleteShop"));
                    if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                        Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DenyDeleteShop"));


                    }
                }
            }


        } else {
            if (b.getType() == Material.WALL_SIGN) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {

                            Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                            if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
                                if (face.getState() instanceof Chest) {
                                    Chest chest = (Chest) face.getState();

                                    Shop shop = ShopManager.fromLocation(chest.getLocation());

                                    if (shop != null) {
                                        OfflinePlayer owner = shop.getOwner();
                                        if (owner == p || p.isOp() || (boolean) Config.getObject("Permissions") && Permissions.hasBreakPerm(p)) {

                                            if ((boolean) Config.getObject("DeleteByBreak")) {

                                                e.setCancelled(false);
                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DeleteShop"));

                                                ShopDeleter.deleteShopExternally(shop);
                                            } else {
                                                e.setCancelled(true);
                                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyDeleteShop"));
                                                if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                    Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DenyDeleteShop"));


                                                }
                                            }
                                        } else {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "DenyDeleteShop"));
                                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                Core.getTitleManager().sendTitle(p, Language.getString("Messages", "DenyDeleteShop"));


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
