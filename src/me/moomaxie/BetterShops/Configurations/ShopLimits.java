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
    public static HashMap<Location, Shop> locs = new HashMap<>();
    public static HashMap<String, Shop> names = new HashMap<>();
    public static HashMap<UUID, List<Shop>> playerShops = new HashMap<>();

    public static int loadShops() {

        limit.clear();
        shops.clear();
        names.clear();
        locs.clear();
        playerShops.clear();

        int ss = 0;

        File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");

        if (!file.exists()) {
            file.mkdirs();
        }

        for (File f : file.listFiles()) {
            if (f.getName().contains(".yml")) {
                UUID id = UUID.fromString(f.getName().substring(0, f.getName().length() - 4));

                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                int amt = config.getKeys(false).size();

                if (amt > 0) {
                    limit.put(id, amt);
                    ss = ss + amt;
                }

                List<Shop> shops1 = new ArrayList<>();

                for (String s : config.getKeys(false)) {
                    Shop shop = new Shop(s, config, f);

                    shops1.add(shop);

                    shops.add(shop);
                    locs.put(shop.getLocation(), shop);
                    names.put(shop.getName(), shop);
                }

                playerShops.put(id, shops1);
            }

        }
        return ss;
    }

    public static HashMap<UUID, Integer> getLimits() {
        return limit;
    }

    public static HashMap<UUID, List<Shop>> getPlayerShops(){
        return playerShops;
    }

    public static List<Shop> getShopsForPlayer(OfflinePlayer p){
        return playerShops.get(p.getUniqueId());
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

    public static Shop fromString(String name) {
        return names.get(name);
    }

    public static Shop fromString(Player p, String name) {
        return names.get(name);
    }

    public static Shop fromLocation(Location name) {
        if (locs.containsKey(name)) {
            return locs.get(name);
        } else {
            name = new Location(name.getWorld(), (int) name.getX(), (int) name.getY(), (int) name.getZ());
            return locs.get(name);
        }
    }
}
