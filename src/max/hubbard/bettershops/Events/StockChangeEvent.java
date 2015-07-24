package max.hubbard.bettershops.Events;

import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class StockChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    ShopItem si;
    int o;
    int n;

    public StockChangeEvent(ShopItem item, int old, int newStock) {
        si = item;
        o = old;
        n = newStock;
    }

    public ShopItem getItem(){
        return si;
    }

    public Shop getShop(){
        return si.getShop();
    }

    public int getOldStock(){
        return o;
    }

    public int getNewStock(){
        return n;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
