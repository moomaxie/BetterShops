package me.moomaxie.BetterShops.Listeners.Misc;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpeningChests implements Listener {

    @EventHandler
    public void onDoubleChestPlace(final BlockPlaceEvent e) {
        Player p = e.getPlayer();


        if (e.getBlock().getType() == Material.CHEST) {
            if (e.getBlock().getLocation().add(1, 0, 0).getBlock().getType() == Material.CHEST ||
                    e.getBlock().getLocation().add(-1, 0, 0).getBlock().getType() == Material.CHEST ||
                    e.getBlock().getLocation().add(0, 0, 1).getBlock().getType() == Material.CHEST ||
                    e.getBlock().getLocation().add(0, 0, -1).getBlock().getType() == Material.CHEST) {

                if (ShopLimits.fromLocation(e.getBlock().getLocation().add(0, 0, -1).getBlock().getLocation()) != null ||
                        ShopLimits.fromLocation(e.getBlock().getLocation().add(0, 0, 1).getBlock().getLocation()) != null ||
                        ShopLimits.fromLocation(e.getBlock().getLocation().add(1, 0, 0).getBlock().getLocation()) != null ||
                        ShopLimits.fromLocation(e.getBlock().getLocation().add(-1, 0, 0).getBlock().getLocation()) != null) {

                    if (Config.getAllowChest()) {
                        e.setCancelled(false);
                    } else {

                        e.setCancelled(true);
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotPlace"));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                e.getBlock().setType(Material.AIR);
                            }
                        },2L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();

            if (b.getType() == Material.CHEST) {

                Player p = e.getPlayer();

                Shop shop = ShopLimits.fromLocation(b.getLocation());

                if (shop != null) {

                    if (shop.getOwner() != null) {

                        if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                            if (Config.getAllowChest()) {
                                e.setCancelled(false);
                            } else {
                                e.setCancelled(true);
                                if (shop.isServerShop()) {
                                    if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                        OpenShop.openShopItems(null, p, shop, 1);
                                    } else {
                                        if (Config.useSellingShop()) {
                                            OpenSellShop.openSellerShop(null, p, shop, 1);
                                        } else {
                                            OpenShop.openShopItems(null, p, shop, 1);
                                        }
                                    }
                                } else {
                                    if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                        OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                    } else {
                                        if (Config.useSellingShop()) {
                                            OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                        }
                                    }
                                }
                            }
                        } else {
                            e.setCancelled(true);
                            if (shop.isOpen()) {
                                OpenShop.openShopItems(null, p, shop, 1);
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                            }
                        }
                    }
                }
            } else if (b.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) b.getState();
                Player p = e.getPlayer();
                if (sign.getLine(0).equals("§0" + MainGUI.getString("SignLine1"))
                        && sign.getLine(1).equals("§0" + MainGUI.getString("SignLine2"))
                        && sign.getLine(3).equals("§0" + MainGUI.getString("SignLine4")) ||
                        sign.getLine(0).equals("§0§0" + MainGUI.getString("SignLine1"))
                                && sign.getLine(1).equals("§0§0" + MainGUI.getString("SignLine2"))
                                && sign.getLine(3).equals("§0§0" + MainGUI.getString("SignLine4")) ||
                        sign.getLine(0).equals(MainGUI.getString("SignLine1"))
                        && sign.getLine(1).equals(MainGUI.getString("SignLine2"))
                        && sign.getLine(3).equals(MainGUI.getString("SignLine4"))) {

                    Block face = e.getClickedBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());
                    if (face.getType() == Material.CHEST) {
                        Shop shop = ShopLimits.fromLocation(face.getLocation());
                        if (shop != null) {
                            if (shop.getOwner() != null) {
                                if (shop.isOpen()) {
                                    if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString()) || shop.isServerShop()) {
                                        if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                            OpenShop.openShopItems(null, p, shop, 1);
                                        } else {
                                            if (Config.useSellingShop()) {
                                                OpenSellShop.openSellerShop(null, p, shop, 1);
                                            } else {
                                                OpenShop.openShopItems(null, p, shop, 1);
                                            }
                                        }
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("OpenShop"));
                                    } else if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                                        if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                        } else {
                                            if (Config.useSellingShop()) {
                                                OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                            } else {
                                                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                            }
                                        }
                                    }
                                } else {
                                    if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                    } else {
                                        if (!shop.isServerShop()) {
                                            if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                            } else {
                                                if (Config.useSellingShop()) {
                                                    OpenSellingOptions.openShopSellingOptions(null, p, shop, 1);
                                                } else {
                                                    OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                                }
                                            }
                                        } else {
                                            if (shop.getShopContents(false).size() >= shop.getShopContents(true).size()) {
                                                OpenShop.openShopItems(null, p, shop, 1);
                                            } else {
                                                if (Config.useSellingShop()) {
                                                    OpenSellShop.openSellerShop(null, p, shop, 1);
                                                } else {
                                                    OpenShop.openShopItems(null, p, shop, 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoOwner"));
                            }
                        }
                    }
                }
            }
        }
    }
}