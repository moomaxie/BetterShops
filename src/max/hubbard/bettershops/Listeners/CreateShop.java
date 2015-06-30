package max.hubbard.bettershops.Listeners;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Events.ShopCreateEvent;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.AddShop;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.AnvilManager;
import max.hubbard.bettershops.Utils.CreationCost;
import max.hubbard.bettershops.Utils.WorldGuardStuff;
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

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CreateShop implements Listener {

    @EventHandler
    public void onSign(final SignChangeEvent e) {
        final Player p = e.getPlayer();

        Chest chest = null;
        if (e.getLine(0).equalsIgnoreCase(Language.getString("MainGUI", "ShopCreate")) || e.getLine(0).equalsIgnoreCase(ChatColor.BLACK + Language.getString("MainGUI", "ShopCreate"))) {

            boolean can = true;
            boolean wgCan;

            if ((boolean) Config.getObject("Permissions") && !Permissions.hasCreatePerm(p)) {
                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","NoPermission"));
                return;
            }

            if ((boolean) Config.getObject("Creation Limit") && (boolean) Config.getObject("Permissions") && !Permissions.hasLimitPerm(p) && ShopManager.atLimit(p) || (boolean) Config.getObject("Creation Limit") && !p.isOp() && !(boolean) Config.getObject("Permissions") && ShopManager.atLimit(p)) {
                can = false;
            }

            wgCan = !Core.useWorldGuard() || WorldGuardStuff.checkCreateShop(e.getBlock().getLocation());
            if (can) {

                if (wgCan) {
                    if (e.getBlock().getType() == Material.WALL_SIGN) {

                        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();

                        Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                        if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
                            if (face.getState() instanceof Chest) {
                                chest = (Chest) face.getState();
                            }

                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "MustBeAttached"));
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
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "WorldGuardDenyShop"));
                    return;
                }
                final Chest finalChest = chest;

                if (finalChest != null && ShopManager.fromLocation(finalChest.getLocation()) == null) {
                    final AnvilManager man = new AnvilManager(p);
                    Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {
                            String name = man.call();
                            if (isAlphaNumeric(name)) {
                                boolean ca = true;
                                boolean Long = false;

                                if (name.length() > 16) {
                                    Long = true;
                                }

                                if (ShopManager.fromString(name) != null) {
                                    ca = false;
                                }

                                if (ca && !Long) {
                                    if (CreationCost.useCost(p)) {

                                        new AddShop(e.getPlayer(), finalChest, name);
                                        e.getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "CreateShop"));

                                        e.setLine(0, Language.getString("MainGUI", "SignLine1"));
                                        e.setLine(1, Language.getString("MainGUI", "SignLine2"));
                                        e.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                                        e.setLine(3, Language.getString("MainGUI", "SignLine4"));

                                        if (e.getBlock().getState() instanceof Sign) {
                                            org.bukkit.block.Sign s = (org.bukkit.block.Sign) e.getBlock().getState();

                                            s.setLine(0, Language.getString("MainGUI", "SignLine1"));
                                            s.setLine(1, Language.getString("MainGUI", "SignLine2"));
                                            s.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                                            s.setLine(3, Language.getString("MainGUI", "SignLine4"));

                                            s.update();
                                        }

                                        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Language.getString("Messages", "CreateShop"));


                                        }


                                        ShopCreateEvent e = new ShopCreateEvent(ShopManager.fromLocation(finalChest.getLocation()));
                                        Bukkit.getPluginManager().callEvent(e);


                                        if ((boolean) Config.getObject("Auto Add")) {

                                            if (finalChest.getBlockInventory() != null) {
                                                Shop shop = ShopManager.fromLocation(finalChest.getLocation());
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

                                                            shopItem.setObject("Stock", shopItem.getStock() + amt);

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
                                        e.getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LongName"));
                                        e.setLine(0, " ");
                                        e.setLine(1, " ");
                                        e.setLine(2, " ");
                                        e.setLine(3, " ");
                                        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Language.getString("Messages", "LongName"));

                                        }
                                    }

                                    if (!ca) {
                                        e.getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NameTaken"));
                                        e.setLine(0, " ");
                                        e.setLine(1, " ");
                                        e.setLine(2, " ");
                                        e.setLine(3, " ");
                                        if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Language.getString("Messages", "NameTaken"));

                                        }
                                    }
                                }
                            } else {
                                e.getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ImproperName"));
                                e.setLine(0, " ");
                                e.setLine(1, " ");
                                e.setLine(2, " ");
                                e.setLine(3, " ");
                                if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                                    Core.getTitleManager().setTimes(p, 20, 40, 20);
                                    Core.getTitleManager().sendSubTitle(p, Language.getString("Messages", "ImproperName"));


                                }
                                e.setCancelled(true);
                            }
                        }
                    });

                }
            } else {
                p.sendMessage(Language.getString("Messages","Prefix") + Language.getString("Messages","AtLimit"));
            }
        }
    }

    public static boolean isAlphaNumeric(String str) {
        if (str.trim().length() < 1) {
            return false;
        }
        String acceptable = "abcdefghijklmnopqrstuvwxyz0123456789 &$_-";
        for (int i = 0; i < str.length(); i++) {
            if (!acceptable.contains(str.substring(i, i + 1).toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static Shop createShopExternally(Location loc, String name, OfflinePlayer owner) {
        if (isAlphaNumeric(name)) {
            boolean can = true;
            boolean Long = false;

            if (name.length() > 16) {
                Long = true;
            }

            if (ShopManager.fromString(name) != null){
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

                        s.setLine(0, Language.getString("MainGUI", "SignLine1"));
                        s.setLine(1, Language.getString("MainGUI", "SignLine2"));
                        s.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                        s.setLine(3, Language.getString("MainGUI", "SignLine4"));

                        s.update();
                    }
                }

                Shop s = ShopManager.fromLocation(loc);

                ShopCreateEvent e = new ShopCreateEvent(s);

                Bukkit.getPluginManager().callEvent(e);

                return s;
            }

        }
        return null;
    }
}
