package max.hubbard.bettershops.Events;

import max.hubbard.bettershops.Shops.Shop;
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
public class ShopDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Shop shop;

    public ShopDeleteEvent(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop(){
        return shop;
    }

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
