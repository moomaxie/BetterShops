package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
public class Trade {

    Shop shop;
    List<ItemStack> tradeItems;
    int tradeGold;
    List<ItemStack> tradeeItems;
    int tradeeGold;
    String id;
    boolean traded;

    public Trade(String id, Shop trader, List<ItemStack> tradeItems, int tradeGold, List<ItemStack> tradeeItems, int tradeeGold, boolean traded) {
        shop = trader;
        this.tradeeGold = tradeeGold;
        this.tradeeItems = tradeeItems;
        this.tradeGold = tradeGold;
        this.tradeItems = tradeItems;
        this.id = id;
        this.traded = traded;
    }

    public Shop getShop() {
        return shop;
    }

    public String getId() {
        return id;
    }

    public ItemStack getIcon() {
        if (tradeItems.size() > 0) {
            return tradeItems.get(0).clone();
        } else {
            return new ItemStack(Material.GOLD_INGOT);
        }
    }

    public boolean isTraded() {
        return traded;
    }

    public List<ItemStack> getTradeItems() {
        return tradeItems;
    }

    public List<ItemStack> getRecievingItems() {
        return tradeeItems;
    }

    public int getTradeGold() {
        return tradeGold;
    }

    public int getReceivingGold() {
        return tradeeGold;
    }
}
