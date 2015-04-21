package BetterShops.Dev.API.Events;

import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopSellItemEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Shop shop;
    private ShopItem item;
    private OfflinePlayer seller;

    public ShopSellItemEvent(ShopItem item, Shop shop, OfflinePlayer seller) {
        this.shop = shop;
        this.item = item;
        this.seller = seller;
    }

    public Shop getShop(){
        return shop;
    }

    public ShopItem getItem(){
        return item;
    }

    public OfflinePlayer getCustomer(){return seller;}

    public OfflinePlayer getOwner(){
        return shop.getOwner();
    }

    public Location getLocation(){
        return shop.getLocation();
    }

    public String getShopName(){
        return shop.getName();
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
