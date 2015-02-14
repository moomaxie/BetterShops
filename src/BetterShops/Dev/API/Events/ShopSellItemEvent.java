package BetterShops.Dev.API.Events;

import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

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
    private ItemStack item;

    public ShopSellItemEvent(ItemStack item, Shop shop) {
        this.shop = shop;
        this.item = item;
    }

    public Shop getShop(){
        return shop;
    }

    public ItemStack getItem(){
        return item;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
