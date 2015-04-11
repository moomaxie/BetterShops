package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
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
public class ShopItem {

    private ShopItemManager manager;
    private Shop shop;
    private ItemStack item;
    private List<String> lore = new ArrayList<>();
    private Material material;
    private boolean sellingSet = false;
    private boolean sell;
    private String displayName = null;
    private byte data;
    private double origAdjustedPrice;
    private double origPrice;
    private short durability;
    private double price = Config.getDefaultPrice();
    private boolean infinite = false;
    private boolean liveEco = false;
    private double priceChangePercent = 1;
    private int amountToDouble = 750;
    private int minPrice = 0;
    private double adjustedPrice = Config.getDefaultPrice();
    private int maxPrice = 10000000;
    private int shopID = 0;
    private double amountTo;

    public ShopItem(Shop shop, ItemStack item, int shopItemId, int page, int slot, boolean sell) {
        this.shop = shop;
        item.setAmount(1);
        this.item = item;

        this.shopID = shopItemId;

        manager = new ShopItemManager(this);

        setPage(page);
        setSlot(slot);
        setSelling(sell);
        this.sellingSet = true;
        this.sell = sell;

        this.lore = item.getItemMeta().getLore();
        this.displayName = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.data = item.getData().getData();
        this.durability = item.getDurability();

//        setAmountTo(calculateAmountTo());
        origAdjustedPrice = getPrice();

    }

    public ShopItemManager getManager() {
        return manager;
    }

    public int getShopItemID() {
        return shopID;
    }

    public Shop getShop() {
        return shop;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setPage(int page) {
        manager.setPage(page);
    }

    public int getPage() {
        return manager.getPage();
    }

    public void setLimit(int limit) {
        manager.setLimit(limit);
    }

    public int getLimit() {
        return manager.getLimit();
    }

    public void setOrigPrice(double amt) {
        origPrice = amt;
        manager.setOrigPrice(amt);
    }

    public double getOrigPrice() {
        return manager.getOrigPrice();
    }

    public void setSlot(int slot) {
        manager.setSlot(slot);
    }

    public int getSlot() {
        int slot = manager.getSlot();

        if (slot >= 72) {
            slot = shop.getPreciseSlot(slot);
            manager.setSlot(slot);
        }

        return slot;
    }

    public void setSelling(boolean sell) {
        manager.setSelling(sell);
    }

    public boolean isSelling() {
        if (!sellingSet) {
            return manager.isSelling();
        } else {
            return sell;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
        manager.setInfinite(infinite);
    }

    public boolean isInfinite() {
        return manager.isInfinite();
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
//        MaterialData d = item.getData();
//        d.setData(data);
//        item.setData(d);

        item = new ItemStack(item.getType(),1,data);
        ItemMeta meta = item.getItemMeta();

        if (getDisplayName() != null) {
            meta.setDisplayName(getDisplayName());
        }
        if (getLore() != null){
            meta.setLore(getLore());
        }
        item.setItemMeta(meta);

        setStock(0);
    }

    public short getDurability() {
        return durability;
    }

    public void setStock(int stock) {
        manager.setStock(stock);
    }

    public int getStock() {
        return manager.getStock();
    }

    public void setAmount(int amt) {
        manager.setAmount(amt);
    }

    public int getAmount() {
        return manager.getAmount();
    }

    public void setPrice(double price) {
        this.price = price;
        manager.setPrice(price);

        if (!getLiveEco()) {
            setOrigPrice(price);
        }
    }

    public double getPrice() {
        return manager.getPrice();
    }

    public String getPriceAsString() {

        return manager.getPriceAsString();
    }

    public void setLiveEco(boolean set) {
        this.liveEco = set;
        manager.setLiveEco(set);
    }

    public boolean getLiveEco() {
        liveEco = manager.isLiveEco();
        return manager.isLiveEco();
    }

    public double calculateAmountTo() {
        amountTo = priceChangePercent / amountToDouble;
        return amountTo;
    }

    public void setAmountTo(double amt) {
        if (!sell) {
            amountTo = amt;
            calculatePricePercent();
            calculatePrice();
        } else {
            if (getLiveEco()) {
                if (getSister() != null) {
                    getSister().setAmountTo(amt);
                }
            }
        }
    }

    public double getOrigAdjustedPrice() {
        return origAdjustedPrice;
    }

    public double getAmountTo() {
        return amountTo;
    }

    public void calculatePricePercent() {
        priceChangePercent = getAmountTo() / getAmountToDouble();
        manager.setPriceChangePercent(priceChangePercent);
    }

    public void calculatePriceChangePercent() {
        priceChangePercent = getAdjustedPrice() / getOrigPrice();
        manager.setPriceChangePercent(priceChangePercent);
    }

    public double getPriceChangePercent() {
        return manager.getPriceChangePercent();
    }

    public void setAmountToDouble(int amt) {
        this.amountToDouble = amt;
        manager.setDoubleAmount(amt);
        calculatePricePercent();
        calculatePrice();

    }

    public int getAmountToDouble() {
        return manager.getDoubleAmount();
    }

    public void setMinPrice(int amt) {
        this.minPrice = amt;
        manager.setMinimumPrice(amt);
    }

    public int getMinPrice() {
        return manager.getMinimumPrice();
    }

    public void setAdjustedPrice(double amt) {

        BigDecimal dec = new BigDecimal(amt);
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        origAdjustedPrice = adjustedPrice;
        this.adjustedPrice = dec.doubleValue();
        manager.setAdjustedPrice(dec.doubleValue());
        if (getLiveEco())
            setPrice(adjustedPrice);
        calculatePriceChangePercent();

        if (!sell) {
            if (getSister() != null) {
                getSister().setAdjustedPrice(dec.doubleValue() / 2);
            }
        }
    }

    public double getAdjustedPrice() {
        BigDecimal dec = new BigDecimal(manager.getAdjustedPrice());
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.doubleValue();
    }

    public String getAdjustedPriceAsString() {

        BigDecimal dec = new BigDecimal(getAdjustedPrice());
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.toPlainString();
    }

    public void setMaxPrice(int amt) {
        this.maxPrice = amt;
        manager.setMaximumPrice(amt);
    }

    public int getMaxPrice() {
        return manager.getMaximumPrice();
    }

    public ShopItem getSister() {
        if (sell) {
            return ShopItem.fromItemStack(shop, item, false);
        } else {
            return ShopItem.fromItemStack(shop, item, true);
        }
    }

    public void calculatePrice() {
        double p = getOrigPrice() * getPriceChangePercent();

        if (getAdjustedPrice() + p < minPrice) {
            setAdjustedPrice(minPrice);
        } else if (getAdjustedPrice() + p > maxPrice) {
            setAdjustedPrice(maxPrice);
        } else {

            setAdjustedPrice(getAdjustedPrice() + p);
        }
    }

    public static ShopItem fromId(Shop shop, int id, boolean sell){
        for (ShopItem it : shop.getShopItems(sell)){
            if (it.getShopItemID() == id){
                return it;
            }
        }
        return null;
    }

    public static ShopItem fromItemStack(Shop shop, ItemStack item, boolean sell) {

        ShopItem exists = null;

        if (item != null && shop != null) {

            int a = item.getAmount();

            item.setAmount(1);

            if (item.getType() != Material.SKULL_ITEM) {

                ItemMeta meta = item.getItemMeta();
                List<String> lore = shop.getLore(item);


                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                List<String> lore = shop.getLore(item);


                meta.setLore(lore);
                item.setItemMeta(meta);
            }


            for (ShopItem it : shop.getShopItems(sell)) {

                ItemStack copy = it.getItem().clone();
                copy.setAmount(1);

                if (item.equals(copy) || item.toString().equals(copy.toString()) && item.getData().getData() == it.getData() && item.getDurability() == it.getDurability()) {
                    exists = it;
                    break;
                }

            }

            if (exists == null) {
                for (ShopItem it : shop.getShopItems(sell)) {
                    ItemStack copy = it.getItem().clone();
                    copy.setAmount(1);
                    if (!item.toString().equals(copy.toString())) {
                        exists = null;
                    } else {
                        exists = it;
                    }

                    if (item.equals(copy) || item.toString().equals(copy.toString())) {
                        if (it.getData() != item.getData().getData()) {
                            exists = null;

                        } else {

                            if (it.getDurability() != item.getDurability()) {
                                exists = null;

                            } else {
                                exists = it;
                                break;
                            }
                            break;
                        }


                    }

                }

            }

            item.setAmount(a);
        }
        return exists;

    }
}
