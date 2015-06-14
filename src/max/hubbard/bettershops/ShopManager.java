package max.hubbard.bettershops;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.SQLShop;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */

public class ShopManager {

    public static List<Shop> shops = new ArrayList<Shop>();

    public static int loadShops() throws SQLException, IOException {

        limit.clear();
        shops.clear();
        names.clear();
        locs.clear();
        signLocs.clear();
        playerShops.clear();
        worlds.clear();

        int ss = 0;

        if (!Core.useSQL() || Core.useSQL() && !SQLUtil.isConverted()) {

            File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");

            if (!file.exists()) {
                file.mkdirs();
            }

            int total = 0;
            if (file.listFiles() != null)
            for (File f : file.listFiles()) {
                File[] fi = f.listFiles();
                if (fi != null)
                    total = total + fi.length;
            }
            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aStarting Loading of Chest Shops (§d" + total + "§a) §eVia File");
            int i = 0;
            if (file.listFiles() != null)
                for (File f : file.listFiles()) {

                    if (f.listFiles() != null)
                        for (File file1 : f.listFiles()) {
                            List<Shop> shops1 = new ArrayList<>();
                            if (file1.getName().contains(".yml")) {
                                UUID id = UUID.fromString(f.getName());

                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file1);

                                limit.put(id, f.listFiles().length);

                                Shop shop = new FileShop(config, file1, Bukkit.getOfflinePlayer(id));

                                shops1.add(shop);

                                shops.add(shop);
                                locs.put(shop.getLocation(), shop);
                                names.put(shop.getName(), shop);
                                if (!worlds.contains(shop.getLocation().getWorld()))
                                    worlds.add(shop.getLocation().getWorld());


                                ss++;

                                i++;
                                if (i == (total * .10)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a10%");
                                }
                                if (i == (total * .33)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a33%");
                                }
                                if (i == (total * .66)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a66%");
                                }
                                if (i == (total * .5)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a50%");
                                }
                                if (i == (total * .25)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a25%");
                                }
                                if (i == (total * .75)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a75%");
                                }
                                if (i == (total * .9)) {
                                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §a90%");
                                }
                            }
                            playerShops.put(UUID.fromString(f.getName()), shops1);
                        }
                }
            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aDone!");
            if (Core.useSQL()) {
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aConverting Shops to MySQL (Will take awhile)");
                SQLUtil.convertShopToSQL();
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aDone!");
                loadSQL();
            }
        } else {
            ss = loadSQL();
        }
        return ss;
    }

    public static List<Shop> getShops() {
        return shops;
    }

    private static HashMap<UUID, Integer> limit = new HashMap<UUID, Integer>();
    public static List<World> worlds = new ArrayList<>();
    public static HashMap<Location, Shop> locs = new HashMap<>();
    public static HashMap<Location, Shop> signLocs = new HashMap<>();
    public static HashMap<String, Shop> names = new HashMap<>();
    public static HashMap<UUID, List<Shop>> playerShops = new HashMap<>();

    public static HashMap<UUID, Integer> getLimits() {
        return limit;
    }

    public static HashMap<UUID, List<Shop>> getPlayerShops() {
        return playerShops;
    }

    public static List<Shop> getShopsForPlayer(OfflinePlayer p) {
        return playerShops.get(p.getUniqueId());
    }

    public static boolean atLimit(OfflinePlayer p) {
        return (boolean) Config.getObject("Creation Limit") && getShopAmt(p) >= (Integer) Config.getObject("Limit");
    }

    public static int getShopAmt(OfflinePlayer p) {
        if (limit.containsKey(p.getUniqueId())) {
            return limit.get(p.getUniqueId());
        } else {
            return 0;
        }
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

    public static Shop fromSignLocation(Location name) {
        if (signLocs.containsKey(name)) {
            return signLocs.get(name);
        } else {
            name = new Location(name.getWorld(), (int) name.getX(), (int) name.getY(), (int) name.getZ());
            return signLocs.get(name);
        }
    }

    public static int loadSQL() throws SQLException {

        Statement statement = Core.getConnection().createStatement();

        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Shops");
        int total = 0;
        if (rs.next()) {
            total = rs.getInt(1);
        }

        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aStarting Loading of Chest Shops (§d" + total + "§a) §evia MySQL");

        int i = 0;

        ResultSet r = statement.executeQuery("SELECT * FROM Shops");
        while (r.next()) {
            String name = r.getString("Name");
            Shop shop = new SQLShop(name, r);

            shops.add(shop);
            locs.put(shop.getLocation(), shop);
            names.put(shop.getName(), shop);
            if (!worlds.contains(shop.getLocation().getWorld()))
                worlds.add(shop.getLocation().getWorld());


            if (playerShops.containsKey(shop.getOwner().getUniqueId())) {
                List<Shop> shops1 = playerShops.get(shop.getOwner().getUniqueId());
                shops1.add(shop);
                playerShops.put(shop.getOwner().getUniqueId(), shops1);
                limit.put(shop.getOwner().getUniqueId(), shops1.size());

            } else {
                List<Shop> shops1 = new ArrayList<>();
                shops1.add(shop);
                playerShops.put(shop.getOwner().getUniqueId(), shops1);
                limit.put(shop.getOwner().getUniqueId(), shops1.size());
            }
            i++;
        }
        return i;
    }
}