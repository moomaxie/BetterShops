package max.hubbard.bettershops.Shops.Items;

import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.inventory.ItemStack;

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
public interface ShopItem {

    Shop shop = null;
    ItemStack item = null;
    boolean sell = false;
    int id = 0;
    byte data = 0;
    short durability = 0;
    List<String> lore = new ArrayList<>();
    String displayName = null;
    double origAdjustedPrice = 0;
    double priceChangePercent = 1;
    int amountToDouble = 750;
    double minPrice = 0;
    double adjustedPrice = 0;
    double maxPrice = 10000000;
    double amountTo = 0;

    public Object getObject(String s) ;

    public void setObject(String s, Object o);

    public Shop getShop();

    public ItemStack getItem();

    public boolean isSelling();

    public boolean isInfinite();

    public boolean getLiveEco();

    public int getPage();

    public int getSlot();

    public List<String> getLore();

    public String getDisplayName();

    public int getAmount();

    public int getId();

    public int getLimit();

    public int getStock();

    public double getPrice();

    public byte getData();

    public void setData(byte data);

    public short getDurability();

    public void setPrice(double price);

    public String getPriceAsString();

    public double getAdjustedPrice();

    public String getAdjustedPriceAsString();

    public int getMinPrice();

    public void setAdjustedPrice(double amt);

    public void setAmountTo(double amt);

    public double getAmountTo();

    public void setAmountToDouble(int amt);

    public int getAmountToDouble();

    public void calculatePricePercent();

    public void calculatePriceChangePercent();

    public double getPriceChangePercent();

    public double getOrigPrice();

    public ShopItem getSister();

    public void calculatePrice();


}
