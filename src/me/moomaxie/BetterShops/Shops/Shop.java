package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.History.History;
import me.moomaxie.BetterShops.History.Transaction;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Shop {

    private OfflinePlayer p;
    private Location l;
    private String name;
    public File file;
    private YamlConfiguration config;
    private History history = new History(this);

    private LinkedList<ShopItem> sellItems = null;
    private LinkedList<ShopItem> buyItems = null;
    private LinkedList<ShopItem> items = null;

    public boolean transLoaded = false;


    /**
     * @param owner    - the owner of the shop
     * @param shopName - The name of the shop
     */

    public Shop(Player owner, String shopName) {
        name = shopName;

        if (name != null) {
            File file = new File(Core.getCore().getDataFolder(), "Shops");

            if (file == null) {
                file.mkdir();
            }

            if (file.listFiles() != null) {

                for (File f : file.listFiles()) {
                    if (f.getName().contains(".yml")) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                        if (config != null) {


                            for (String s : config.getKeys(false)) {
                                if (name.equals(s)) {
                                    s = s + ".Location";
                                    if (s.contains("Location")) {
                                        String c = config.getString(s);

                                        if (c != null) {

                                            String[] locs = c.split(" ");

                                            World w = Bukkit.getWorld(locs[0]);

                                            double x = Double.parseDouble(locs[1]);
                                            double y = Double.parseDouble(locs[2]);
                                            double z = Double.parseDouble(locs[3]);

                                            p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getString("Owner")));

                                            l = new Location(w, x, y, z);

                                            this.config = config;

                                            this.file = f;

                                            loadTransactions();

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param shopName - The name of the shop
     */
    public Shop(String shopName) {
        name = shopName;

        if (name != null) {
            File file = new File(Core.getCore().getDataFolder(), "Shops");

            if (file == null) {
                file.mkdir();
            }

            if (file.listFiles() != null) {

                for (File f : file.listFiles()) {
                    if (f.getName().contains(".yml")) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                        if (config != null) {


                            for (String s : config.getKeys(false)) {
                                if (name.equals(s)) {
                                    s = s + ".Location";
                                    if (s.contains("Location")) {
                                        String c = config.getString(s);

                                        if (c != null) {

                                            String[] locs = c.split(" ");

                                            World w = Bukkit.getWorld(locs[0]);

                                            double x = Double.parseDouble(locs[1]);
                                            double y = Double.parseDouble(locs[2]);
                                            double z = Double.parseDouble(locs[3]);

                                            p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getString("Owner")));

                                            l = new Location(w, x, y, z);

                                            this.config = config;

                                            this.file = f;

                                            loadTransactions();

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param loc - the location of the shop (where the chest is)
     */
    public Shop(Location loc) {

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file == null) {
            file.mkdir();
        }

        if (file.listFiles() != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (w != null) {

                                if (loc.getWorld().getName().equals(w.getName())) {
                                    if (loc.getX() == x && loc.getY() == y && loc.getZ() == z) {
                                        l = loc;

                                        String n = s.substring(0, s.length() - 9);

                                        this.file = f;

                                        name = n;

                                        this.config = config;

                                        p = Bukkit.getOfflinePlayer(UUID.fromString(config.getString(name + ".Owner")));

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return name - the name of the shop
     */
    public String getName() {
        return name;
    }

    /**
     * @param loc - the new location of the shop
     */
    public void setLocation(Location loc) {
        this.l = loc;

        config.set(name + ".Location", loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the description of the shop
     */
    public String getDescription() {
        return config.getString(name + ".Description");
    }

    /**
     * @param desc - the new description
     */
    public void setDescription(String desc) {
        config.set(name + ".Description", desc);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return a list of Offline Players who are shop managers
     */
    public List<OfflinePlayer> getManagers() {
        List<OfflinePlayer> mans = new ArrayList<OfflinePlayer>();

        if (config != null && name != null) {
            if (config.isConfigurationSection(name) && config.getConfigurationSection(name).isConfigurationSection("Managers")) {
                for (String id : config.getConfigurationSection(name).getConfigurationSection("Managers").getKeys(false)) {
                    UUID uid = UUID.fromString(id);

                    mans.add(Bukkit.getOfflinePlayer(uid));
                }
            }
        }
        return mans;
    }

    /**
     * @param p - yhe new shop manager
     */
    public void addManager(OfflinePlayer p) {

        if (config != null && name != null) {
            if (!config.getConfigurationSection(name).isConfigurationSection("Managers")) {
                config.getConfigurationSection(name).createSection("Managers");
            }
            if (config.isConfigurationSection(name) && config.getConfigurationSection(name).isConfigurationSection("Managers")) {

                config.getConfigurationSection(name).getConfigurationSection("Managers").set(p.getUniqueId().toString(), p.getUniqueId().toString());

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the transaction history of the shop
     */
    public History getHistory() {
        return history;
    }

    /**
     * @param p - the manager to remove
     */
    public void removeManager(OfflinePlayer p) {
        if (getManagers().contains(p)) {
            config.getConfigurationSection(name).getConfigurationSection("Managers").set(p.getUniqueId().toString(), null);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return true if open, false if closed
     */
    public boolean isOpen() {

        return config != null && config.isString(name + ".Open") && config.getConfigurationSection(name).getString("Open").equals("True");
    }

    /**
     * @return true if a server shop, false if not
     */
    public boolean isServerShop() {

        try {
            if (config != null) {
                if (config.isString(name + ".Server")) {
                    return config.getConfigurationSection(name).getString("Server").equals("True");
                } else {
                    setServerShop(false);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            setServerShop(false);
            return false;
        }
    }

    /**
     * @return true if an NPC shop, false if not
     */
    public boolean isNPCShop() {

        try {
            if (config != null) {
                if (config.isString(name + ".NPC")) {
                    return config.getConfigurationSection(name).getString("NPC").equals("True");
                } else {
                    setNPCShop(false);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            setNPCShop(false);
            return false;
        }
    }

    /**
     * @return true if shop notifications are on, false if off
     */
    public boolean isNotify() {

        return config != null && config.isString(name + ".Notify") && config.getConfigurationSection(name).getString("Notify").equals("True");

    }

    /**
     * @return the location of the shop
     */
    public Location getLocation() {
        return l;
    }

    /**
     * @return the owner of the shop as an Offline Player
     */
    public OfflinePlayer getOwner() {
        return p;
    }

    /**
     * @param item - the itemstack to test
     * @param sell - true if a selling shop, false if a buying shop
     * @return true if the item is already in the shop, false if not
     */
    public boolean alreadyBeingSold(ItemStack item, boolean sell) {

        boolean exists = false;

        int a = item.getAmount();

        item.setAmount(1);

        if (item.getType() != Material.SKULL_ITEM) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = getLore(item);


            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = getLore(item);


            meta.setLore(lore);
            item.setItemMeta(meta);
        }


        if (getShopContents(sell).keySet().contains(item)) {
            exists = true;
        }


        for (ItemStack it : getShopContents(sell).keySet()) {
            ItemMeta meta = it.getItemMeta();
            meta.setLore(getLore(it));
            it.setItemMeta(meta);
            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                exists = true;
            }

        }


        if (!exists) {
            int amt = item.getAmount();
            item.setAmount(1);

            if (getShopContents(sell).containsKey(item)) {
                exists = true;
            }
            for (ItemStack it : getShopContents(sell).keySet()) {
                int am = it.getAmount();
                it.setAmount(1);
                if (item.equals(it)) {
                    exists = true;
                }

                if (!item.toString().equals(it.toString())) {
                    exists = false;
                }

                it.setAmount(am);
            }


            item.setAmount(amt);
        }

        for (ItemStack it : getShopContents(sell).keySet()) {
            int am = it.getAmount();
            it.setAmount(1);

            if (!item.toString().equals(it.toString())) {
                exists = false;
            }

            if (item.equals(it) || item.toString().equals(it.toString())) {
                if (it.getData().getData() != item.getData().getData()) {
                    exists = false;

                } else {

                    if (it.getDurability() != item.getDurability()) {
                        exists = false;

                    } else {
                        exists = true;
                        break;
                    }
                    break;
                }


            }

            it.setAmount(am);
        }

        item.setAmount(a);

        return exists;
    }

    public void clearTransactions() {
        if (config.getConfigurationSection(name).isConfigurationSection("Transactions")) {
            config.getConfigurationSection(name).set("Transactions", null);
        } else {
            config.getConfigurationSection(name).createSection("Transactions");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTransactions() {
        if (config.getConfigurationSection(name).isConfigurationSection("Transactions")) {

            if (history != null) {
                history.clearAllTransactions();
            }


            for (String s : config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false)) {

                Date d = new Date(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getLong("Date"));
                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getString("Player")));
                ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getItemStack("Item");
                double price = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getDouble("Price");
                int amount = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getInt("Amount");
                boolean sell = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getBoolean("Selling");


                history.addTransaction(p, d, item, price, amount, sell, false);

            }



        } else {
            config.getConfigurationSection(name).createSection("Transactions");

        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        transLoaded = true;
    }

    /**
     * @param t    - the Transaction to save
     * @param save - a boolean whether to save to the file or not
     */
    public void saveTransaction(Transaction t, boolean save) {

        if (!config.getConfigurationSection(name).isConfigurationSection("Transactions")) {
            config.getConfigurationSection(name).createSection("Transactions");
        }

        int amt = config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false).size();

        config.getConfigurationSection(name).getConfigurationSection("Transactions").createSection("" + (amt + 1));

        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Date", t.getDate().getTime());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Player", t.getPlayer().getUniqueId().toString());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Item", t.getItem());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Price", t.getPrice());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Amount", t.getAmount());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Selling", t.isSell());

        if (save) {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param item - the Itemstack to add
     * @param inv  - The current inventory open
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void addItem(ItemStack item, Inventory inv, boolean sell) {

        ItemMeta meta = item.getItemMeta();
        meta.setLore(getLore(item));
        item.setItemMeta(meta);

        if (!sell) {
            if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                config.getConfigurationSection(name).createSection("Contents");
            }
        } else {
            if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                config.getConfigurationSection(name).createSection("Sell");
            }
        }

        if (alreadyBeingSold(item, sell)) {

            if (getOwner().isOnline()) {
                getOwner().getPlayer().sendMessage(Messages.getPrefix() + "§aYou can add more of an item by going into the item settings!");
            }

            int amt1 = item.getAmount();

            item.setAmount(1);

            if (!sell) {

                int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt + 1);
            }

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            item.setAmount(amt1);
        } else {

            int amt = item.getAmount();

            item.setAmount(1);

            if (!sell) {

                config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(inv.firstEmpty() + "");
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("ItemStack", item);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Name", item.getItemMeta().getDisplayName());

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Stock", 1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Price", Config.getDefaultPrice());
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Amount", 1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Infinite", "False");
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Slot", inv.firstEmpty());
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(inv.firstEmpty() + "");
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("ItemStack", item);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Name", item.getItemMeta().getDisplayName());

                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Stock", 0);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Price", Config.getDefaultPrice());
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Amount", 1);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Slot", inv.firstEmpty());
            }

            item.setAmount(amt);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    /**
     * @param item - The Itemstack to add
     * @param slot - the slot number to add it in
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void addItem(ItemStack item, int slot, boolean sell) {


        if (item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();
            meta.setLore(getLore(item));
            item.setItemMeta(meta);

            if (!sell) {
                if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                    config.getConfigurationSection(name).createSection("Contents");
                }
            } else {
                if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                    config.getConfigurationSection(name).createSection("Sell");
                }
            }

            if (alreadyBeingSold(item, sell)) {

                if (getOwner().isOnline()) {
                    getOwner().getPlayer().sendMessage(Messages.getPrefix() + "§aYou can add more of an item by going into the item settings!");
                }

                int amt1 = item.getAmount();

                item.setAmount(1);

                if (!sell) {

                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt + 1);

                }

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                item.setAmount(amt1);
            } else {

                int amt = item.getAmount();

                item.setAmount(1);

                if (!sell) {

                    config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(slot + "");
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Name", item.getItemMeta().getDisplayName());

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Stock", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Price", Config.getDefaultPrice());
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Amount", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Infinite", "False");
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Slot", slot);
                } else {
                    config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(slot + "");
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Name", item.getItemMeta().getDisplayName());

                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Stock", 0);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Price", Config.getDefaultPrice());
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Amount", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Slot", slot);
                }


                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                item.setAmount(amt);
            }
        }
    }

    /**
     * @param open - true if open, false if closed
     */
    public void setOpen(boolean open) {
        if (open) {
            config.set(name + ".Open", "True");
        } else {
            config.set(name + ".Open", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param open - true if server shop, false if not
     */
    public void setServerShop(boolean open) {
        if (open) {
            config.set(name + ".Server", "True");
        } else {
            config.set(name + ".Server", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param open - true if an NPC shop, false if not
     */
    public void setNPCShop(boolean open) {
        if (open) {
            config.set(name + ".NPC", "True");
        } else {
            config.set(name + ".NPC", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param notify - true if notify, false if not
     */
    public void setNotification(boolean notify) {
        if (notify) {
            config.set(name + ".Notify", "True");
        } else {
            config.set(name + ".Notify", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param item - The item to set
     * @param amt  - the new price
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void setPrice(ItemStack item, double amt, boolean sell) {

        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Price", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Price", amt);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param item - The item to set
     * @param amt  - the new stock
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void setStock(ItemStack item, int amt, boolean sell) {

        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param item - the item to set
     * @param amt  - the new slot
     * @param sell - true if a selling shop, false if a buying shop
     */
    @Deprecated
    public void setSlot(ItemStack item, int amt, boolean sell) {
        if (alreadyBeingSold(item, sell)) {
            if (!sell) {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Slot", amt);
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Slot", amt);
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param item - the item to set
     * @param in   - true if infinite, false if not
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void setInfinite(ItemStack item, boolean in, boolean sell) {

        if (!sell) {
            if (in) {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Infinite", "True");
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Infinite", "False");
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param item - the item to set
     * @param amt  - the new amount
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void setAmount(ItemStack item, int amt, boolean sell) {
        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Amount", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Amount", amt);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param item1 - the first item to compare
     * @param item2 - the second to compare
     * @param sell  - true if a selling shop, false if a buying shop
     * @return true if the same, false if not
     */
    public boolean compareItems(ItemStack item1, ItemStack item2, boolean sell) {
        boolean exists = false;


        if (item1 != null && item2 != null) {

            if (item1.getType() != Material.SKULL_ITEM && item2.getType() != Material.SKULL_ITEM && !item1.getType().name().contains("LEATHER_") && !item2.getType().name().contains("LEATHER_")) {

                ItemMeta meta = item1.getItemMeta();


                meta.setLore(getLore(item1));
                item1.setItemMeta(meta);


                ItemMeta meta2 = item2.getItemMeta();


                meta2.setLore(getLore(item2));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1)) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        if (item1 != null && item2 != null) {

            if (item1.getType() == Material.SKULL_ITEM && item2.getType() == Material.SKULL_ITEM) {


                SkullMeta meta = (SkullMeta) item1.getItemMeta();


                meta.setLore(getLore(item1));
                item1.setItemMeta(meta);


                SkullMeta meta2 = (SkullMeta) item2.getItemMeta();


                meta2.setLore(getLore(item2));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1) || item1 == item2 || item1.toString().equals(item2.toString())) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        if (item1 != null && item2 != null) {

            if (item1.getType().name().contains("LEATHER_") && item2.getType().name().contains("LEATHER_")) {

                LeatherArmorMeta meta = (LeatherArmorMeta) item1.getItemMeta();


                meta.setLore(getLore(item1));
                item1.setItemMeta(meta);


                LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();


                meta2.setLore(getLore(item2));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1)) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        return exists;
    }

    /**
     * @param item - item to check
     * @param sell - true if a selling shop, false if a buying shop
     * @return the price of the item as a Double
     */
    public Double getPrice(ItemStack item, boolean sell) {
        double price = Config.getDefaultPrice();
        if (item != null) {
            if (!sell) {
                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").isDouble("Price")) {
                    price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                } else {
                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Price").contains(",")) {
                        price = Double.parseDouble(config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Price").replaceFirst(",", ".").replaceAll(",", ""));
                    } else {
                        price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Price");
                    }
                }
            } else {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").isDouble("Price")) {
                    price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                } else {
                    if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Price").contains(",")) {
                        price = Double.parseDouble(config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Price").replaceFirst(",", ".").replaceAll(",", ""));
                    } else {
                        price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Price");
                    }
                }
            }
        }

        if (String.valueOf(price).contains(",")){
            price = Double.parseDouble(String.valueOf(price).replaceFirst(",", ".").replaceAll(",", ""));
        }


        return Double.parseDouble(new DecimalFormat("#.00").format(price).replaceFirst(",", ".").replaceAll(",", ""));
    }

    /**
     * @param sell - true if a selling shop, false if a buying shop
     * @return the highest non-empty slot in the shop
     */
    public Integer getHighestSlot(boolean sell) {
        int higest = 18;
        if (!sell) {

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {
                int h = Integer.parseInt(s);

                if (h > higest) {
                    higest = h;
                }
            }

        } else {
            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {
                int h = Integer.parseInt(s);

                if (h > higest) {
                    higest = h;
                }
            }
        }
        return higest;
    }

    /**
     * @param item - the item to check
     * @return the lore of the item as a String list
     */
    public List<String> getLore(ItemStack item) {

        List<String> lore = null;
        ItemMeta meta = item.getItemMeta();
        List<String> l = meta.getLore();


        if (l != null) {

            if (l.contains(MainGUI.getString("ShopKeeperManage"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 7; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("ManageItem"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(ItemTexts.getString("RemoveItemLore"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 5; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }


            if (l.contains(MainGUI.getString("SellItem"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 5; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("SellOptions"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }


            if (l.contains(MainGUI.getString("Arrange"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 3; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("Selected"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 3; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("AddToCart"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(Checkout.getString("ClickToRemove"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 5; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("Price"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("Amount"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("AskingPrice"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("AskingAmount"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("Stock"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

        }
        lore = l;

        return lore;
    }

    /**
     * @param item - the item to check
     * @param sell - true if a selling shop, false if a buying shop
     * @return the slot of the item
     */
    @Deprecated
    public Integer getSlot(ItemStack item, boolean sell) {
        int price = 18;
        if (alreadyBeingSold(item, sell)) {

            price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");
        }
        return price;
    }

    /**
     * @param item - the item to work with
     * @param sell - true if a selling shop, false if a buying shop
     * @return the stock of the item
     */
    public Integer getStock(ItemStack item, boolean sell) {
        int price = 1;

        if (item != null) {

            if (!sell) {
                if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(getSlotForItem(item, sell) + ""))
                    price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");
            } else {

                if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(getSlotForItem(item, sell) + ""))
                    price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");
            }
        }
        return price;
    }

    /**
     * @param item - the item to work with
     * @param sell - true if a selling shop, false if a buying shop
     * @return true if infinite, false if not
     */
    public boolean isInfinite(ItemStack item, boolean sell) {
        if (alreadyBeingSold(item, sell)) {

            if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite").equals("True")) {
                return true;
            } else {
                return false;
            }
        } else {
            setInfinite(item, false, sell);
            return false;
        }

    }

    /**
     * @param item - the item to work with
     * @param sell - true if a selling shop, false if a buying shop
     * @return the amount the item sells for
     */
    public Integer getAmount(ItemStack item, boolean sell) {
        int price = 1;

        if (!sell) {
            price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
        } else {
            price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
        }


        return price;
    }

    /**
     * @param item1 - the first item
     * @param slot1 - the first item's current slot
     * @param item2 - the second item
     * @param slot2 - the second item's current slot
     * @param sell  - true if a selling shop, false if a buying shop
     */
    public void exchangeItems(ItemStack item1, int slot1, ItemStack item2, int slot2, boolean sell) {
        if (!sell && item2 != null && item1 != null) {
            if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(slot2 + "")) {

                double price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getDouble("Price");
                int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getInt("Amount");
                int stock = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getInt("Stock");

                String inf = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getString("Infinite");

                double price2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getDouble("Price");
                int amt2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getInt("Amount");
                int stock2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getInt("Stock");

                String inf2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getString("Infinite");


                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("ItemStack", item1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Stock", stock);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Price", price);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Amount", amt);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Infinite", inf);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Slot", slot2);

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("ItemStack", item2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Stock", stock2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Price", price2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Amount", amt2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Infinite", inf2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Slot", slot1);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(slot2 + "")) {

                double price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getDouble("Price");
                int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getInt("Amount");
                int stock = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getInt("Stock");

                String inf = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getString("Infinite");

                double price2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getDouble("Price");
                int amt2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getInt("Amount");
                int stock2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getInt("Stock");

                String inf2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getString("Infinite");


                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("ItemStack", item1);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Stock", stock);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Price", price);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Amount", amt);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Infinite", inf);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Slot", slot2);

                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("ItemStack", item2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Stock", stock2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Price", price2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Amount", amt2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Infinite", inf2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Slot", slot1);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param item    - the item to work with
     * @param newSlot - the new slot for the item
     * @param sell    - true if a selling shop, false if a buying shop
     */
    public void changePlaces(ItemStack item, int newSlot, boolean sell) {

        if (!sell) {

            if (!config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(newSlot + "")) {

                if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(getSlotForItem(item, sell) + "")) {
                    double price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
                    int stock = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    String inf = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").set(getSlotForItem(item, sell) + "", null);

                    config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(newSlot + "");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Stock", stock);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Price", price);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Amount", amt);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Infinite", inf);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Slot", newSlot);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (!config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(newSlot + "")) {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(getSlotForItem(item, sell) + "")) {

                    double price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
                    int stock = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    String inf = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite");

                    config.getConfigurationSection(name).getConfigurationSection("Sell").set(getSlotForItem(item, sell) + "", null);

                    config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(newSlot + "");

                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Stock", stock);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Price", price);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Amount", amt);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Infinite", inf);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Slot", newSlot);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param item - the item to delete
     * @param sell - true if a selling shop, false if a buying shop
     */
    public void deleteItem(ItemStack item, boolean sell) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();


        meta.setLore(getLore(item));
        item.setItemMeta(meta);

        int slot;

        if (!sell) {

            slot = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");

            config.getConfigurationSection(name).getConfigurationSection("Contents").set(slot + "", null);
        } else {
            slot = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");

            config.getConfigurationSection(name).getConfigurationSection("Sell").set(slot + "", null);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            for (int i = 18; i < 162; i++) {

                if (i > slot && i < 54) {

                    ItemStack it = getItemForSlot(i, sell);


                    if (it != null) {
                        changePlaces(it, i - 1, sell);
                    }
                }
                if (i > slot && i >= 72 && i < 108) {
                    ItemStack it = getItemForSlot(i, sell);

                    if (it != null) {

                        if (i == 72) {
                            changePlaces(it, i - 19, sell);
                        } else {
                            changePlaces(it, i - 1, sell);
                        }

                    }
                }


                if (i > slot && i >= 126 && i < 162) {
                    ItemStack it = getItemForSlot(i, sell);

                    if (it != null) {
                        if (i == 126) {
                            changePlaces(it, i - 19, sell);
                        } else {
                            changePlaces(it, i - 1, sell);
                        }
                    }
                }
            }
        }


    }

    /**
     * @param item   - the item to remove
     * @param amount - the amount to remove
     * @param sell   - true if a selling shop, false if a buying shop
     */
    public void removeItem(ItemStack item, int amount, boolean sell) {

        ItemMeta meta = item.getItemMeta();


        meta.setLore(getLore(item));
        item.setItemMeta(meta);

        if (alreadyBeingSold(item, sell)) {

            if (!sell) {

                if (!isInfinite(item, sell)) {

                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") > amount) {

                        setStock(item, getStock(item, sell) - amount, sell);

                    } else if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") == amount) {
                        deleteItem(item, sell);
                    }
                }
            } else {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") > amount) {

                    setStock(item, getStock(item, sell) - amount, sell);

                } else if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") == amount) {
                    deleteItem(item, sell);
                }
            }
        }
    }

    /**
     * @param sell - true if a selling shop, false if a buying shop
     * @return a Map of an Itemstack and it's slot number
     */
    public HashMap<ItemStack, Integer> getShopContents(boolean sell) {
        HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();

        if (!sell) {


            if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                config.getConfigurationSection(name).createSection("Contents");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {


                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getInt("Slot");
                    items.put(item, amt);

                }
            }

        } else {

            if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                config.getConfigurationSection(name).createSection("Sell");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {


                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getInt("Slot");
                    items.put(item, amt);
                }
            }
        }


        return items;
    }

    /**
     * @param item - the item to check
     * @param sell - true if a selling shop, false if a buying shop
     * @return the slot of the item
     */
    public Integer getSlotForItem(ItemStack item, boolean sell) {
        int slot = 18;
        if (item != null) {
            if (item.getType() != Material.SKULL_ITEM) {
                ItemMeta meta = item.getItemMeta();

                List<String> lore = getLore(item);

                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                List<String> lore = getLore(item);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }


            if (!sell) {
                for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {
                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).isItemStack("ItemStack")) {
                        ItemStack it = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getItemStack("ItemStack");

                        if (it != null) {
                            if (it.getType() != Material.SKULL_ITEM) {
                                ItemMeta meta = it.getItemMeta();

                                List<String> lore = getLore(it);

                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            } else {
                                SkullMeta meta = (SkullMeta) it.getItemMeta();
                                List<String> lore = getLore(it);
                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            }

                            int am = item.getAmount();
                            int at = it.getAmount();

                            item.setAmount(1);
                            it.setAmount(1);

                            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                                slot = Integer.parseInt(s);
                            }

                            item.setAmount(am);
                            it.setAmount(at);
                        }
                    }
                }


            } else {
                for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {
                    if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                        ItemStack it = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");

                        if (it != null) {

                            if (it.getType() != Material.SKULL_ITEM) {
                                ItemMeta meta = it.getItemMeta();

                                List<String> lore = getLore(it);

                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            } else {
                                SkullMeta meta = (SkullMeta) it.getItemMeta();
                                List<String> lore = getLore(it);
                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            }

                            int am = item.getAmount();
                            int at = it.getAmount();

                            item.setAmount(1);
                            it.setAmount(1);

                            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                                slot = Integer.parseInt(s);
                            }

                            item.setAmount(am);
                            it.setAmount(at);
                        }
                    }
                }
            }
        }
        return slot;
    }

    /**
     * @param slot - the slot of the item
     * @param sell - true if a selling shop, false if a buying shop
     * @return an Itemstack for the slot
     */
    public ItemStack getItemForSlot(int slot, boolean sell) {

        if (!sell) {

            if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(slot + "")) {

                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").isItemStack("ItemStack")) {
                    return config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").getItemStack("ItemStack");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(slot + "")) {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").isItemStack("ItemStack")) {
                    return config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").getItemStack("ItemStack");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * @param pl            - The Player
     * @param chestLocation - The Location of the Chest
     * @return true if owner, false if not
     */
    public static boolean isShopOwner(Player pl, Location chestLocation) {
        boolean owner = false;

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (chestLocation.getWorld().getName().equals(w.getName())) {
                                if (chestLocation.getX() == x && chestLocation.getY() == y && chestLocation.getZ() == z) {

                                    String n = s.substring(0, s.length() - 9);


                                    Player p = Bukkit.getPlayer(UUID.fromString(config.getString(n + ".Owner")));

                                    if (p == pl) {
                                        owner = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return owner;
    }

    public static boolean isShopOwner(Player pl, String name) {
        boolean owner = false;

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (isShopOwner(pl, new Location(w, x, y, z))) {

                                String n = s.substring(0, s.length() - 9);

                                Player p = Bukkit.getPlayer(UUID.fromString(config.getString(n + ".Owner")));

                                if (p == pl) {
                                    if (n.equals(name)) {
                                        owner = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return owner;
    }



    // Start of new Item System



    public List<ShopItem> getShopItems() {
        if (items != null) {
            return items;
        } else {
            return loadShopItems();
        }
    }

    public List<ShopItem> getShopItems(boolean sell) {
        if (items != null) {
            if (sell) {
                return sellItems;
            } else {
                return buyItems;
            }
        } else {
            return loadShopItems();
        }
    }

    public List<ShopItem> loadShopItems() {

        if (config.getConfigurationSection(name).isInt("NextShopId")) {
            config.getConfigurationSection(name).set("NextShopId", 1);
        }

        if (config.getConfigurationSection(name).isConfigurationSection("Items")) {
            config.getConfigurationSection(name).createSection("Items");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config.getConfigurationSection(name).isConfigurationSection("Contents")) {

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {

                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getInt("Slot");
                    ShopItem it = new ShopItem(this, item, config.getConfigurationSection(name).getInt("NextShopId"), getPageForSlot(amt), amt, false);
                    items.add(it);
                    buyItems.add(it);
                    config.getConfigurationSection(name).set("NextShopId", it.getShopItemID() + 1);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (config.getConfigurationSection(name).isConfigurationSection("Sell")) {

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {

                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getInt("Slot");
                    ShopItem it = new ShopItem(this, item, config.getConfigurationSection(name).getInt("NextShopId"), getPageForSlot(amt), amt, true);
                    items.add(it);
                    sellItems.add(it);
                    config.getConfigurationSection(name).set("NextShopId", it.getShopItemID() + 1);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (config.getConfigurationSection(name).getConfigurationSection("Items").getKeys(false).size() != items.size()) {
            saveItemsToFile();
        }

        //delete 'Sell' and 'Contents'
//        config.getConfigurationSection(name).set("Contents",null);
//        config.getConfigurationSection(name).set("Sell",null);
//
//        try {
//            config.save(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return items;
    }

    public void saveItemsToFile() {

        for (ShopItem item : items) {
            if (!config.getConfigurationSection(name).getConfigurationSection("Items").isConfigurationSection("" + item.getShopItemID())) {
                config.getConfigurationSection(name).getConfigurationSection("Items").createSection("" + item.getShopItemID());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Id", item.getShopItemID());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("ItemStack", item.getItem());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Page", getPageForSlot(item.getSlot()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Slot", item.getSlot());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Selling", item.isSelling());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", getStock(item.getItem(),item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", getAmount(item.getItem(),item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", getPrice(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", isInfinite(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("LiveEconomy", false);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("PriceChangePercent", 1);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("DoubleAmount", 200);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MinimumPrice", 0);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MaximumPrice", Config.getMaxPrice());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", getPrice(item.getItem(),item.isSelling()));
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    public int getPageForSlot(int amt) {
        if (amt >= 18 && amt < 54) {
            return 1;
        } else if (amt >= 72 && amt < 108) {
            return 2;
        } else {
            return 3;
        }
    }

    public String getPriceAsString(ItemStack item, boolean sell) {
        return (new BigDecimal(Double.toString(getPrice(item, sell)))).toPlainString();
    }

    public boolean alreadyInShop(ItemStack item, boolean sell){
        boolean exists = false;

        int a = item.getAmount();

        item.setAmount(1);

        if (item.getType() != Material.SKULL_ITEM) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = getLore(item);


            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = getLore(item);


            meta.setLore(lore);
            item.setItemMeta(meta);
        }


        for (ShopItem it : getShopItems(sell)) {

            if (item.equals(it.getItem()) || item.toString().equals(it.getItem().toString()) && item.getData().getData() == it.getData() && item.getDurability() == it.getDurability()) {
                exists = true;
                break;
            }

        }

        if (!exists) {
            for (ShopItem it : getShopItems(sell)) {

                if (!item.toString().equals(it.toString())) {
                    exists = false;
                }

                if (item.equals(it.getItem()) || item.toString().equals(it.getItem().toString())) {
                    if (it.getData() != item.getData().getData()) {
                        exists = false;

                    } else {

                        if (it.getDurability() != item.getDurability()) {
                            exists = false;

                        } else {
                            exists = true;
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

    public void createShopItem(ItemStack it, int slot, int page, boolean sell){
        if (alreadyInShop(it,sell)){
            ShopItem item = new ShopItem(this,it,getNextAvailableId(),page,slot,sell);
            items.add(item);

            config.getConfigurationSection(name).getConfigurationSection("Items").createSection("" + item.getShopItemID());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Id", item.getShopItemID());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("ItemStack", item.getItem());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Page", getPageForSlot(item.getSlot()));
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Slot", item.getSlot());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Selling", item.isSelling());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", getStock(item.getItem(),item.isSelling()));
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", getAmount(item.getItem(),item.isSelling()));
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", getPrice(item.getItem(), item.isSelling()));
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", isInfinite(item.getItem(), item.isSelling()));
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("LiveEconomy", false);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("PriceChangePercent", 1);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("DoubleAmount", 200);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MinimumPrice", 0);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MaximumPrice", Config.getMaxPrice());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", getPrice(item.getItem(),item.isSelling()));

            config.getConfigurationSection(name).set("NextShopId", item.getShopItemID() + 1);

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Add Stock
        }
    }

    public void deleteShopItem(ShopItem item){
        config.getConfigurationSection(name).getConfigurationSection("Items").set("" + item.getShopItemID(),null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        items.remove(item);
    }

    public int getNextAvailableId(){
        return config.getConfigurationSection(name).getInt("NextShopId");
    }
}