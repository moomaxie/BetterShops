package me.moomaxie.BetterShops.Shops;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

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

    private static File file;
    private static YamlConfiguration config;
    private ShopItem item;
    private ConfigurationSection section;

    protected ShopItemManager(ShopItem item){
        file = item.getShop().file;
        config = YamlConfiguration.loadConfiguration(file);
        this.item = item;
        String name = item.getShop().getName();
        section = config.getConfigurationSection(name).getConfigurationSection("" + item.getShopItemID());
    }

    public ShopItem getItem(){
        return item;
    }

    public void setStock(int amt){
        section.set("Stock", amt);
        saveConfig();
    }

    public int getStock(){
        return section.getInt("Stock");
    }

    public void setPrice(double amt){
        section.set("Price", amt);
        saveConfig();
    }

    public double getPrice(){
        return section.getDouble("Price");
    }

    public String getPriceAsString(){
        return (new BigDecimal(Double.toString(getPrice()))).toPlainString();
    }

    public void setAmount(int amt){
        section.set("Amount", amt);
        saveConfig();
    }

    public int getAmount(){
        return section.getInt("Amount");
    }

    public void setPage(int page){
        section.set("Page", page);
        saveConfig();
    }

    public int getPage(){
        return section.getInt("Page");
    }

    public void setSlot(int slot){
        section.set("Slot", slot);
        saveConfig();
    }

    public int getSlot(){
        return section.getInt("Slot");
    }

    public void setSelling(boolean b){
        section.set("Selling", b);
        saveConfig();
    }

    public boolean isSelling(){
        return section.getBoolean("Selling");
    }

    public void setInfinite(boolean b){
        section.set("Infinite", b);
        saveConfig();
    }

    public boolean isInfinite(){
        return section.getBoolean("Infinite");
    }

    public void setLiveEco(boolean b){
        section.set("LiveEconomy", b);
        saveConfig();
    }

    public boolean isLiveEco(){
        return section.getBoolean("LiveEconomy");
    }

    public void setPriceChangePercent(double amt){
        section.set("PriceChangePercent", amt);
        saveConfig();
    }

    public double getPriceChangePercent(){
        return section.getDouble("PriceChangePercent");
    }

    public void setDoubleAmount(int amt){
        section.set("DoubleAmount", amt);
        saveConfig();
    }

    public int getDoubleAmount(){
        return section.getInt("DoubleAmount");
    }

    public void setMinimumPrice(int amt){
        section.set("MinimumPrice", amt);
        saveConfig();
    }

    public int getMinimumPrice(){
        return section.getInt("MinimumPrice");
    }

    public void setMaximumPrice(int amt){
        section.set("MaximumPrice", amt);
        saveConfig();
    }

    public int getMaximumPrice(){
        return section.getInt("MaximumPrice");
    }

    public void setAdjustedPrice(double amt){
        section.set("AdjustedPrice", amt);
        saveConfig();
    }

    public double getAdjustedPrice(){
        return section.getDouble("AdjustedPrice");
    }

    public void saveConfig(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
