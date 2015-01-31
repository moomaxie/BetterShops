package me.moomaxie.BetterShops.Configurations;

import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopLimits {

    private static HashMap<UUID, Integer> limit = new HashMap<UUID, Integer>();
    public static List<Shop> shops = new ArrayList<>();

    public static int loadShops() {

        limit.clear();
        shops.clear();

        int ss = 0;

        File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");

        if (file.listFiles() != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    UUID id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));

                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    int amt = config.getKeys(false).size();

                    if (amt > 0) {
                        limit.put(id, amt);
                        ss = ss + amt;
                    }
                    for (String s : config.getKeys(false)) {
                        Shop shop = new Shop(s);

                        shops.add(shop);
                    }
                }
            }
        }
        return ss;
    }

    public static HashMap<UUID, Integer> getLimits() {
        return limit;
    }

    public static boolean atLimit(OfflinePlayer p) {
        return Config.useLimit() && getShopAmt(p) >= Config.getLimit();
    }

    public static int getShopAmt(OfflinePlayer p) {
        if (limit.containsKey(p.getUniqueId())) {
            return limit.get(p.getUniqueId());
        } else {
            return 0;
        }
    }

    public static List<Shop> getAllShops() {

        return shops;
    }

    public static Shop fromString(String name){
        for (Shop shop : shops){
            if (shop.getName().equals(name)){
                return shop;
            }
        }
        return null;
    }

    public static Shop fromString(Player p, String name){
        for (Shop shop : shops){
            if (shop.getName().equals(name)){
                return shop;
            }
        }
        return null;
    }

    public static Shop fromString(Location name){
        for (Shop shop : shops){

            if (shop.getLocation().equals(name) || shop.getLocation().toString().equals(name.toString()) || shop.getLocation() == name){
                return shop;
            }

            if ((int)shop.getLocation().getX() == (int)name.getX() && (int)shop.getLocation().getY() == (int)name.getY() && (int)shop.getLocation().getZ() == (int)name.getZ() && shop.getLocation().getWorld().getName().equals(name.getWorld().getName())
                    || (int)shop.getLocation().getX() == (int)name.getX() && (int)shop.getLocation().getY() == (int)name.getY() && (int)shop.getLocation().getZ() == (int)name.getZ() && shop.getLocation().getWorld().equals(name.getWorld())){
                shop.setLocation(name);

                return shop;
            }


        }
        return null;
    }
}
