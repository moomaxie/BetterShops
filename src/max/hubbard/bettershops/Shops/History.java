package max.hubbard.bettershops.Shops;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Utils.Transaction;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
public class History {

    private LinkedList<Transaction> Buytransactions = new LinkedList<>();
    private LinkedList<Transaction> transactions = new LinkedList<>();
    private LinkedList<Transaction> Selltransactions = new LinkedList<>();

    public HashMap<UUID, LinkedList<Transaction>> playerTrans = new HashMap<>();
    public HashMap<String, LinkedList<Transaction>> nameTrans = new HashMap<>();

    public YamlConfiguration config = null;
    private File file = null;

    private Shop shop;

    public History(Shop shop) {
        this.shop = shop;
        file = new File(Core.getCore().getDataFolder(), "Transactions/" + shop.getName() + ".yml");
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void addTransaction(OfflinePlayer p, Date date, ShopItem item, double price, int amount, boolean sell, boolean save) {

        Transaction trans = new Transaction(p, date, item, price, amount, sell);

        if (sell) {
            Selltransactions.add(trans);
        } else {
            Buytransactions.add(trans);
        }

        transactions.add(trans);

        if (playerTrans.containsKey(p.getUniqueId())) {
            LinkedList<Transaction> tr = playerTrans.get(p.getUniqueId());
            tr.add(trans);
            playerTrans.put(p.getUniqueId(), tr);
        } else {
            LinkedList<Transaction> tr = new LinkedList<>();
            tr.add(trans);
            playerTrans.put(p.getUniqueId(), tr);
        }

        if (save) {
            if (shop instanceof FileShop) {
                shop.saveTransaction(trans, true);
                saveTransactionToFile(trans);
            } else {
                try {
                    saveToSQL(trans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addTransaction(String p, Date date, String item, double price, int amount, boolean sell, boolean save) {

        Transaction trans = new Transaction(p, date, item, price, amount, sell);

        if (sell) {
            Selltransactions.add(trans);
        } else {
            Buytransactions.add(trans);
        }

        transactions.add(trans);

        if (nameTrans.containsKey(p)) {
            LinkedList<Transaction> tr = nameTrans.get(p);
            tr.add(trans);
            nameTrans.put(p, tr);
        } else {
            LinkedList<Transaction> tr = new LinkedList<>();
            tr.add(trans);
            nameTrans.put(p, tr);
        }

        if (save) {
            if (shop instanceof FileShop) {
                shop.saveTransaction(trans, true);
                saveTransactionToFile(trans);
            } else {
                try {
                    saveToSQL(trans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addTransaction(OfflinePlayer p, Date date, String item, double price, int amount, boolean sell, boolean save) {

        Transaction trans = new Transaction(p, date, item, price, amount, sell);

        if (sell) {
            Selltransactions.add(trans);
        } else {
            Buytransactions.add(trans);
        }

        transactions.add(trans);

        if (playerTrans.containsKey(p.getUniqueId())) {
            LinkedList<Transaction> tr = playerTrans.get(p.getUniqueId());
            tr.add(trans);
            playerTrans.put(p.getUniqueId(), tr);
        } else {
            LinkedList<Transaction> tr = new LinkedList<>();
            tr.add(trans);
            playerTrans.put(p.getUniqueId(), tr);
        }

        if (save) {
            if (shop instanceof FileShop) {
                shop.saveTransaction(trans, true);
                saveTransactionToFile(trans);
            } else {
                try {
                    saveToSQL(trans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public LinkedList<Transaction> getBuyingTransactions() {
        return Buytransactions;
    }

    public LinkedList<Transaction> getSellingTransactions() {
        return Selltransactions;
    }

    public LinkedList<Transaction> getAllTransactions() {
        return transactions;
    }

    public Shop getShop() {
        return shop;
    }

    public void clearAllTransactions() {
        transactions.clear();
        Buytransactions.clear();
        Selltransactions.clear();
    }

    public void clearHistory() { //Everyday
        transactions.clear();
        Buytransactions.clear();
        Selltransactions.clear();

        shop.clearTransactions();
    }

    public void saveTransactionToFile(Transaction t) {

        if (file == null) {

            file = new File(Core.getCore().getDataFolder(), "Transactions/" + shop.getName() + ".yml");

            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }

        if (config == null) {
            config = YamlConfiguration.loadConfiguration(file);
        }
        int next = config.getKeys(false).size() + 1;

        config.createSection("" + next);

        config.getConfigurationSection("" + next).set("Shop Owner UUID", shop.getOwner().getUniqueId().toString());
        config.getConfigurationSection("" + next).set("Shop Owner Name", shop.getOwner().getName());
        config.getConfigurationSection("" + next).set("Date", t.getDate().toLocaleString());
        config.getConfigurationSection("" + next).set("Buyer UUID", t.getPlayer().getUniqueId().toString());
        config.getConfigurationSection("" + next).set("Buyer Name", t.getPlayer().getName());
        config.getConfigurationSection("" + next).set("Item", t.getItem());
        config.getConfigurationSection("" + next).set("Price", t.getPrice());
        config.getConfigurationSection("" + next).set("Amount", t.getAmount());
        config.getConfigurationSection("" + next).set("Selling Shop", t.isSell());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToSQL(Transaction t) throws Exception {
        Statement statement;
        statement = Core.getConnection().createStatement();

        String item = t.getItem();
        String player;
        if (t.getPlayer() != null) {
            player = t.getPlayer().getUniqueId().toString();
        } else {
            player = t.getPlayerName();
        }
        String owner = shop.getOwner().getUniqueId().toString();
        String date = t.getDate().toLocaleString();
        boolean sell = t.isSell();
        double price = t.getPrice();
        int amt = t.getAmount();

        statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Transactions (`Shop`, `Item`, `Player`, `Owner`, `Price`, `Amount`, `Selling`, `Date`) VALUES" +
                " ('" + shop.getName() + "', '" + item + "', '" + player + "', '" + owner + "', '" + price + "', '" + amt + "', "
                + sell + ", '" + date + "'" +
                ");");
    }

    public static void saveAllTransactionsToSQL() throws Exception {
        File fi = new File(Core.getCore().getDataFolder(), "Transactions");

        if (!fi.exists()) {
            fi.mkdirs();
        }
        Statement statement;
        statement = Core.getConnection().createStatement();
        for (File f : fi.listFiles()) {
            if (f.getName().contains(".yml")) {
                YamlConfiguration c = YamlConfiguration.loadConfiguration(f);

                for (String s : c.getKeys(false)) {
                    String item = c.getConfigurationSection(s).getString("Item").replaceAll("'", "");
                    String player = c.getConfigurationSection(s).getString("Buyer Name");
                    String owner = c.getConfigurationSection(s).getString("Owner Name");
                    String date = c.getConfigurationSection(s).getString("Date");
                    boolean sell = c.getConfigurationSection(s).getBoolean("Selling Shop");
                    double price = c.getConfigurationSection(s).getDouble("Price");
                    int amt = c.getConfigurationSection(s).getInt("Amount");


                    statement.executeUpdate("INSERT IGNORE INTO " + Config.getObject("prefix") + "Transactions (`Shop`, `Item`, `Player`, `Owner`, `Price`, `Amount`, `Selling`, `Date`) VALUES" +
                            " ('" + f.getName().substring(0, f.getName().length() - 4) + "', '" + item + "', '" + player + "', '" + owner + "', '" + price + "', '" + amt + "', "
                            + sell + ", '" + date + "'" +
                            ");");
                }
            }
        }
    }
}
