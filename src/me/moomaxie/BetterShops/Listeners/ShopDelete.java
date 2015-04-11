package me.moomaxie.BetterShops.Listeners;

import BetterShops.Dev.API.Events.ShopDeleteEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopDelete implements Listener {

    @EventHandler
    public void onDelete(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();


        if (b.getType() == Material.CHEST) {

            File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");
            if (file != null) {
                if (file.listFiles() != null) {
                    for (File f : file.listFiles()) {
                        if (f.getName().contains(".yml")) {
                            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                            try {
                                for (String s : config.getKeys(false)) {
                                    String l = config.getConfigurationSection(s).getString("Location");

                                    if (l != null) {

                                        String[] locs = l.split(" ");

                                        World w = Bukkit.getWorld(locs[0]);

                                        double x = Double.parseDouble(locs[1]);
                                        double y = Double.parseDouble(locs[2]);
                                        double z = Double.parseDouble(locs[3]);

                                        if (b.getLocation().equals(new Location(w, x, y, z))) {
                                            Player owner = Bukkit.getPlayer(UUID.fromString(config.getConfigurationSection(s).getString("Owner")));
                                            Shop shop = ShopLimits.fromLocation(e.getBlock().getLocation());

                                            if (shop != null) {
                                                if (owner == p || p.isOp() || Config.usePerms() && Permissions.hasBreakPerm(p)) {

                                                    if (Config.useDeleteByBreak()) {

                                                        e.setCancelled(false);
                                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                                        ShopDeleteEvent ev = new ShopDeleteEvent(shop);
                                                        Bukkit.getPluginManager().callEvent(ev);

                                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                            Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                                        }


                                                        for (ShopItem item : shop.getShopItems(false)) {
                                                            Stocks.removeAllOfDeletedItem(item, shop, p, false);
                                                        }
                                                        for (ShopItem item : shop.getShopItems(true)) {
                                                            Stocks.removeAllOfDeletedItem(item, shop, p, true);
                                                        }

                                                        config.set(s, null);
                                                        try {
                                                            config.save(f);
                                                        } catch (IOException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        ShopLimits.locs.remove(shop.getLocation());
                                                        ShopLimits.names.remove(shop.getName());
                                                        ShopLimits.shops.remove(shop);

                                                        int amt = ShopLimits.getLimits().get(owner.getUniqueId());
                                                        ShopLimits.getLimits().put(owner.getUniqueId(), amt - 1);

                                                        List<Shop> li = ShopLimits.getShopsForPlayer(owner);
                                                        li.remove(shop);
                                                        ShopLimits.playerShops.put(owner.getUniqueId(), li);


                                                        if (Core.useSQL()) {
                                                            Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                                        }

                                                    } else {
                                                        e.setCancelled(true);
                                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyDeleteShop"));
                                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                            Core.getTitleManager().sendTitle(p, Messages.getString("DenyDeleteShop"));

                                                        }
                                                    }
                                                } else {
                                                    e.setCancelled(true);
                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyDeleteShop"));
                                                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, Messages.getString("DenyDeleteShop"));


                                                    }
                                                }
                                                break;
                                            } else {
                                                p.sendMessage(Messages.getString("Prefix") + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted.");
                                            }
                                        }
                                    }
                                }
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                                p.sendMessage(Messages.getString("Prefix") + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted. Check the Console for more details.");
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            if (b.getType() == Material.WALL_SIGN) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getBlock().getState();

                if (sign.getLine(0).contains(MainGUI.getString("SignLine1"))) {
                    if (sign.getLine(3).contains(MainGUI.getString("SignLine4"))) {
                        if (sign.getLine(1).contains(MainGUI.getString("SignLine2"))) {

                            Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                            if (face.getType() == Material.CHEST) {
                                if (face.getState() instanceof Chest) {
                                    Chest chest = (Chest) face.getState();

                                    File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");
                                    if (file != null) {

                                        if (file.listFiles() != null) {

                                            for (File f : file.listFiles()) {
                                                if (f.getName().contains(".yml")) {
                                                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                                                    try {
                                                        for (String s : config.getKeys(false)) {
                                                            String l = config.getConfigurationSection(s).getString("Location");

                                                            String[] locs = l.split(" ");

                                                            World w = Bukkit.getWorld(locs[0]);

                                                            double x = Double.parseDouble(locs[1]);
                                                            double y = Double.parseDouble(locs[2]);
                                                            double z = Double.parseDouble(locs[3]);

                                                            if (chest.getLocation().equals(new Location(w, x, y, z))) {

                                                                Shop shop = ShopLimits.fromLocation(new Location(w, x, y, z));
                                                                Player owner = Bukkit.getPlayer(UUID.fromString(config.getConfigurationSection(s).getString("Owner")));

                                                                if (shop == null) {
                                                                    ShopLimits.loadShops();
                                                                    shop = ShopLimits.fromLocation(new Location(w, x, y, z));
                                                                }


                                                                if (shop != null) {

                                                                    if (owner == p || p.isOp() || Config.usePerms() && Permissions.hasBreakPerm(p)) {

                                                                        if (Config.useDeleteByBreak()) {

                                                                            e.setCancelled(false);
                                                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                                                            ShopDeleteEvent ev = new ShopDeleteEvent(shop);
                                                                            Bukkit.getPluginManager().callEvent(ev);

                                                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                                Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                                                            }

                                                                            for (ShopItem item : shop.getShopItems(false)) {
                                                                                Stocks.removeAllOfDeletedItem(item, shop, p, false);
                                                                            }
                                                                            for (ShopItem item : shop.getShopItems(true)) {
                                                                                Stocks.removeAllOfDeletedItem(item, shop, p, true);
                                                                            }

                                                                            config.set(s, null);
                                                                            try {
                                                                                config.save(f);
                                                                            } catch (IOException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                            ShopLimits.locs.remove(shop.getLocation());
                                                                            ShopLimits.names.remove(shop.getName());
                                                                            ShopLimits.shops.remove(shop);

                                                                            int amt = ShopLimits.getLimits().get(owner.getUniqueId());
                                                                            ShopLimits.getLimits().put(owner.getUniqueId(), amt - 1);

                                                                            List<Shop> li = ShopLimits.getShopsForPlayer(owner);
                                                                            li.remove(shop);
                                                                            ShopLimits.playerShops.put(owner.getUniqueId(), li);

                                                                            if (Core.useSQL()) {
                                                                                Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                                                            }
                                                                        } else {
                                                                            e.setCancelled(true);
                                                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyDeleteShop"));
                                                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                                Core.getTitleManager().sendTitle(p, Messages.getString("DenyDeleteShop"));


                                                                            }
                                                                        }
                                                                    } else {
                                                                        e.setCancelled(true);
                                                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyDeleteShop"));
                                                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                            Core.getTitleManager().sendTitle(p, Messages.getString("DenyDeleteShop"));


                                                                        }
                                                                    }

                                                                    break;
                                                                } else {

                                                                    p.sendMessage(Messages.getString("Prefix") + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted.");
                                                                }
                                                            }
                                                        }
                                                    } catch (NullPointerException ex) {
                                                        ex.printStackTrace();
                                                        p.sendMessage(Messages.getString("Prefix") + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted. Check the Console for details.");
                                                    } catch (SQLException e1) {
                                                        e1.printStackTrace();
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
    }

    public static void deleteShopExternally(Shop shop) {
        ShopDeleteEvent ev = new ShopDeleteEvent(shop);
        Bukkit.getPluginManager().callEvent(ev);

        shop.getLocation().getBlock().setType(Material.AIR);

        for (ShopItem item : shop.getShopItems()) {
            Stocks.throwItemsOnGround(item);
        }

        shop.config.set(shop.getName(), null);
        try {
            shop.config.save(shop.file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ShopLimits.locs.remove(shop.getLocation());
        ShopLimits.names.remove(shop.getName());
        ShopLimits.shops.remove(shop);
        if (shop.getOwner() != null) {
            int amt = ShopLimits.getLimits().get(shop.getOwner().getUniqueId());
            ShopLimits.getLimits().put(shop.getOwner().getUniqueId(), amt - 1);

            List<Shop> li = ShopLimits.getShopsForPlayer(shop.getOwner());
            li.remove(shop);
            ShopLimits.playerShops.put(shop.getOwner().getUniqueId(), li);
        }

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
