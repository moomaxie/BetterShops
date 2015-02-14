package me.moomaxie.BetterShops.SupplyandDemand;

import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */


    // More buying = high buy price and low sell price
    // More selling = low sell price and high buying price
    // Less buying = low buy price and high sell price
    // Less selling = low buy price and high sell price


    // Rate of inflation - subject to change (changeable)
    // Static inflation - rate does not change (toggleable)

    //Max Buy/Sell price
    //Min Buy/Sell price

    // Amount needed to double

public class PriceManager {

    private Shop shop;
    private static double globalInflationRate = 1.0;
    private ShopItem item;

    public PriceManager(ShopItem item){
        this.item = item;
        this.shop = item.getShop();
    }

    public static double getGlobalInflationRate(){
        return globalInflationRate;
    }

    public static void setGlobalInflationRate(double amt){
        globalInflationRate = amt;
    }

    public Shop getShop(){
        return shop;
    }

    public ShopItem getItem(){
        return item;
    }


}
