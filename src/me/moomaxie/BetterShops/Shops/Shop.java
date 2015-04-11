package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.History.History;
import me.moomaxie.BetterShops.History.Transaction;
import me.moomaxie.BetterShops.MySQL.DatabaseManager;
import me.moomaxie.BetterShops.ShopTypes.Holographic.HologramManager;
import me.moomaxie.BetterShops.ShopTypes.Holographic.ShopHologram;
import me.moomaxie.BetterShops.ShopTypes.NPC.NPCs;
import me.moomaxie.BetterShops.ShopTypes.NPC.ShopsNPC;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public YamlConfiguration config;
    private History history;

    private LinkedList<ShopItem> sellItems = new LinkedList<>();
    private LinkedList<ShopItem> buyItems = new LinkedList<>();
    private LinkedList<ShopItem> items = new LinkedList<>();

    public boolean transLoaded = false;


    /**
     * @param owner    - the owner of the shop
     * @param shopName - The name of the shop
     */

    @Deprecated
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

                                            loadShopItems();
                                            history = new History(this);
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

    public Shop(String name, YamlConfiguration config, File file) {
        this.config = config;
        this.name = name;
        this.file = file;

        String s = config.getConfigurationSection(name).getString("Location");

        String[] locs = s.split(" ");

        World w = Bukkit.getWorld(locs[0]);

        double x = Double.parseDouble(locs[1]);
        double y = Double.parseDouble(locs[2]);
        double z = Double.parseDouble(locs[3]);

        p = Bukkit.getOfflinePlayer(UUID.fromString(file.getName().substring(0, file.getName().length() - 4)));

        l = new Location(w, x, y, z);

        loadShopItems();
        history = new History(this);
        loadTransactions();
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

                                            loadShopItems();
                                            history = new History(this);
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

                                        loadShopItems();
                                        history = new History(this);
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

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `Description` = '" + desc + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
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
     * @return true if an NPC shop, false if not
     */
    public boolean isHoloShop() {

        try {
            if (config != null) {
                if (config.isString(name + ".Holo")) {
                    return config.getConfigurationSection(name).getString("Holo").equals("True");
                } else {
                    setHoloShop(false);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            setHoloShop(false);
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
    @Deprecated
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

        if (Config.useTransactions()) {
            if (config.getConfigurationSection(name).isConfigurationSection("Transactions")) {

                if (history != null) {
                    history.clearAllTransactions();
                }


                Object[] carl = config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false).toArray();

                for (int i = carl.length; i > 0; i--) {

                    if (carl.length - i < 36) {

                        String s = (String) carl[i - 1];


                        ItemStack item = null;
                        String it = "";
                        Date d = new Date(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getLong("Date"));
                        OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getString("Player")));
                        if (config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).isItemStack("Item")) {
                            item = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getItemStack("Item");
                        } else {
                            it = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getString("Item");
                        }
                        double price = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getDouble("Price");
                        int amount = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getInt("Amount");
                        boolean sell = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getBoolean("Selling");


                        if (item != null) {
                            ShopItem ite = ShopItem.fromItemStack(this, item, sell);
                            if (ite != null) {
                                history.addTransaction(p, d, ite, price, amount, sell, false);

                                if (ite.getDisplayName() != null)
                                    config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).set("Item", ite.getDisplayName());
                                else
                                    config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).set("Item", ite.getItem().getType().name());
                            } else {
                                if (item.getItemMeta().getDisplayName() != null) {
                                    config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).set("Item", item.getItemMeta().getDisplayName());
                                    history.addTransaction(p, d, item.getItemMeta().getDisplayName(), price, amount, sell, false);
                                } else {
                                    config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).set("Item", item.getType().name());
                                    history.addTransaction(p, d, item.getType().name(), price, amount, sell, false);
                                }
                            }
                        } else {
                            history.addTransaction(p, d, it, price, amount, sell, false);
                        }
                    } else {
                        String s = (String) carl[i - 1];

                        config.getConfigurationSection(name).getConfigurationSection("Transactions").set(s, null);
                    }
                }
            } else {
                clearTransactions();
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

    public void deleteFirstTransaction() {
        if (!config.getConfigurationSection(name).isConfigurationSection("Transactions")) {
            config.getConfigurationSection(name).createSection("Transactions");
        }

        Object[] carl = config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false).toArray();

        config.getConfigurationSection(name).getConfigurationSection("Transactions").set((String) carl[0], null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `Open` = '" + open + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
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

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `ServerShop` = '" + open + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
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

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `NPCShop` = '" + open + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param open - true if an NPC shop, false if not
     */
    public void setHoloShop(boolean open) {
        if (open) {
            config.set(name + ".Holo", "True");
        } else {
            config.set(name + ".Holo", "False");
        }

        try {
            config.save(file);

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `HoloShop` = '" + open + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
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

            if (Core.useSQL()) {
                Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `Notify` = '" + notify + "' WHERE `Shops`.`Name` ='" + name + "';");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param item - The item to set
     * @param amt  - the new price
     * @param sell - true if a selling shop, false if a buying shop
     */
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
     * @param item - item to check
     * @param sell - true if a selling shop, false if a buying shop
     * @return the price of the item as a Double
     */
    @Deprecated
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

        if (String.valueOf(price).contains(",")) {
            price = Double.parseDouble(String.valueOf(price).replaceFirst(",", ".").replaceAll(",", ""));
        }


        return Double.parseDouble(new DecimalFormat("#.00").format(price).replaceFirst(",", ".").replaceAll(",", ""));
    }

    /**
     * @param item - the item to check
     * @return the lore of the item as a String list
     */
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public boolean isInfinite(ItemStack item, boolean sell) {
        if (!sell) {
            if (alreadyBeingSold(item, sell)) {


                assert config != null;
                assert name != null;
                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").isString("Infinite")) {
                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite").equals("True")) {
                        return true;
                    } else {
                        return false;
                    }
                }

            } else {
                setInfinite(item, false, sell);
                return false;
            }
        }
        return false;

    }

    /**
     * @param item - the item to work with
     * @param sell - true if a selling shop, false if a buying shop
     * @return the amount the item sells for
     */
    @Deprecated
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
     * @param item2 - the second item
     */
    public void exchangeItems(ShopItem item1, ShopItem item2) {
        int sl1 = item1.getSlot();
        int page1 = item1.getPage();

        int sl2 = item2.getSlot();
        int page2 = item2.getPage();

        item1.setSlot(sl2);
        item1.setPage(page2);

        item2.setSlot(sl1);
        item2.setPage(page1);

    }

    /**
     * @param item    - the item to work with
     * @param newSlot - the new slot for the item
     */
    public void changePlaces(ShopItem item, int newSlot, int newPage) {
        item.setSlot(newSlot);
        item.setPage(newPage);
    }


    /**
     * @param sell - true if a selling shop, false if a buying shop
     * @return a Map of an Itemstack and it's slot number
     */
    @Deprecated
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
    @Deprecated
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
     * @param pl            - The Player
     * @param chestLocation - The Location of the Chest
     * @return true if owner, false if not
     */

    public static boolean isShopOwner(Player pl, Location chestLocation) {
        Shop shop = ShopLimits.fromLocation(chestLocation);

        boolean owner = false;

        if (shop != null) {
            if (shop.getOwner() != null) {
                if (shop.getOwner().getUniqueId().equals(pl.getUniqueId()) || shop.getOwner().getUniqueId() == pl.getUniqueId()) {
                    owner = true;
                }
            }
        }

        return owner;
    }

    public static boolean isShopOwner(Player pl, String name) {
        Shop shop = ShopLimits.fromString(name);

        boolean owner = false;

        if (shop != null) {
            if (shop.getOwner() != null) {
                if (shop.getOwner().getUniqueId().equals(pl.getUniqueId()) || shop.getOwner().getUniqueId() == pl.getUniqueId()) {
                    owner = true;
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

        if (!config.getConfigurationSection(name).isInt("NextShopId")) {
            config.getConfigurationSection(name).set("NextShopId", 0);
        }

        if (!config.getConfigurationSection(name).isConfigurationSection("Items")) {
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

                    createShopItem(item, amt, getPageForSlot(amt), false);
                }
            }
        }

        if (config.getConfigurationSection(name).isConfigurationSection("Sell")) {

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {

                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getInt("Slot");

                    createShopItem(item, amt, getPageForSlot(amt), true);

                }
            }
        }

        for (String s : config.getConfigurationSection(name).getConfigurationSection("Items").getKeys(false)) {
            ItemStack it = config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection(s).getItemStack("ItemStack");
            int id = config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection(s).getInt("Id");
            int page = config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection(s).getInt("Page");
            int slot = config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection(s).getInt("Slot");
            boolean sell = config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection(s).getBoolean("Selling");

            if (it != null) {
                ShopItem item = new ShopItem(this, it, id, page, slot, sell);
                items.add(item);

                if (sell) {
                    sellItems.add(item);
                } else {
                    buyItems.add(item);
                }
            }
        }


        if (config.getConfigurationSection(name).getConfigurationSection("Items").getKeys(false).size() != items.size()) {
            saveItemsToFile();
        }

        //delete 'Sell' and 'Contents'
        config.getConfigurationSection(name).set("Contents", null);
        config.getConfigurationSection(name).set("Sell", null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", getStock(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", getAmount(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", getPrice(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("OrigPrice", getPrice(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", isInfinite(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("LiveEconomy", false);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("PriceChangePercent", 1);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("DoubleAmount", 750);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MinimumPrice", 0);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MaximumPrice", Config.getMaxPrice());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", getPrice(item.getItem(), item.isSelling()));
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

    @Deprecated
    public int getPreciseSlot(int slot) {
        int s = slot;

        if (slot >= 72 && slot < 108) {
            s = slot - 54;
        }
        if (slot >= 126 && slot < 162) {
            s = slot - 108;
        }

        return s;
    }

    public String getPriceAsString(ItemStack item, boolean sell) {
        return (new BigDecimal(Double.toString(getPrice(item, sell)))).toPlainString();
    }

    public boolean alreadyInShop(ItemStack item, boolean sell) {
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

    public ShopItem createShopItem(ItemStack it, int slot, int page, boolean sell) {
        if (ShopItem.fromItemStack(this, it, sell) == null) {
            ShopItem item = new ShopItem(this, it, getNextAvailableId(), page, slot, sell);
            items.add(item);

            if (!sell) {
                buyItems.add(item);
            } else {
                sellItems.add(item);
            }

            config.getConfigurationSection(name).getConfigurationSection("Items").createSection("" + item.getShopItemID());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Id", item.getShopItemID());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("ItemStack", item.getItem());
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Page", page);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Slot", slot);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Selling", item.isSelling());
            if (alreadyBeingSold(it, sell)) {
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", getStock(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", getAmount(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", getPrice(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("OrigPrice", getPrice(item.getItem(), item.isSelling()));
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", isInfinite(item.getItem(), item.isSelling()));
            } else {
                if (!sell) {
                    config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", 1);
                } else {
                    config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Stock", 0);
                }
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Amount", 1);
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Price", Config.getDefaultPrice());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("OrigPrice", Config.getDefaultPrice());
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("Infinite", false);
            }
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("LiveEconomy", false);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("PriceChangePercent", 1);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("DoubleAmount", 750);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MinimumPrice", 0);
            config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("MaximumPrice", Config.getMaxPrice());
            if (alreadyBeingSold(it, sell)) {
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", getPrice(item.getItem(), item.isSelling()));
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Items").getConfigurationSection("" + item.getShopItemID()).set("AdjustedPrice", Config.getDefaultPrice());
            }
            config.getConfigurationSection(name).set("NextShopId", item.getShopItemID() + 1);

            try {
                config.save(file);

                if (Core.useSQL()) {
                    DatabaseManager.createTable(item);
                    if (Core.getSQLDatabase().getConnection().getMetaData().getTables(null, null, "Shops", null).next()) {
                        Core.getSQLDatabase().getConnection().createStatement().executeUpdate("UPDATE Shops SET `NextShopId` = '" + (item.getShopItemID() + 1) + "' WHERE `Shops`.`Name` ='" + name + "';");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return item;
        }
        return null;
    }

    public void deleteShopItem(ShopItem item) {
        config.getConfigurationSection(name).getConfigurationSection("Items").set("" + item.getShopItemID(), null);

        items.remove(item);
        if (item.isSelling()) {
            sellItems.remove(item);
        } else {
            buyItems.remove(item);
        }

        if (Core.useSQL()) {
            DatabaseManager.deleteItemTable(item);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getNextAvailableId() {
        return config.getConfigurationSection(name).getInt("NextShopId");
    }

    public boolean pageFull(int page, boolean sell) {
        return getNumberOfItemsOnPage(page, sell) == 36;
    }

    public int getNumberOfItemsOnPage(int page, boolean sell) {
        int i = 0;
        for (ShopItem item : getShopItems(sell)) {
            if (item.getPage() == page) {
                i++;
            }
        }
        return i;
    }

    public int getNextAvailablePage(boolean sell) {
        int page = 0;

        for (int i = 1; i < 1000; i++) {
            if (!pageFull(i, sell)) {
                page = i;
                break;
            }
        }

        return page;
    }

    public int getNextSlotForPage(int page, boolean sell) {
        List<Integer> slots = new ArrayList<>();
        for (ShopItem item : getShopItems(sell)) {
            if (item.getPage() == page) {
                slots.add(item.getSlot());
            }
        }

        if (slots.size() > 0) {
            for (int i = 18; i < 54; i++) {
                if (!slots.contains(i)) {
                    return i;
                }
            }
        }

        return 18;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShopHologram getHolographicShop() {
        return HologramManager.getShopHolograms().get(this);
    }

    public ShopsNPC getNPCShop() {
        return NPCs.getShopNPCs().get(this);
    }
}