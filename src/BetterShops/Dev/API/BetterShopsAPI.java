package BetterShops.Dev.API;

import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Shops.Shop;

import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class BetterShopsAPI {

    private List<Shop> shops = ShopLimits.getAllShops();

    public BetterShopsAPI(){
    }

    public List<Shop> getAllShops(){
        return shops;
    }
}
