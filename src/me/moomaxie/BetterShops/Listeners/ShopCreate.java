package me.moomaxie.BetterShops.Listeners;

import BetterShops.Dev.API.Events.ShopCreateEvent;
import me.moomaxie.BetterShops.Configurations.AnvilGUI;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.GUIMessages.SearchEngine;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.CreationCost.CreationCost;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Shops.AddShop;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopCreate implements Listener {

    @EventHandler
    public void onSign(final SignChangeEvent e) {
        final Player p = e.getPlayer();

        Chest chest = null;
        if (e.getLine(0).equalsIgnoreCase(MainGUI.getString("ShopCreate")) || e.getLine(0).equalsIgnoreCase(ChatColor.BLACK + MainGUI.getString("ShopCreate"))) {

            boolean can = true;

            if (Config.usePerms() && !Permissions.hasCreatePerm(p)) {
                can = false;
            }

            if (Config.useLimit() && Config.usePerms() && !Permissions.hasLimitPerm(p) && ShopLimits.atLimit(p) || Config.useLimit() && !p.isOp() && !Config.usePerms() && ShopLimits.atLimit(p)) {
                can = false;
            }


            if (can) {


                if (e.getBlock().getType() == Material.WALL_SIGN) {

                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();

                    Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                    if (face.getType() == Material.CHEST) {
                        if (face.getState() instanceof Chest) {
                            chest = (Chest) face.getState();
                        }

                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("MustBeAttached"));
                        e.setLine(0, " ");
                        e.setLine(1, " ");
                        e.setLine(2, " ");
                        e.setLine(3, " ");
                        e.setCancelled(true);
                        return;
                    }

                } else {
                    return;
                }


                final Chest finalChest = chest;

                if (finalChest != null && ShopLimits.fromLocation(finalChest.getLocation()) == null) {

                    if (Config.useAnvil()) {

                        AnvilGUI gui = Core.getAnvilGUI();

                        gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
                            @Override
                            public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                                if (ev.getSlot() == 2) {
                                    ev.setWillClose(true);
                                    ev.setWillDestroy(true);


                                    if (ev.getCurrentItem().getType() == Material.PAPER) {
                                        if (ev.getCurrentItem().hasItemMeta()) {
                                            if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                                String name = ev.getCurrentItem().getItemMeta().getDisplayName();

                                                if (isAlphaNumeric(name)) {
                                                    boolean can = true;
                                                    boolean Long = false;

                                                    if (name.length() > 16) {
                                                        Long = true;
                                                    }

                                                    if (ShopLimits.fromString(name) != null){
                                                        can = false;
                                                    }

                                                    if (can && !Long) {
                                                        if (CreationCost.useCost(p)) {

                                                            new AddShop(e.getPlayer(), finalChest, name);
                                                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("CreateShop"));

                                                            e.setLine(0, MainGUI.getString("SignLine1"));
                                                            e.setLine(1, MainGUI.getString("SignLine2"));
                                                            e.setLine(2, MainGUI.getString("SignLine3Closed"));
                                                            e.setLine(3, MainGUI.getString("SignLine4"));

                                                            if (e.getBlock().getState() instanceof Sign) {
                                                                org.bukkit.block.Sign s = (org.bukkit.block.Sign) e.getBlock().getState();

                                                                s.setLine(0, MainGUI.getString("SignLine1"));
                                                                s.setLine(1, MainGUI.getString("SignLine2"));
                                                                s.setLine(2, MainGUI.getString("SignLine3Closed"));
                                                                s.setLine(3, MainGUI.getString("SignLine4"));

                                                                s.update();
                                                            }

                                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                Core.getTitleManager().sendTitle(p, Messages.getString("CreateShop"));


                                                            }


                                                            ShopCreateEvent e = new ShopCreateEvent(ShopLimits.fromLocation(finalChest.getLocation()));

                                                            Bukkit.getPluginManager().callEvent(e);


//                                                    ShopLimits.loadShops();

                                                            if (Config.autoAddItems()) {

                                                                if (finalChest.getBlockInventory() != null) {
                                                                    Shop shop = ShopLimits.fromLocation(finalChest.getLocation());
                                                                    int i = 18;
                                                                    for (final ItemStack items : finalChest.getBlockInventory().getContents()) {
                                                                        if (items != null && items.getType() != Material.AIR) {

                                                                            int am = items.getAmount();

                                                                            items.setAmount(1);

                                                                            int page = shop.getNextAvailablePage(false);
                                                                            int sl = shop.getNextSlotForPage(page, false);

                                                                            ShopItem shopItem = shop.createShopItem(items, sl, page, false);

                                                                            items.setAmount(am);

                                                                            if (items.getAmount() > 1) {
                                                                                int amt = items.getAmount();

                                                                                amt = amt - 1;

                                                                                shopItem.setStock(shopItem.getStock() + amt);

                                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                                                    public void run() {
                                                                                        for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                                                            if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                                                if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                                                    finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                    }
                                                                                }, 5L);

                                                                            } else {
                                                                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                                                    public void run() {
                                                                                        for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                                                            if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                                                if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                                                    finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                    }
                                                                                }, 5L);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    } else {
                                                        if (Long) {
                                                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("LongName"));
                                                            e.setLine(0, " ");
                                                            e.setLine(1, " ");
                                                            e.setLine(2, " ");
                                                            e.setLine(3, " ");
                                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                Core.getTitleManager().sendTitle(p, Messages.getString("LongName"));

                                                            }
                                                        }

                                                        if (!can) {
                                                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NameTaken"));
                                                            e.setLine(0, " ");
                                                            e.setLine(1, " ");
                                                            e.setLine(2, " ");
                                                            e.setLine(3, " ");
                                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                Core.getTitleManager().sendTitle(p, Messages.getString("NameTaken"));

                                                            }
                                                        }
                                                    }

                                                } else {
                                                    e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperName"));
                                                    e.setLine(0, " ");
                                                    e.setLine(1, " ");
                                                    e.setLine(2, " ");
                                                    e.setLine(3, " ");
                                                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendSubTitle(p, Messages.getString("ImproperName"));


                                                    }
                                                    e.setCancelled(true);
                                                }
                                            }
                                        } else {
                                            e.getPlayer().sendMessage(Messages.getString("Prefix") + "§4ERROR: §cMalfunction with Shop Name Creating, is the plugin updated?");
                                            e.setLine(0, " ");
                                            e.setLine(1, " ");
                                            e.setLine(2, " ");
                                            e.setLine(3, " ");
                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                Core.getTitleManager().sendTitle(p, Messages.getString("Error"));
                                                Core.getTitleManager().sendSubTitle(p, "§cMalfunction with Shop Name Creating, is the plugin updated?");


                                            }
                                            e.setCancelled(true);
                                        }
                                    } else {
                                        e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("CreationCancelled"));
                                        e.setLine(0, " ");
                                        e.setLine(1, " ");
                                        e.setLine(2, " ");
                                        e.setLine(3, " ");
                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Messages.getString("CreationCancelled"));

                                        }
                                    }
                                } else {
                                    ev.setWillClose(false);
                                    ev.setWillDestroy(false);
                                }
                            }
                        });

                        ItemStack it = new ItemStack(Material.PAPER);
                        ItemMeta meta = it.getItemMeta();
                        meta.setDisplayName(SearchEngine.getString("EnterName"));
                        it.setItemMeta(meta);

                        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);

                        gui.open();
                    } else {
                        ChatMessages.shopCreate.put(p, finalChest);
                        ChatMessages.shopCreate2.put(p, e.getBlock());
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChatMessage"));

                    }
                } else {
                    e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("LocationTaken"));
                    e.setLine(0, " ");
                    e.setLine(1, " ");
                    e.setLine(2, " ");
                    e.setLine(3, " ");
                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                        Core.getTitleManager().sendSubTitle(p, Messages.getString("Sorry"));
                        Core.getTitleManager().sendSubTitle(p, Messages.getString("LocationTaken"));

                    }
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                e.setLine(0, " ");
                e.setLine(1, " ");
                e.setLine(2, " ");
                e.setLine(3, " ");
                if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                    Core.getTitleManager().sendTitle(p, Messages.getString("NoPermission"));


                }
            }
        }
    }

    public static boolean isAlphaNumeric(String str) {
        if (str.trim().length() < 1) {
            return false;
        }
        String acceptable = "abcdefghijklmnopqrstuvwxyz0123456789 &/$";
        for (int i = 0; i < str.length(); i++) {
            if (!acceptable.contains(str.substring(i, i + 1).toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static void createShopExternally(Location loc, String name, OfflinePlayer owner) {
        if (isAlphaNumeric(name)) {
            boolean can = true;
            boolean Long = false;

            if (name.length() > 16) {
                Long = true;
            }

            if (ShopLimits.fromString(name) != null){
                can = false;
            }

            if (can && !Long) {

                new AddShop(owner, loc, name);
                loc.getBlock().setType(Material.CHEST);

                Chest chest = (Chest) loc.getBlock().getState();

                org.bukkit.material.Chest c = (org.bukkit.material.Chest) chest.getData();

                BlockFace face = c.getFacing();

                c.setFacingDirection(face.getOppositeFace());

                chest.setData(c);

                chest.update();

                Block b = chest.getBlock().getRelative(face.getOppositeFace());

                if (b != null) {

                    b.setType(Material.WALL_SIGN);


                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();

                        org.bukkit.material.Sign sign = (org.bukkit.material.Sign) s.getData();

                        sign.setFacingDirection(face.getOppositeFace());

                        s.setData(sign);

                        s.setLine(0, MainGUI.getString("SignLine1"));
                        s.setLine(1, MainGUI.getString("SignLine2"));
                        s.setLine(2, MainGUI.getString("SignLine3Closed"));

                        s.setLine(3, MainGUI.getString("SignLine4"));

                        s.update();
                    }
                }

                ShopCreateEvent e = new ShopCreateEvent(ShopLimits.fromLocation(loc));

                Bukkit.getPluginManager().callEvent(e);


            }

        }
    }
}
