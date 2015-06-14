package max.hubbard.bettershops.SQL;


import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Items.FileShopItem;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class DatabaseManager {

    public static ShopItem getItem(Shop shop, int id) {
        ResultSet res = null;
        try {
            res = Core.getSQLDatabase().getConnection().createStatement().executeQuery("SELECT * FROM " + shop.getName() + " WHERE Id = '" + id + "';");
            res.next();

            int page = res.getInt("Page");
            int slot = res.getInt("Slot");
            boolean sell = res.getBoolean("Selling");

            if (shop instanceof FileShop) {
                return FileShopItem.fromPageAndSlot(shop, page, slot, sell);
            } else {
                return SQLShopItem.fromPageAndSlot(shop, page, slot, sell);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void deleteItemTable(ShopItem item) {
        try {

            if (Core.getConnection().getMetaData().getTables(null, null, item.getShop().getName() + "_Items", null).next()) {
                Core.getConnection().createStatement().execute("DELETE from " + item.getShop().getName() + "_Items where ID='" + item.getId() + "';");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(ShopItem item) {
        Shop shop = item.getShop();

        try {
            Core.getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + shop.getName() + "_Items" +
                    " (ID INT, " +
                    "Material TEXT, " +
                    "Data INT, " +
                    "Durability INT, " +
                    "DisplayName TEXT, " +
                    "Lore TEXT, " +
                    "Page INT, " +
                    "Slot INT, " +
                    "Selling TEXT, " +
                    "Stock INT, " +
                    "Amount INT, " +
                    "Price DOUBLE, " +
                    "Infinite TEXT, " +
                    "LiveEconomy TEXT, " +
                    "SpecialEcoNumber INT," +
                    "MinPrice DOUBLE," +
                    "MaxPrice DOUBLE);");

            String lore = "";
            String name = "";

            if (item.getDisplayName() != null){
                name = item.getDisplayName().replaceAll("'","").replaceAll(",","").replaceAll(";","");
            }

            if (item.getLore() != null && item.getLore().size() > 0) {

                lore = item.getLore().get(0).replaceAll("'","").replaceAll(",","").replaceAll(";","");

                for (String s : item.getLore()) {
                    if (!s.equals(item.getLore().get(0))) {
                        lore = lore + "\n" + s.replaceAll("'","").replaceAll(",","").replaceAll(";","");
                    }
                }

            }

            Core.getSQLDatabase().updateSQL("INSERT INTO " + shop.getName() + "_Items (`ID`, `Material`, `Data`, `Durability`, `DisplayName`, `Lore`" +
                    ",`Page`, `Slot`, `Selling`, `Stock`, `Amount`, `Price`, `Infinite`, `LiveEconomy`, `SpecialEcoNumber`" +
                    ",`MinPrice`, `MaxPrice`) VALUES" +
                    " ('" + item.getId() + "', " +
                    "'" + item.getItem().getType().name() + "'," +
                    "'" + item.getData() + "'," +
                    "'" + item.getDurability() + "'," +
                    "'" + name + "'," +
                    "'" + lore + "'," +
                    "'" + item.getPage() + "'," +
                    "'" + item.getSlot() + "'," +
                    "'" + item.isSelling() + "'," +
                    "'" + item.getStock() + "'," +
                    "'" + item.getAmount() + "'," +
                    "'" + item.getPrice() + "'," +
                    "'" + item.isInfinite() + "'," +
                    "'" + item.getLiveEco() + "'," +
                    "'" + item.getAmountToDouble() + "'," +
                    "'" + item.getMinPrice() + "'," +
                    "'" + item.getObject("MaxPrice") + "');");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
