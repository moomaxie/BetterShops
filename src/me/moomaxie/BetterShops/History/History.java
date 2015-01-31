package me.moomaxie.BetterShops.History;

import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Date;
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
public class History {

    private List<Transaction> Buytransactions = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<Transaction> Selltransactions = new ArrayList<>();

    private Shop shop;

    public History(Shop shop){
        this.shop = shop;
    }

    public void addTransaction(OfflinePlayer p, Date date, ItemStack item, double price, int amount, boolean sell, boolean save){

        Transaction trans = new Transaction(p, date, item,price, amount,sell);

        if (sell) {
            Selltransactions.add(trans);
        } else {
            Buytransactions.add(trans);
        }

        transactions.add(trans);

        if (save) {
            shop.saveTransaction(trans, true);
        }
    }

    public List<Transaction> getBuyingTransactions(){
        return Buytransactions;
    }
    public List<Transaction> getSellingTransactions(){
        return Selltransactions;
    }
    public List<Transaction> getAllTransactions(){
        return transactions;
    }

    public Shop getShop(){
        return shop;
    }

    public void clearAllTransactions(){
        transactions.clear();
        Buytransactions.clear();
        Selltransactions.clear();
    }

    public void clearHistory(){ //Everyday
        transactions.clear();
        Buytransactions.clear();
        Selltransactions.clear();

        shop.clearTransactions();
    }
}
