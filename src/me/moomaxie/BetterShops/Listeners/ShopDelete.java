package me.moomaxie.BetterShops.Listeners;

import BetterShops.Dev.API.Events.ShopDeleteEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Shops.Shop;
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
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
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

                                            if (shop == null) {
                                                ShopLimits.loadShops();
                                                shop = ShopLimits.fromLocation(new Location(w, x, y, z));
                                            }

                                            if (shop != null) {
                                                if (owner == p || p.isOp() || Config.usePerms() && Permissions.hasBreakPerm(p)) {

                                                    if (Config.useDeleteByBreak()) {

                                                        e.setCancelled(false);
                                                        p.sendMessage(Messages.getPrefix() + Messages.getRemoveShop());

                                                        ShopDeleteEvent ev = new ShopDeleteEvent(shop);
                                                        Bukkit.getPluginManager().callEvent(ev);

                                                        if (Core.isAboveEight() && Config.useTitles()) {


                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                            Core.getTitleManager().sendTitle(p, Messages.getRemoveShop());


                                                        }


                                                        for (ItemStack item : shop.getShopContents(false).keySet()) {
                                                            if (item.getType() != Material.AIR) {
                                                                item.setAmount(shop.getStock(item, false));
                                                                b.getWorld().dropItem(b.getLocation(), item);
                                                            }
                                                        }
                                                        for (ItemStack item : shop.getShopContents(true).keySet()) {
                                                            if (shop.getStock(item, true) > 0) {
                                                                if (item.getType() != Material.AIR) {
                                                                    item.setAmount(shop.getStock(item, true));
                                                                    b.getWorld().dropItem(b.getLocation(), item);
                                                                }
                                                            }
                                                        }

                                                        config.set(s, null);
                                                        try {
                                                            config.save(f);
                                                        } catch (IOException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        ShopLimits.loadShops();
                                                    } else {
                                                        e.setCancelled(true);
                                                        p.sendMessage(Messages.getPrefix() + Messages.getDenyRemoveShop());
                                                        if (Core.isAboveEight() && Config.useTitles()) {


                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                            Core.getTitleManager().sendTitle(p, Messages.getDenyRemoveShop());


                                                        }
                                                    }
                                                } else {
                                                    e.setCancelled(true);
                                                    p.sendMessage(Messages.getPrefix() + Messages.getDenyRemoveShop());
                                                    if (Core.isAboveEight() && Config.useTitles()) {


                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, Messages.getDenyRemoveShop());


                                                    }
                                                }
                                                break;
                                            } else {
                                                p.sendMessage(Messages.getPrefix() + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted.");
                                            }
                                        }
                                    }
                                }
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                                p.sendMessage(Messages.getPrefix() + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted. Check the Console for more details.");
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
                                                                            p.sendMessage(Messages.getPrefix() + Messages.getRemoveShop());

                                                                            ShopDeleteEvent ev = new ShopDeleteEvent(shop);
                                                                            Bukkit.getPluginManager().callEvent(ev);

                                                                            if (Core.isAboveEight() && Config.useTitles()) {

                                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                                Core.getTitleManager().sendTitle(p, Messages.getRemoveShop());


                                                                            }

                                                                            if (shop.getShopContents(false) != null) {

                                                                                for (ItemStack item : shop.getShopContents(false).keySet()) {
                                                                                    if (chest.getInventory().firstEmpty() > -1) {
                                                                                        if (item.getType() != Material.AIR) {
                                                                                            item.setAmount(shop.getStock(item, false));
                                                                                            chest.getInventory().addItem(item);
                                                                                        }
                                                                                    } else {
                                                                                        if (item.getType() != Material.AIR) {
                                                                                            item.setAmount(shop.getStock(item, false));

                                                                                            chest.getWorld().dropItem(chest.getLocation(), item);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            if (shop.getShopContents(true) != null) {
                                                                                for (ItemStack item : shop.getShopContents(true).keySet()) {
                                                                                    if (chest.getInventory().firstEmpty() > -1) {
                                                                                        if (shop.getStock(item, true) > 0) {
                                                                                            if (item.getType() != Material.AIR) {
                                                                                                item.setAmount(shop.getStock(item, true));
                                                                                                chest.getInventory().addItem(item);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        if (shop.getStock(item, true) > 0) {
                                                                                            if (item.getType() != Material.AIR) {
                                                                                                item.setAmount(shop.getStock(item, true));
                                                                                                chest.getWorld().dropItem(chest.getLocation(), item);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            config.set(s, null);
                                                                            try {
                                                                                config.save(f);
                                                                            } catch (IOException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                            ShopLimits.loadShops();
                                                                        } else {
                                                                            e.setCancelled(true);
                                                                            p.sendMessage(Messages.getPrefix() + Messages.getDenyRemoveShop());
                                                                            if (Core.isAboveEight() && Config.useTitles()) {


                                                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                                Core.getTitleManager().sendTitle(p, Messages.getDenyRemoveShop());


                                                                            }
                                                                        }
                                                                    } else {
                                                                        e.setCancelled(true);
                                                                        p.sendMessage(Messages.getPrefix() + Messages.getDenyRemoveShop());
                                                                        if (Core.isAboveEight() && Config.useTitles()) {


                                                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                                            Core.getTitleManager().sendTitle(p, Messages.getDenyRemoveShop());


                                                                        }
                                                                    }

                                                                    break;
                                                                } else {

                                                                    p.sendMessage(Messages.getPrefix() + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted.");
                                                                }
                                                            }
                                                        }
                                                    } catch (NullPointerException ex) {
                                                        ex.printStackTrace();
                                                        p.sendMessage(Messages.getPrefix() + "§4Error: §cPlease inform a admin or owner. A shop's file appears to be corrupted. Check the Console for details.");
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
}
