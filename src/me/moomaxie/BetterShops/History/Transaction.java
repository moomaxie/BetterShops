package me.moomaxie.BetterShops.History;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Transaction {

    private OfflinePlayer p;
    private ItemStack item;
    private double price;
    private int amount;
    private Date date;
    private boolean sell;

    public Transaction(OfflinePlayer p, Date date, ItemStack item, double price, int amount, boolean sell) {
        this.p = p;
        this.amount = amount;
        this.item = item;
        this.price = price;
        this.date = date;
        this.sell = sell;
    }

    public OfflinePlayer getPlayer() {
        return p;
    }

    public double getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSell(){
        return sell;
    }
}
