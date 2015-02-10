package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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
    private String displayName = null;
    private byte data;
    private short durability;
    private double price = Config.getDefaultPrice();
    private boolean infinite = false;
    private boolean liveEco = false;
    private double priceChangePercent = 1;
    private int amountToDouble = 200;
    private int minPrice = 0;
    private double adjustedPrice = Config.getDefaultPrice();
    private int maxPrice = 10000000;
    private int shopID = 0;

    public ShopItem(Shop shop, ItemStack item, int shopItemId, int page, int slot, boolean sell) {
        this.shop = shop;
        this.item = item;

        this.shopID = shopItemId;

        manager = new ShopItemManager(this);

        setPage(page);
        setSlot(slot);
        setSelling(sell);

        this.lore = item.getItemMeta().getLore();
        this.displayName = item.getItemMeta().getDisplayName();
        this.material = item.getType();
        this.data = item.getData().getData();
        this.durability = item.getDurability();

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

    public void setSlot(int slot) {
        manager.setSlot(slot);
    }

    public int getSlot() {
        return manager.getSlot();
    }

    public void setSelling(boolean sell) {
        manager.setSelling(sell);
    }

    public boolean isSelling() {
        return manager.isSelling();
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
        return manager.isLiveEco();
    }

    public void setPriceChangePercent(double percent) {
        this.priceChangePercent = percent;
        manager.setPriceChangePercent(percent);
    }

    public double getPriceChangePercent() {
        return manager.getPriceChangePercent();
    }

    public void setAmountToDouble(int amt) {
        this.amountToDouble = amt;
        manager.setDoubleAmount(amt);
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
        this.adjustedPrice = amt;
        manager.setAdjustedPrice(amt);
    }

    public double getAdjustedPrice() {
        return manager.getAdjustedPrice();
    }

    public void setMaxPrice(int amt) {
        this.maxPrice = amt;
        manager.setMaximumPrice(amt);
    }

    public int getMaxPrice() {
        return manager.getMaximumPrice();
    }

    public static ShopItem fromItemStack(Shop shop, ItemStack item, boolean sell) {

        ShopItem exists = null;

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

            if (item.equals(it.getItem()) || item.toString().equals(it.getItem().toString()) && item.getData().getData() == it.getData() && item.getDurability() == it.getDurability()) {
                exists = it;
                break;
            }

        }

        if (exists == null) {
            for (ShopItem it : shop.getShopItems(sell)) {

                if (!item.toString().equals(it.toString())) {
                    exists = null;
                }

                if (item.equals(it.getItem()) || item.toString().equals(it.getItem().toString())) {
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

        return exists;

    }
}
