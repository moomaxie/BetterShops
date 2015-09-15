package max.hubbard.bettershops.Shops.Items;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Shops.SQLShop;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.ItemUtils;
import max.hubbard.bettershops.Utils.SQLUtil;
import max.hubbard.bettershops.Utils.Timing;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SQLShopItem implements ShopItem {

    private Shop shop;
    private ItemStack item;
    private boolean sell;
    private int id;
    private byte data;
    private short durability;
    private List<String> lore;
    private String displayName;
    private double priceChangePercent = 1.0;
    private int amountToDouble = 420;
    private double minPrice = 0;
    private double adjustedPrice;
    private double maxPrice = 10000000;
    private double amountTo;
    private Statement statement;
    private Timing autoStock;
    private Timing transCool;

    public static ShopItem createShopItem(Shop shop, ItemStack item, int id, int page, int slot, boolean sell) {
        return new SQLShopItem(shop, item, id, page, slot, sell);
    }

    public static ShopItem fromItemStack(Shop shop, ItemStack item, boolean sell) {
        if (shop != null && shop.getShopItems() != null)
            for (ShopItem item1 : shop.getShopItems()) {
                if (item1.isSelling() == sell && item1.getItem().isSimilar(item)) {
                    return item1;
                }
            }
        return null;
    }

    public static ShopItem fromId(Shop shop, int id, boolean sell) {
        if (shop != null && shop.getShopItems() != null)
            for (ShopItem item1 : shop.getShopItems()) {
                if (item1.isSelling() == sell && item1.getId() == id) {
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

    protected SQLShopItem(Shop shop, ItemStack item, int id, int page, int slot, boolean sell) {

        if (shop instanceof SQLShop) {
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

            if (getObject("AutoStock") != null) {
                autoStock = new Timing(this, (String) getObject("AutoStock"), true);
            } else {
                autoStock = new Timing(this, 0, 0, 0, 0, 0, true);
                setObject("AutoStock", autoStock.toString());
            }

            if (getObject("TransCool") != null) {
                transCool = new Timing(this, (String) getObject("TransCool"), false);
            } else {
                transCool = new Timing(this, 0, 0, 0, 0, 0, false);
                setObject("TransCool", transCool.toString());
            }

            try {
                this.statement = Core.getConnection().createStatement();
                String l = null;
                if (lore != null) {
                    l = lore.get(0);
                    for (int i = 1; i < lore.size(); i++) {
                        l = l + "||BS||" + lore.get(i);
                    }
                }

                String enchants = "";
                if (item.getEnchantments().size() > 0) {
                    for (Enchantment en : item.getEnchantments().keySet()) {
                        enchants = enchants + "||BS||" + en.getName() + "-" + item.getEnchantments().get(en);
                    }
                }

                statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Items (Shop, Id, Item, DisplayName, Lore, Enchants, Page, Slot, Selling, Stock, Amount, Price, OrigPrice, Infinite, " +
                        "LiveEconomy, PriceChangePercent, DoubleAmount, MinimumPrice, MaximumPrice, AdjustedPrice, SellLimit) VALUES " +
                        "('" + shop.getName() + "', '" + id + "', '" + ItemUtils.toString(item) + "', '" + displayName + "', '" + l + "', '" + enchants + "', '" + page + "', '" + slot + "', '" + SQLUtil.getBoolValue(sell) + "', '" + 0 + "', " +
                        "'" + 1 + "', '" + Config.getObject("DefaultPrice") + "', '" + Config.getObject("DefaultPrice") + "', '" + 0 + "', '" + 0 + "', '" + priceChangePercent + "', " +
                        "'" + amountToDouble + "', '" + minPrice + "', '" + maxPrice + "', '" + Config.getObject("DefaultPrice") + "', '" + 0 + "');");

                if (!sell) {
                    setObject("Stock", 1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected SQLShopItem(Shop shop, int id) {
        if (shop instanceof SQLShop) {

            this.shop = shop;
            this.id = id;

            try {
                this.statement = Core.getConnection().createStatement();
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Items WHERE Shop='" + shop.getName() + "' AND Id='" + id + "';");
                set.next();
                String ite = set.getString("Item");
                Map<String, Object> m = ItemUtils.deserialize(ite);
                try {
                    this.item = ItemStack.deserialize(m);
                } catch (Exception e) {
                    this.item = ItemUtils.fromString(set.getString("Item"));
                    if (item.getItemMeta() != null) {
                        if (set.getString("Lore") != null && !set.getString("Lore").equals("null"))
                            lore = Arrays.asList(set.getString("Lore").split(Pattern.quote("||BS||")));
                        if (set.getString("DisplayName") != null && !set.getString("DisplayName").equals("null"))
                            displayName = set.getString("DisplayName");
                    }

                    ItemMeta meta = item.getItemMeta();
                    if (displayName != null) {
                        meta.setDisplayName(displayName);
                    }
                    if (lore != null) {
                        meta.setLore(lore);
                    }
                    item.setItemMeta(meta);

                    if (set.getString("Enchants") != null && !set.getString("Enchants").equals("null")) {
                        String en = set.getString("Enchants");

                        String[] split = en.split(Pattern.quote("||BS||"));

                        for (String s : split) {
                            if (s.contains("-")) {
                                String[] sp = s.split(Pattern.quote("-"));
                                item.addEnchantment(Enchantment.getByName(sp[0]), Integer.parseInt(sp[1]));
                            }
                        }
                    }
                }


                this.sell = set.getBoolean("Selling");

                data = item.getData().getData();
                durability = item.getDurability();

                priceChangePercent = set.getDouble("PriceChangePercent");
                amountToDouble = set.getInt("DoubleAmount");
                minPrice = set.getDouble("MinimumPrice");
                maxPrice = set.getDouble("MaximumPrice");
                adjustedPrice = set.getDouble("AdjustedPrice");

                if (!getLiveEco()) {
                    setAdjustedPrice(getPrice());
                } else {
                    calculateAmountTo();
                }

                if (getObject("AutoStock") != null) {
                    autoStock = new Timing(this, (String) getObject("AutoStock"), true);
                } else {
                    autoStock = new Timing(this, 0, 0, 0, 0, 0, true);
                    setObject("AutoStock", autoStock.toString());
                }

                if (getObject("TransCool") != null) {
                    transCool = new Timing(this, (String) getObject("TransCool"), false);
                } else {
                    transCool = new Timing(this, 0, 0, 0, 0, 0, false);
                    setObject("TransCool", transCool.toString());
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    public static ShopItem loadShopItem(Shop shop, int id) {
        return new SQLShopItem(shop, id);
    }

    public Object getObject(String s) {
        ResultSet set;

        try {
            if (statement == null || statement.isClosed()) {
                statement = Core.getConnection().createStatement();
            }
            set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Items WHERE Shop='" + shop.getName() + "' AND Id='" + id + "';");

            if (set != null && !set.isClosed() && set.next()) {
                try {
                    return set.getObject(s);
                } catch (Exception e1) {
                    return getObject(s);
                }
            }
        } catch (SQLException r) {
            r.printStackTrace();
        }
        return null;
    }

    public void setObject(String s, Object o) {
        if (o instanceof Boolean) {
            o = SQLUtil.getBoolValue((boolean) o);
        }
        try {
            statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Items SET `" + s + "` = '" + o + "' WHERE Shop='" + shop.getName() + "' AND Id='" + id + "';");
        } catch (SQLException r) {
            r.printStackTrace();
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
        return (Boolean) getObject("Infinite");
    }

    public boolean getLiveEco() {
        return (Boolean) getObject("LiveEconomy");
    }

    @Override
    public boolean isAutoStock() {
        if (getObject("Auto") != null) {
            return (Boolean) getObject("Auto");
        } else {
            setObject("Auto", false);
            return false;
        }
    }

    @Override
    public boolean isTransCooldown() {
        if (getObject("Trans") != null) {
            return (Boolean) getObject("Trans");
        } else {
            setObject("Trans", false);
            return false;
        }
    }

    @Override
    public boolean isSellEco() {
        if (getObject("SellEco") != null)
            return (Boolean) getObject("SellEco");
        else {
            setObject("SellEco", false);
            return false;
        }
    }

    @Override
    public Timing getAutoStockTiming() {
        return autoStock;
    }

    @Override
    public Timing getTransCooldownTiming() {
        return transCool;
    }

    public int getPage() {
        if (getObject("Page") != null) {
            return (Integer) getObject("Page");
        } else {
            return 1;
        }
    }

    public int getSlot() {
        return (Integer) getObject("Slot");
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
        return (int) getObject("SellLimit");
    }

    public int getStock() {
        return (Integer) getObject("Stock");
    }

    public double getPrice() {
        return ((Double) getObject("Price"));
    }

    public byte getData() {
        return data;
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

        return (double) getObject("AdjustedPrice");
    }

    public String getAdjustedPriceAsString() {

        BigDecimal dec = new BigDecimal(getAdjustedPrice());
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        return dec.toPlainString();
    }

    public int getMinPrice() {
        return (Integer) getObject("MinPrice");
    }

    public void setAdjustedPrice(double amt) {

        BigDecimal dec = new BigDecimal(amt);
        dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

        this.adjustedPrice = dec.doubleValue();
        setObject("AdjustedPrice", dec.doubleValue());
        if (getLiveEco()) {
            setObject("Price", adjustedPrice);
        }

        if (!sell) {
            if (getSister() != null) {
                if (!isSellEco())
                    getSister().setAdjustedPrice(dec.doubleValue() / 2);
            }
        }
    }

    public void setAmountTo(double amt) {
        if (!sell || isSellEco()) {
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

    public double calculateAmountTo() {
        amountTo = getPriceChangePercent() * getAmountToDouble();
        return amountTo;
    }

    public double getAmountTo() {
        return amountTo;
    }

    public void setAmountToDouble(int amt) {
        this.amountToDouble = amt;
        setObject("DoubleAmount", amt);
        if (!sell && getSister() != null && !isSellEco()) {
            getSister().setAmountToDouble(amt);
        }
        calculatePricePercent();
        calculatePrice();

    }

    public int getAmountToDouble() {
        return (Integer) getObject("DoubleAmount");
    }

    public void calculatePricePercent() {
        priceChangePercent = (amountTo / amountToDouble);
        setObject("PriceChangePercent", priceChangePercent);
    }

    public void calculatePriceChangePercent() {
        priceChangePercent = getAdjustedPrice() / getOrigPrice();
        if (!Double.isNaN(priceChangePercent) && !Double.isInfinite(priceChangePercent)) {
            setObject("PriceChangePercent", priceChangePercent);
        } else {
            setObject("PriceChangePercent", 1.0);
        }
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public double getOrigPrice() {
        return ((Double) getObject("OrigPrice"));
    }

    public ShopItem getSister() {
        if (sell) {
            return SQLShopItem.fromItemStack(shop, item, false);
        } else {
            return SQLShopItem.fromItemStack(shop, item, true);
        }
    }

    public void calculatePrice() {
        double p = getOrigPrice() + (getOrigPrice() * (priceChangePercent/100));

        if (p < minPrice) {
            setAdjustedPrice(minPrice);
        } else if (p > maxPrice) {
            setAdjustedPrice(maxPrice);
        } else {

            setAdjustedPrice(p);
        }
        if (!sell) {
            getSister().setObject("LiveEconomy", true);
            if (!isSellEco()) {
                getSister().setAdjustedPrice(p / 2);
            }
        }


    }
}
