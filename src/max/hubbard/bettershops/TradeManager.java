package max.hubbard.bettershops;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Shops.FileShop;
import max.hubbard.bettershops.Shops.SQLShop;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.ItemUtils;
import max.hubbard.bettershops.Utils.SQLUtil;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.Trade;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class TradeManager {

    private static HashMap<Shop, List<Trade>> trades = new HashMap<>();
    private static HashMap<String, Trade> ids = new HashMap<>();
    public static HashMap<UUID, Trade> ps = new HashMap<>();

    public static void loadTrades(Shop shop) {
        List<Trade> t = new ArrayList<>();

        if (shop instanceof FileShop) {

            if (((FileShop) shop).config.isConfigurationSection("Trades")) {

                for (String s : ((FileShop) shop).config.getConfigurationSection("Trades").getKeys(false)) {
                    List<ItemStack> items = new ArrayList<>();
                    List<ItemStack> recItems = new ArrayList<>();
                    int gold = ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getInt("Gold");
                    int tradeGold = ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getInt("RecGold");

                    for (String it : ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getConfigurationSection("TradeItems").getKeys(false)) {
                        items.add(((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getConfigurationSection("TradeItems").getItemStack(it));
                    }
                    for (String it : ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getConfigurationSection("ReceiveItems").getKeys(false)) {
                        recItems.add(((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getConfigurationSection("ReceiveItems").getItemStack(it));
                    }

                    boolean traded = ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).isString("Traded") && ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(s).getString("Traded").equalsIgnoreCase("True");

                    Trade tr = new Trade(s, shop, items, gold, recItems, tradeGold, traded);

                    t.add(tr);

                    ids.put(s, tr);
                }

            } else {
                ((FileShop) shop).config.createSection("Trades");
                try {
                    ((FileShop) shop).config.save(((FileShop) shop).file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Statement statement = ((SQLShop) shop).statement;

            try {
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Trades WHERE Shop = '" + shop.getName() + "';");

                while (!set.isClosed() && set.next()) {

                    List<ItemStack> items = new ArrayList<>();
                    List<ItemStack> recItems = new ArrayList<>();
                    int gold = set.getInt("Gold");
                    int tradeGold = set.getInt("RecGold");
                    boolean traded = set.getBoolean("Traded");

                    String g = set.getString("TradeItems");
                    String[] l = g.split("###");

                    for (String s : l) {
                        if (!s.equals(""))
                            items.add(ItemUtils.fromString(s));
                    }

                    g = set.getString("ReceiveItems");
                    l = g.split("###");

                    for (String s : l) {
                        if (!s.equals(""))
                            recItems.add(ItemUtils.fromString(s));
                    }

                    Trade tr = new Trade(set.getString("Id"), shop, items, gold, recItems, tradeGold, traded);

                    t.add(tr);

                    ids.put(set.getString("Id"), tr);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        trades.put(shop, t);
    }

    public static Trade getTrade(String id) {
        return ids.get(id);
    }

    public static void setTraded(Player p,Trade trade, boolean traded) {
        Shop shop = trade.getShop();

        if (shop instanceof FileShop) {
            if (traded) {
                ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).set("Traded", "True");
            } else {
                ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).set("Traded", "False");
            }
            try {
                ((FileShop) shop).config.save(((FileShop) shop).file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Statement statement = ((SQLShop) shop).statement;

            try {
                statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Trades SET Traded = '" + SQLUtil.getBoolValue(traded) + "' WHERE Shop = '" + shop.getName() + "' AND Id = '" + trade.getId() + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        removeTrade(trade, shop);
        addTrade(p,new Trade(trade.getId(), shop, trade.getTradeItems(), trade.getTradeGold(), trade.getRecievingItems(), trade.getReceivingGold(), traded), shop);
    }

    public static void addTrade(Player p,Trade trade, Shop shop) {
        List<Trade> t = trades.get(shop);
        t.add(trade);
        trades.put(shop, t);
        ids.put(trade.getId(), trade);

        if (shop instanceof FileShop) {
            ((FileShop) shop).config.getConfigurationSection("Trades").createSection(trade.getId());
            ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).createSection("TradeItems");
            ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).createSection("ReceiveItems");
            ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).set("Traded", "False");
            ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).set("Gold", trade.getTradeGold());
            ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).set("RecGold", trade.getReceivingGold());

            for (int i = 0; i < trade.getTradeItems().size(); i++) {
                ItemStack it = trade.getTradeItems().get(i);
                ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).getConfigurationSection("TradeItems").set("" + i, it);
            }

            for (int i = 0; i < trade.getRecievingItems().size(); i++) {
                ItemStack it = trade.getRecievingItems().get(i);
                ((FileShop) shop).config.getConfigurationSection("Trades").getConfigurationSection(trade.getId()).getConfigurationSection("ReceiveItems").set("" + i, it);
            }

            try {
                ((FileShop) shop).config.save(((FileShop) shop).file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Statement statement = ((SQLShop) shop).statement;

            try {
                List<String> ti = new ArrayList<>();

                for (ItemStack ite : trade.getTradeItems()) {
                    ti.add(ItemUtils.toString(ite));
                }

                List<String> ti2 = new ArrayList<>();

                for (ItemStack ite : trade.getRecievingItems()) {
                    ti2.add(ItemUtils.toString(ite));
                }

                String s = "";
                String s2 = "";

                for (String s1 : ti) {
                    s = s + s1 + "###";
                }
                for (String s1 : ti2) {
                    s2 = s2 + s1 + "###";
                }

                statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Trades (`Shop`, `Id`, `TradeItems`, `ReceiveItems`, `Gold`, `RecGold`, `Traded`) VALUES" +
                        " ('" + shop.getName() + "', '" + trade.getId() + "', '" + s + "', '" + s2 + "', '" + trade.getTradeGold() + "', '" + trade.getReceivingGold() + "', '"
                        + SQLUtil.getBoolValue(trade.isTraded()) + "');");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (ItemStack it : trade.getTradeItems()) {
            int amt = it.getAmount();
            it.setAmount(1);
            Stocks.removeItemsFromInventory(it, p, amt);
        }


    }

    public static void removeTrade(Trade trade, Shop shop) {


        if (shop instanceof FileShop) {

            ((FileShop) shop).config.getConfigurationSection("Trades").set(trade.getId(), null);
            try {
                ((FileShop) shop).config.save(((FileShop) shop).file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Statement statement = ((SQLShop) shop).statement;

            if (trade.getId().equals("")) {
                return;
            }

            try {
                statement.executeUpdate("DELETE FROM " + Config.getObject("prefix") + "Trades WHERE Shop = '" + shop.getName() + "' AND Id = '" + trade.getId() + "';");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        List<Trade> t = trades.get(shop);
        t.remove(trade);
        trades.put(shop, t);
        ids.remove(trade.getId());
    }

    public static void deleteTrade(Trade trade, Shop shop) {


        if (shop instanceof FileShop) {

            ((FileShop) shop).config.getConfigurationSection("Trades").set(trade.getId(), null);
            try {
                ((FileShop) shop).config.save(((FileShop) shop).file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Statement statement = ((SQLShop) shop).statement;

            try {
                statement.executeUpdate("DELETE FROM " + Config.getObject("prefix") + "Trades WHERE Shop = '" + shop.getName() + "' AND Id = '" + trade.getId() + "';");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        List<Trade> t = trades.get(shop);
        t.remove(trade);
        trades.put(shop, t);
        ids.remove(trade.getId());

        for (ItemStack it : trade.getTradeItems()) {
            int amt = it.getAmount();
            it.setAmount(1);
            Stocks.addItemsToInventory(it, trade.getShop().getOwner().getPlayer(), amt);
        }
    }

    public static List<Trade> getTrades(Shop shop) {
        return trades.get(shop);
    }

    public static Trade getTrade(OfflinePlayer p) {
        return ps.get(p.getUniqueId());
    }
}
