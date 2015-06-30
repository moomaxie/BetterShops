package max.hubbard.bettershops.Shops.Items;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.ItemUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
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
public class FileShopItem implements ShopItem {
    private Shop shop;
    private ItemStack item;
    private boolean sell;
    private int id;
    private byte data;
    private short durability;
    private List<String> lore;
    private String displayName;
    private double priceChangePercent = 1;
    private int amountToDouble = 750;
    private double minPrice = 0;
    private double adjustedPrice;
    private double maxPrice = 10000000;
    private double amountTo;

    public static ShopItem createShopItem(Shop shop, ItemStack item, int id, int page, int slot, boolean sell) {
        return new FileShopItem(shop, item, id, page, slot, sell);
    }

    public static ShopItem fromItemStack(Shop shop, ItemStack item, boolean sell) {
        for (ShopItem item1 : shop.getShopItems()) {
            if (item1.isSelling() == sell && item1.getItem().isSimilar(item)) {
                return item1;
            }
        }
        return null;
    }

    public static ShopItem fromPageAndSlot(Shop shop, int page, int slot, boolean sell) {
        for (ShopItem item1 : shop.getShopItems()) {
            if (item1.isSelling() == sell && item1.getPage() == page && item1.getSlot() == slot) {
                return item1;
            }
        }
        return null;
    }

    protected FileShopItem(Shop shop, ItemStack item, int id, int page, int slot, boolean sell) {
        if (shop instanceof FileShop) {
            this.shop = shop;
            this.item = item;
            this.sell = sell;
            this.id = id;
            if (item.getItemMeta() != null) {
                lore = item.getItemMeta().getLore();
                displayName = item.getItemMeta().getDisplayName();
            }
            data = item.getData().getData();
            durability = item.getDurability();

            ((FileShop) shop).config.set("Items." + id + ".Page", page);
            ((FileShop) shop).config.set("Items." + id + ".Slot", slot);

            shop.saveConfig();

            if (getObject("LiveEconomy") == null) {
                setObject("Price", Config.getObject("DefaultPrice"));
                setObject("LiveEconomy", false);
                setObject("OrigPrice", Config.getObject("DefaultPrice"));
                setAdjustedPrice((double) Config.getObject("DefaultPrice"));
                shop.saveConfig();
                return;
            }

            if (!getLiveEco()) {
                setObject("OrigPrice", Config.getObject("DefaultPrice"));
                setAdjustedPrice(getPrice());
            } else {
                calculateAmountTo();
            }
        }
    }

    public static ShopItem loadShopItem(Shop shop, int id) {
        if (shop instanceof FileShop) {
            ItemStack item;
            if (((FileShop) shop).config.isItemStack("Items." + id + ".ItemStack")) {
                item = ((FileShop) shop).config.getItemStack("Items." + id + ".ItemStack");
            } else {
                item = ItemUtils.fromString(((FileShop) shop).config.getString("Items." + id + ".ItemStack"));
            }
            int page = (Integer) shop.getObject("Items." + id + ".Page");
            int slot = (Integer) shop.getObject("Items." + id + ".Slot");
            boolean sell = (Boolean) shop.getObject("Items." + id + ".Selling");

            if (item != null)
                return new FileShopItem(shop, item, id, page, slot, sell);
            else return null;
        } else {
            return null;
        }
    }

    public static ShopItem loadShopItem(Shop shop, int id, boolean sell) {
        if (shop instanceof FileShop) {
            if (((FileShop) shop).config.isBoolean("Items." + id + ".Selling")) {
                ItemStack item;
                if (((FileShop) shop).config.isItemStack("Items." + id + ".ItemStack")) {
                    item = ((FileShop) shop).config.getItemStack("Items." + id + ".ItemStack");
                } else {
                    item = ItemUtils.fromString(((FileShop) shop).config.getString("Items." + id + ".ItemStack"));
                }
                int page = (Integer) shop.getObject("Items." + id + ".Page");

                int slot;
                if (shop.getObject("Items." + id + ".Slot") != null) {
                    slot = (Integer) shop.getObject("Items." + id + ".Slot");
                } else {
                    slot = 18;

                }
                return new FileShopItem(shop, item, id, page, slot, sell);
            } else {
                return loadShopItem(shop, id);
            }
        } else {
            return null;
        }
    }

    public Object getObject(String s) {
        return ((FileShop) shop).config.getConfigurationSection("Items").getConfigurationSection("" + id).get(s);
    }

    public void setObject(String s, Object o) {
        ((FileShop) shop).config.getConfigurationSection("Items").getConfigurationSection("" + id).set(s, o);
        shop.saveConfig();

        if (s.equals("LiveEconomy")) {
            if (!sell) {
                if (getSister() != null)
                    getSister().setObject(s, o);

            }
        }
    }

    public Shop getShop() {
        return shop;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isSelling() {
        return sell;
    }

    public boolean isInfinite() {
        if (getObject("Infinite") != null)
            return (Boolean) getObject("Infinite");
        else {
            setObject("Infinite", false);
            return false;
        }
    }

    public boolean getLiveEco() {
        if (getObject("LiveEconomy") != null)
            return (Boolean) getObject("LiveEconomy");
        else {
            setObject("LiveEconomy", false);
            return false;
        }
    }

    public int getPage() {
        if (getObject("Page") != null)
            return (Integer) getObject("Page");
        else {
            setObject("Page", shop.getNextAvailablePage(sell));
            return shop.getNextAvailablePage(sell);
        }
    }

    public int getSlot() {
        if (getObject("Slot") != null)
            return (Integer) getObject("Slot");
        else {
            setObject("Slot", shop.getNextSlotForPage(getPage(), sell));
            return shop.getNextSlotForPage(getPage(), sell);
        }
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAmount() {
        return (Integer) getObject("Amount");
    }

    public int getId() {
        return id;
    }

    public int getLimit() {
        if (getObject("Limit") != null) {
            return (Integer) getObject("Limit");
        } else {
            return 0;
        }
    }

    public int getStock() {
        return (Integer) getObject("Stock");
    }

    public double getPrice() {
        return (Double) getObject("Price");
    }

    public byte getData() {
        return data;
    }

    public double calculateAmountTo() {
//        System.out.println("amtTo = " + getPriceChangePercent() + " * " + getAmountToDouble() + " = " + getPriceChangePercent() * getAmountToDouble());
        amountTo = getPriceChangePercent() * getAmountToDouble();
        amountTo = getAmountToDouble() - amountTo;
//        System.out.println("percent = " + amountTo + " / " + getAmountToDouble() + " = " + amountTo / getAmountToDouble());
//        System.out.println("double = " + amountTo + " / " + getPriceChangePercent() + " = " + amountTo / getPriceChangePercent());

        return amountTo;
    }

    public void setData(byte data) {
        this.data = data;

        item = new ItemStack(item.getType(), 1, data);
        ItemMeta meta = item.getItemMeta();

        if (getDisplayName() != null) {
            meta.setDisplayName(getDisplayName());
        }
        if (getLore() != null) {
            meta.setLore(getLore());
        }
        item.setItemMeta(meta);

        setObject("Stock", 0);
    }

    public short getDurability() {
        return durability;
    }

    public void setPrice(double price) {
        setObject("Price", price);

        if (!getLiveEco()) {
            setAdjustedPrice(price);
            setObject("OrigPrice", price);
        }
    }

    public String getPriceAsString() {

        BigDecimal dec = new BigDecimal(getPrice());
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.toPlainString();
    }

    public double getAdjustedPrice() {
        BigDecimal dec = new BigDecimal((Double) getObject("AdjustedPrice"));
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.doubleValue();
    }

    public String getAdjustedPriceAsString() {

        BigDecimal dec = new BigDecimal(getAdjustedPrice());
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.toPlainString();
    }

    public int getMinPrice() {
        return (Integer) getObject("MinimumPrice");
    }

    public void setAdjustedPrice(double amt) {

        BigDecimal dec = new BigDecimal(amt);
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        this.adjustedPrice = dec.doubleValue();
        setObject("AdjustedPrice", dec.doubleValue());
        if (getLiveEco()) {
            setObject("Price", adjustedPrice);
        }
        calculatePriceChangePercent();

        if (!sell) {
            if (getSister() != null) {
                getSister().setAdjustedPrice(dec.doubleValue() / 2);
            }
        }
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

    public double getAmountTo() {
        return amountTo;
    }

    public void setAmountToDouble(int amt) {
        amountToDouble = amt;
        setObject("DoubleAmount", amt);
        calculatePricePercent();
        calculatePrice();

    }

    public int getAmountToDouble() {
        return (Integer) getObject("DoubleAmount");
    }

    public void calculatePricePercent() {
        priceChangePercent = getAmountTo() / getAmountToDouble();
        setObject("PriceChangePercent", priceChangePercent);
    }

    public void calculatePriceChangePercent() {
        priceChangePercent = getAdjustedPrice() / getOrigPrice();
        setObject("PriceChangePercent", priceChangePercent);
    }

    public double getPriceChangePercent() {
        return ((FileShop) shop).config.getConfigurationSection("Items").getConfigurationSection("" + id).getDouble("PriceChangePercent");
    }

    public double getOrigPrice() {
        return (Double) getObject("OrigPrice");
    }

    public ShopItem getSister() {
        if (sell) {
            return FileShopItem.fromItemStack(shop, item, false);
        } else {
            return FileShopItem.fromItemStack(shop, item, true);
        }
    }

    public void calculatePrice() {
        double p = getOrigPrice() * (getPriceChangePercent() / 100);

        if (getAdjustedPrice() + p < minPrice) {
            setAdjustedPrice(minPrice);
        } else if (getAdjustedPrice() + p > maxPrice) {
            setAdjustedPrice(maxPrice);
        } else {

            setAdjustedPrice(getAdjustedPrice() + p);
        }

        if (!sell) {
            getSister().setObject("LiveEconomy", true);
            getSister().setAdjustedPrice(getAdjustedPrice() / 2);
        }
    }
}
