package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.History;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.TradeManager;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SQLUtil {

    public static boolean isConverted() throws SQLException {
        Statement statement;
        statement = Core.getConnection().createStatement();

        ResultSet set = statement.getConnection().getMetaData().getTables(null, null, "Transactions", null);

        return set.next();
    }

    public static void convertShopToSQL() throws SQLException, IOException {
        Statement statement;
        statement = Core.getConnection().createStatement();

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Shops (Name VARCHAR(30) NOT NULL, Owner TEXT, Description TEXT, World TEXT, X INTEGER, " +
                "Y INTEGER, Z INTEGER, NextShopId INTEGER, Open BOOLEAN, Notify BOOLEAN, Server BOOLEAN, NPC BOOLEAN, Holo BOOLEAN, Frame INTEGER, PRIMARY KEY (`Name`));");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Keepers (Shop VARCHAR(30) NOT NULL, Players TEXT);");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Blacklist (Shop VARCHAR(30) NOT NULL, Players TEXT);");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Shop VARCHAR(30) NOT NULL, Id TEXT, Item TEXT, DisplayName TEXT, Lore TEXT, Enchants TEXT, Page INTEGER, Slot INTEGER, Selling BOOLEAN, Stock INTEGER, Amount INTEGER," +
                "Price DECIMAL, OrigPrice DECIMAL, Infinite BOOLEAN, LiveEconomy BOOLEAN, PriceChangePercent DECIMAL, DoubleAmount INTEGER," +
                "MinimumPrice DECIMAL, MaximumPrice DECIMAL, AdjustedPrice DECIMAL, SellLimit INTEGER);");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Trades (Shop VARCHAR(30) NOT NULL, Id TEXT, TradeItems TEXT, ReceiveItems TEXT, Gold INTEGER, " +
                "RecGold INTEGER,Traded BOOLEAN);");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions (Shop VARCHAR(30) NOT NULL, Item TEXT, Player TEXT, Owner TEXT, Price DECIMAL, " +
                "Amount INTEGER, Selling BOOLEAN, Date TEXT);");

        for (Shop shop : ShopManager.getShops()) {
            if (shop instanceof FileShop) {
                String name = shop.getName();
                String id = shop.getOwner().getUniqueId().toString();
                String description = (String) shop.getObject("Description");
                Location loc = shop.getLocation();
                List<OfflinePlayer> keepers = shop.getKeepers();
                List<OfflinePlayer> blacklist = shop.getBlacklist();
                boolean npc = shop.isNPCShop();
                boolean holo = shop.isHoloShop();
                boolean open = shop.isOpen();
                boolean notify = shop.isNotify();
                boolean server = shop.isServerShop();
                int nextId = shop.getNextAvailableId();
                List<ShopItem> items = shop.getShopItems();
                History history = shop.getHistory();

                statement.executeUpdate("INSERT IGNORE INTO Shops (`Name`, `Owner`, `Description`, `World`, `X`, `Y`, `Z`, `NextShopId`, `Open`, `Notify`, `Server`, `NPC`, `Holo`, `Frame`) VALUES" +
                        " ('" + name + "', '" + id + "', '" + description + "', '" + loc.getWorld().getName() + "', '" + loc.getX() + "', '" + loc.getY() + "', '" + loc.getZ() + "', '"
                        + nextId + "', " + open + ", '" + getBoolValue(notify) + "', '" + getBoolValue(server) + "', '" + getBoolValue(npc) + "', '" + getBoolValue(holo) + "', '" + shop.getFrameColor()+
                        "');");

                for (OfflinePlayer pl : keepers) {
                    statement.executeUpdate("INSERT IGNORE INTO Keepers (`Shop`, `Players`) VALUES ('" + name + "', '" + pl.getUniqueId().toString() + "')"+

                            ";");
                }
                for (OfflinePlayer pl : blacklist) {
                    statement.executeUpdate("INSERT IGNORE INTO Blacklist (`Shop`, `Players`) VALUES ('" + name + "', '" + pl.getUniqueId().toString() + "')" +
                            ";");
                }
                for (String s : history.config.getKeys(false)) {
                    String item = history.config.getConfigurationSection(s).getString("Item").replaceAll("'","");
                    String player = history.config.getConfigurationSection(s).getString("Buyer Name");
                    String owner = history.config.getConfigurationSection(s).getString("Owner Name");
                    String date = history.config.getConfigurationSection(s).getString("Date");
                    boolean sell = history.config.getConfigurationSection(s).getBoolean("Selling Shop");
                    double price = history.config.getConfigurationSection(s).getDouble("Price");
                    int amt = history.config.getConfigurationSection(s).getInt("Amount");


                    statement.executeUpdate("INSERT IGNORE INTO Transactions (`Shop`, `Item`, `Player`, `Owner`, `Price`, `Amount`, `Selling`, `Date`) VALUES" +
                            " ('" + name + "', '" + item + "', '" + player + "', '" + owner + "', '" + price + "', '" + amt + "', "
                            + sell + ", '" + date + "'"+
                            ");");
                }

                for (ShopItem item : items) {
                    int idd = item.getId();
                    ItemStack ite = item.getItem();
                    String display = item.getDisplayName();
                    String l = null;
                    if (item.getLore() != null) {
                        l = item.getLore().get(0);
                        for (int i = 1; i < item.getLore().size(); i++) {
                            l = l + "||BS||" + item.getLore().get(i);
                        }
                    }
                    String enchants = "";
                    if (item.getItem().getEnchantments().size() > 0) {
                        for (Enchantment en : item.getItem().getEnchantments().keySet()) {
                            enchants = enchants + "||BS||" + en.getName() + "-" + item.getItem().getEnchantments().get(en);
                        }
                    }
                    int page = item.getPage();
                    int slot = item.getSlot();
                    boolean sell = item.isSelling();
                    int stock = item.getStock();
                    int amt = item.getAmount();
                    double price = item.getPrice();
                    double origPrice = item.getOrigPrice();
                    boolean infinite = item.isInfinite();
                    boolean liveEco = item.getLiveEco();
                    double percent = item.getPriceChangePercent();
                    int doubleAmt = item.getAmountToDouble();
                    double min = item.getMinPrice();
                    double max = (Double) item.getObject("MaximumPrice");
                    double adjust = item.getAdjustedPrice();

                    statement.executeUpdate("INSERT IGNORE INTO Items VALUES " +
                            "('" + name + "', '" + idd + "', '" + ItemUtils.toString(ite) + "', '" + display + "', '" + l + "', '" + enchants + "', '" + page + "', '" + slot + "', '" + getBoolValue(sell) + "', '" + stock + "', " +
                            "'" + amt + "', '" + price + "', '" + origPrice + "', '" + getBoolValue(infinite) + "', '" + getBoolValue(liveEco) + "', '" + percent + "', " +
                            "'" + doubleAmt + "', '" + min + "', '" + max + "', '" + adjust + "', '" + item.getLimit() + "'"+
                            ");");
                }

                if (TradeManager.getTrades(shop) != null)
                    for (Trade t : TradeManager.getTrades(shop)) {

                        List<String> ti = new ArrayList<>();

                        for (ItemStack ite : t.getTradeItems()) {
                            ti.add(ItemUtils.toString(ite));
                        }

                        List<String> ti2 = new ArrayList<>();

                        for (ItemStack ite : t.getRecievingItems()) {
                            ti2.add(ItemUtils.toString(ite));
                        }

                        String s = "";
                        String s2 = "";

                        for (String s1 : ti) {
                            s = s + s1 + "###";
                        }
                        for (String s1 : ti2) {
                            s2 = s2 + s1 + "###";
                        }

                        statement.executeUpdate("INSERT IGNORE INTO Trades (`Shop`, `Id`, `TradeItems`, `ReceiveItems`, `Gold`, `RecGold`, `Traded`) VALUES" +
                                " ('" + name + "', '" + t.getId() + "', '" + s + "', '" + s2 + "', '" + t.getTradeGold() + "', '" + t.getReceivingGold() + "', '"
                                + getBoolValue(t.isTraded()) + "'"+
                                ");");


                    }
            }
        }


    }

    public enum ObjectType {
        /**
         * Represent a normal Integer:<br/>
         * From -2,147,483,648 to +2,147,483,647
         */
        INTEGER,
        /**
         * Represent a big Integer:<br/>
         * From -9,223,372,036,854,775,808 to +9,223,372,036,854,775,807
         */
        BIGINT,
        /**
         * Represent a single character
         */
        CHARACTER,
        /**
         * Represent a String:<br/>
         * Max size: 8000 characters
         */
        TEXT,
        /**
         * Represent a boolean
         */
        BOOLEAN,
        /**
         * Represent a float
         */
        FLOAT,
        /**
         * Represent a decimal number
         */
        DECIMAL,
        /**
         * Represent an array
         */
        ARRAY,
        /**
         * Represent a date with years, month and days values
         */
        DATE,
        /**
         * Represent a time with hour, minute, and second values
         */
        TIME,
        /**
         * Represent any kind of data
         */
        NONE,
        /**
         * Represent a date and a time with year, month, day, hour, minute, and second values
         */
        TIMESTAMP;
    }


    public static int getBoolValue(boolean b) {
        if (b) {
            return 1;
        }
        return 0;
    }
}
