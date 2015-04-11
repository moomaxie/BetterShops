package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
public class ShopItemManager {

    private ShopItem item;
    private String name;
    private YamlConfiguration config;
    private File file;

    protected ShopItemManager(ShopItem item) {
        
        this.item = item;
        this.name = item.getShop().getName();
        this.config = item.getShop().config;
        this.file = item.getShop().file;

        if (!config.getConfigurationSection(name).getConfigurationSection("Items").isConfigurationSection("" + item.getShopItemID())) {
            config.getConfigurationSection(name).getConfigurationSection("Items").createSection("" + item.getShopItemID());

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ShopItem getItem() {
        return item;
    }

    public void setStock(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", amt);
        saveConfig();
        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Stock` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getStock() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("Stock");
    }

    public void setLimit(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Limit", amt);
        saveConfig();
    }

    public int getLimit() {
        if (config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).isInt("Limit")) {
            return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("Limit");
        } else {
            setLimit(0);
            return 0;
        }
    }

    public void setPrice(double amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", amt);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Price` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double getPrice() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getDouble("Price");
    }

    public double getOrigPrice() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getDouble("OrigPrice");
    }

    public void setOrigPrice(double amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("OrigPrice", amt);
        saveConfig();
    }

    public String getPriceAsString() {
        return (new BigDecimal(Double.toString(getPrice()))).toPlainString();
    }

    public void setAmount(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", amt);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Amount` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getAmount() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("Amount");
    }

    public void setPage(int page) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Page", page);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Page` = '" + page + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPage() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("Page");
    }

    public void setSlot(int slot) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Slot", slot);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Slot` = '" + slot + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSlot() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("Slot");
    }

    public void setSelling(boolean b) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Selling", b);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Selling` = '" + b + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSelling() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getBoolean("Selling");
    }

    public void setInfinite(boolean b) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", b);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `Infinite` = '" + b + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isInfinite() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getBoolean("Infinite");
    }

    public void setLiveEco(boolean b) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("LiveEconomy", b);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `LiveEconomy` = '" + b + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLiveEco() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getBoolean("LiveEconomy");
    }

    public void setPriceChangePercent(double amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("PriceChangePercent", amt);
        saveConfig();
    }

    public double getPriceChangePercent() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getDouble("PriceChangePercent");
    }

    public void setDoubleAmount(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("DoubleAmount", amt);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `SpecialEcoNumber` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDoubleAmount() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("DoubleAmount");
    }

    public void setMinimumPrice(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MinimumPrice", amt);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `MinPrice` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getMinimumPrice() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("MinimumPrice");
    }

    public void setMaximumPrice(int amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MaximumPrice", amt);
        saveConfig();

        if (Core.useSQL()) {
            try {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE " + item.getShop().getName() + "_Items SET" +
                        " `MaxPrice` = '" + amt + "' WHERE `" + item.getShop().getName() + "_Items`.`ID` ='" + item.getShopItemID() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getMaximumPrice() {
        return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getInt("MaximumPrice");
    }

    public void setAdjustedPrice(double amt) {
        config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", amt);
        saveConfig();
    }

    public double getAdjustedPrice() {
        if (isLiveEco())
            return config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).getDouble("AdjustedPrice");
        else
            return getPrice();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
