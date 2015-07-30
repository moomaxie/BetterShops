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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public static int loadingTotal;

    public static int loadShops() throws Exception {

        limit.clear();
        shops.clear();
        names.clear();
        locs.clear();
        signLocs.clear();
        playerShops.clear();
        worlds.clear();

        int ss;

        if (!Core.useSQL() || Core.useSQL() && !SQLUtil.isConverted()) {
            ss = loadFile();
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
        return (boolean) Config.getObject("Creation Limit") && getShopAmt(p) >= (Double) Config.getObject("Limit");
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

    public static int loadFile() throws Exception {
        limit.clear();
        shops.clear();
        names.clear();
        locs.clear();
        signLocs.clear();
        playerShops.clear();
        worlds.clear();
        int ss = 0;
        File file = new File(Bukkit.getPluginManager().getPlugin("BetterShops").getDataFolder(), "Shops");

        if (!file.exists()) {
            file.mkdirs();
        }

        int total = 0;
        if (file.listFiles() != null)
            for (File f : file.listFiles()) {
                File[] fi = f.listFiles();
                if (fi != null) {
                    total = total + fi.length;
                    loadingTotal = total;
                }
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
        return ss;
    }

    public static int loadSQL() throws SQLException {
        limit.clear();
        shops.clear();
        names.clear();
        locs.clear();
        signLocs.clear();
        playerShops.clear();
        worlds.clear();
        Statement statement = Core.getConnection().createStatement();

        DatabaseMetaData md = Core.getConnection().getMetaData();

        ResultSet rs2 = md.getColumns(null, null, "Shops", null);
        if (rs2.next()) {
            statement.executeUpdate("RENAME TABLE Shops TO " + Config.getObject("prefix") + "Shops;");
            statement.executeUpdate("RENAME TABLE Items TO " + Config.getObject("prefix") + "Items;");
            statement.executeUpdate("RENAME TABLE Trades TO " + Config.getObject("prefix") + "Trades;");
            statement.executeUpdate("RENAME TABLE Keepers TO " + Config.getObject("prefix") + "Keepers;");
            statement.executeUpdate("RENAME TABLE Blacklist TO " + Config.getObject("prefix") + "Blacklist;");
            statement.executeUpdate("RENAME TABLE Transactions TO " + Config.getObject("prefix") + "Transactions;");
        }

        ResultSet rs3 = md.getColumns(null, null, Config.getObject("prefix") + "Items", "DisplayName");
        if (!rs3.next()) {
            statement.executeUpdate("ALTER TABLE " + Config.getObject("prefix") + "Items " +
                    "ADD COLUMN `DisplayName` TEXT NULL DEFAULT NULL AFTER `Item`, " +
                    "ADD COLUMN `Lore` TEXT NULL DEFAULT NULL AFTER `DisplayName`, " +
                    "ADD COLUMN `Enchants` TEXT NULL DEFAULT NULL AFTER `Lore`;");
        }

        ResultSet rs4 = md.getColumns(null, null, Config.getObject("prefix") + "Shops", "NPCInfo");
        if (!rs4.next()) {
            statement.executeUpdate("ALTER TABLE " + Config.getObject("prefix") + "Shops " +
                    "ADD COLUMN `NPCInfo` TEXT NULL DEFAULT NULL;");
        }

        ResultSet rs6 = md.getColumns(null, null, Config.getObject("prefix") + "Shops", "Removal");
        if (!rs6.next()) {
            statement.executeUpdate("ALTER TABLE " + Config.getObject("prefix") + "Shops " +
                    "ADD COLUMN `Removal` TEXT NULL DEFAULT NULL;");
        }

        ResultSet rs5 = md.getColumns(null, null, Config.getObject("prefix") + "Items", "AutoStock");
        if (!rs5.next()) {
            statement.executeUpdate("ALTER TABLE " + Config.getObject("prefix") + "Items " +
                    "ADD COLUMN `AutoStock` TEXT NULL DEFAULT NULL, " +
                    "ADD COLUMN `TransCool` TEXT NULL DEFAULT NULL, " +
                    "ADD COLUMN `Auto` BOOLEAN NULL DEFAULT NULL, " +
                    "ADD COLUMN `Trans` BOOLEAN NULL DEFAULT NULL, " +
                    "ADD COLUMN `Cooldowns` TEXT NULL DEFAULT NULL, " +
                    "ADD COLUMN `SellEco` BOOLEAN NULL DEFAULT NULL;");
            statement.executeUpdate("ALTER TABLE " + Config.getObject("prefix") + "Items MODIFY Price DOUBLE, " +
                    "MODIFY OrigPrice DOUBLE, " +
                    "MODIFY MinimumPrice DOUBLE, " +
                    "MODIFY MaximumPrice DOUBLE, " +
                    "MODIFY AdjustedPrice DOUBLE;");
        }

        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + Config.getObject("prefix") + "Shops");
        int total = 0;
        if (rs.next()) {
            total = rs.getInt(1);
            loadingTotal = total;
        }

        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aStarting Loading of Chest Shops (§d" + total + "§a) §evia MySQL");

        int i = 0;

        final ResultSet r = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops");
        while (r.next()) {

            final String name = r.getString("Name");

            Shop shop = null;
            try {
                shop = new SQLShop(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            shops.add(shop);
            assert shop != null;
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