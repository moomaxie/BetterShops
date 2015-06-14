package max.hubbard.bettershops.Shops;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Menus.ShopMenus.*;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Types.Holo.HologramManager;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Shops.Types.NPC.NPCManager;
import max.hubbard.bettershops.Shops.Types.NPC.ShopsNPC;
import max.hubbard.bettershops.TradeManager;
import max.hubbard.bettershops.Utils.SQLUtil;
import max.hubbard.bettershops.Utils.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SQLShop implements Shop {

    public Statement statement;
    private OfflinePlayer owner;
    private List<ShopItem> items = new ArrayList<>();
    private List<ShopItem> buy = new ArrayList<>();
    private List<ShopItem> sell = new ArrayList<>();
    private List<OfflinePlayer> blacklist = new ArrayList<>();
    private List<OfflinePlayer> keepers = new ArrayList<>();
    private Location l;
    private HashMap<MenuType, ShopMenu> menus = new HashMap<>();
    private boolean transLoaded = false;
    private History history = new History(this);
    private String name;

    public SQLShop(String name, ResultSet rs) throws SQLException {

        try {
            this.statement = Core.getConnection().createStatement();
        } catch (SQLException ignored) {

        }

        this.name = name;
        this.owner = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("Owner")));
        l = new Location(Bukkit.getWorld(rs.getString("World")), rs.getInt("X"), rs.getInt("Y"), rs.getInt("Z"));
        history = new History(this);
        loadItems();
        loadMenus();
        loadTransactions();
        loadKeepers();
        loadBlacklist();
        TradeManager.loadTrades(this);
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public Object getObject(final String s) {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM Shops WHERE Name = '" + name + "';");
            if (set.next()) {
                return set.getObject(s);
            } else {
                return null;
            }
        } catch (SQLException e) {

            return null;
        }

    }

    public void setObject(String path, Object obj) {

        if (obj instanceof Boolean) {
            obj = SQLUtil.getBoolValue((boolean) obj);
        }

        try {
            statement.executeUpdate("UPDATE Shops SET `" + path + "` = '" + obj + "' WHERE Name = '" + name + "';");
        } catch (SQLException ignored) {

        }
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (ShopManager.fromString(name) == null) {
            setObject("Name", name);
            try {
                statement.executeUpdate("UPDATE Trades SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE Keepers SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE Blacklist SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE Items SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
            } catch (Exception ignored) {

            }
            this.name = name;
            loadMenus();
            return true;
        } else {
            return false;
        }
    }

    public boolean setOwner(OfflinePlayer owner) {
        if (!ShopManager.atLimit(owner)) {
            List<Shop> s = ShopManager.playerShops.get(getOwner().getUniqueId());
            s.remove(this);
            List<Shop> s1 = new ArrayList<>();
            if (ShopManager.playerShops.containsKey(owner.getUniqueId())) {
                s1 = ShopManager.playerShops.get(owner.getUniqueId());
            }
            s1.add(this);
            ShopManager.playerShops.put(getOwner().getUniqueId(), s);
            ShopManager.playerShops.put(owner.getUniqueId(), s1);

            setObject("Owner", owner.getUniqueId().toString());
            this.owner = owner;
            return true;
        } else {
            return false;
        }
    }

    public void loadItems() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM Items WHERE Shop = '" + name + "';");

            while (set.next()) {

                ShopItem item = SQLShopItem.loadShopItem(this, set.getInt("Id"), set);
                items.add(item);
                if (!item.isSelling()) {
                    buy.add(item);
                } else {
                    sell.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void loadMenus() {
        menus.clear();
        menus.put(MenuType.OWNER_BUYING, new OwnerBuying(this));
        menus.put(MenuType.OWNER_SELLING, new OwnerSelling(this));
        menus.put(MenuType.MAIN_BUYING, new MainBuying(this));
        menus.put(MenuType.MAIN_SELLING, new MainSelling(this));
        menus.put(MenuType.KEEPER_MANAGER, new KeeperManager(this));
        menus.put(MenuType.SHOP_SETTINGS, new ShopSettings(this));
        menus.put(MenuType.HISTORY, new max.hubbard.bettershops.Menus.ShopMenus.History(this));
        menus.put(MenuType.ITEM_MANAGER_BUYING, new ItemManagerBuying(this));
        menus.put(MenuType.ITEM_MANAGER_SELLING, new ItemManagerSelling(this));
        menus.put(MenuType.LIVE_ECONOMY, new LiveEconomy(this));
        menus.put(MenuType.KEEPER_ITEM_MANAGER, new KeeperItemManager(this));
        menus.put(MenuType.BUY_ITEM, new BuyItem(this));
        menus.put(MenuType.SELL_ITEM, new SellItem(this));
        menus.put(MenuType.NPC_CHOOSE, new NPCChoose(this));
        menus.put(MenuType.NPC_CONFIGURE, new NPCConfigure(this));
        menus.put(MenuType.REARRANGE, new Rearrange(this));
        menus.put(MenuType.AMOUNT_CHOOSER, new AmountChooser(this));
        menus.put(MenuType.CART, new Cart(this));
        menus.put(MenuType.SEARCH_ENGINE, new SearchEngine(this));
        menus.put(MenuType.TRADING, new Trading(this));
        menus.put(MenuType.TRADE_MANAGER, new max.hubbard.bettershops.Menus.ShopMenus.TradeManager(this));
        menus.put(MenuType.TRADE_CONFIRM, new TradeConfirm(this));
        menus.put(MenuType.TRADE_CHOOSE, new TradeChoose(this));
        menus.put(MenuType.PLAYER_BLACKLIST, new PlayerBlacklist(this));
    }

    public ShopMenu getMenu(MenuType type) {
        return menus.get(type);
    }

    public void clearTransactions() {

    }

    public void loadTransactions() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM Transactions WHERE Shop = '" + getName() + "';");

            while (set.next()) {
                OfflinePlayer buy;
                if (set.getString("Player").contains("-")) {
                    buy = Bukkit.getOfflinePlayer(UUID.fromString(set.getString("Player")));
                } else {
                    buy = Bukkit.getOfflinePlayer(set.getString("Player"));
                }
                Date date = new Date(set.getString("Date"));
                double price = set.getDouble("Price");
                int amt = set.getInt("Amount");
                boolean sell = set.getBoolean("Selling");
                String item = set.getString("Item");

                history.addTransaction(buy, date, item, price, amt, sell, false);
            }

        } catch (SQLException ignored) {

        }
        transLoaded = true;
    }

    public void deleteShopItem(ShopItem item) {
        try {
            statement.executeUpdate("DELETE FROM Items WHERE Shop = '" + getName() + "' AND Id = '" + item.getId() + "';");
            items.remove(item);

            if (item.isSelling()) {
                sell.remove(item);
            } else {
                buy.remove(item);
            }
        } catch (SQLException ignored) {

        }

    }

    public byte getFrameColor() {
        if (getObject("Frame") != null) {
            return (byte) (int) getObject("Frame");
        } else {
            setObject("Frame", 7);
            return 7;
        }
    }

    public void deleteFirstTransaction() {


    }

    /**
     * @param t    - the Transaction to save
     * @param save - a boolean whether to save to the file or not
     */
    public void saveTransaction(Transaction t, boolean save) {

        try {
            statement.executeUpdate("INSERT INTO Transactions (Shop, Id, Item, Player, Owner, Price, Amount, Selling, Date) VALUES" +
                    " ('" + getName() + "', " + t.getItem() + "', " + t.getPlayer().getName() + "', " + owner.getName() + "', " + t.getPrice() + "', " + t.getAmount() + "', '"
                    + t.isSell() + "', " + t.getDate().toLocaleString() + "');");
        } catch (SQLException ignored) {

        }
    }

    public List<ShopItem> getShopItems() {
        return items;
    }

    public List<ShopItem> getShopItems(boolean sell) {

        if (sell) {
            return this.sell;
        } else {
            return buy;
        }
    }

    public void loadKeepers() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM Keepers WHERE Shop = '" + getName() + "';");

            while (set.next()) {
                keepers.add(Bukkit.getOfflinePlayer(UUID.fromString(set.getString("Players"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addKeeper(OfflinePlayer p) {
        keepers.add(p);
        try {
            statement.executeUpdate("INSERT INTO keepers (Shop, Players) VALUES ('" + getName() + "', '" + p.getUniqueId().toString() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeKeeper(OfflinePlayer p) {
        keepers.remove(p);
        try {
            statement.executeUpdate("DELETE FROM keepers WHERE Shop = '" + getName() + "' AND Players = '" + p.getUniqueId().toString() + "';");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OfflinePlayer> getKeepers() {
        return keepers;
    }

    public void loadBlacklist() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM Blacklist WHERE Shop = '" + getName() + "';");

            while (set.next()) {
                blacklist.add(Bukkit.getOfflinePlayer(UUID.fromString(set.getString("Players"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBlacklist(OfflinePlayer p) {
        blacklist.add(p);
        try {
            statement.executeUpdate("INSERT INTO Blacklist VALUES ('" + getName() + "', '" + p.getUniqueId().toString() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBlacklist(OfflinePlayer p) {
        blacklist.remove(p);
        try {
            statement.executeUpdate("DELETE FROM Blacklist WHERE Shop = '" + getName() + "' AND Players = '" + p.getUniqueId().toString() + "';");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OfflinePlayer> getBlacklist() {
        return blacklist;
    }

    public Location getLocation() {
        return l;
    }

    public void setOpen(boolean b) {
        setObject("Open", b);
        Chest chest = (Chest) l.getWorld().getBlockAt(l).getState();

        Block block = chest.getBlock();

        Sign sign = null;
        if (block.getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
            sign = (Sign) block.getRelative(1, 0, 0).getState();
        } else if (block.getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
            sign = (Sign) block.getRelative(-1, 0, 0).getState();
        } else if (block.getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
            sign = (Sign) block.getRelative(0, 0, 1).getState();
        } else if (block.getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
            sign = (Sign) block.getRelative(0, 0, -1).getState();
        }

        if (sign != null) {
            ShopManager.signLocs.put(sign.getLocation(), this);
            if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                    if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                        if (b) {
                            sign.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                        } else {
                            sign.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                        }
                        sign.update();
                    }
                }
            }
        }
    }

    public History getHistory() {
        return history;
    }

    public ShopHologram getHolographicShop() {
        return HologramManager.getShopHologram(this);
    }

    public ShopsNPC getNPCShop() {
        return NPCManager.getNPCShop(this);
    }

    public boolean isOpen() {
        return getObject("Open") != null && (boolean) getObject("Open");
    }

    public boolean isNPCShop() {
        return getObject("NPC") != null && (boolean) getObject("NPC");
    }

    public boolean isHoloShop() {
        return getObject("Holo") != null && (boolean) getObject("Holo");
    }

    public boolean isServerShop() {
        return getObject("Server") != null && (boolean) getObject("Server");
    }

    public boolean isNotify() {
        return getObject("Notify") != null && (boolean) getObject("Notify");
    }

    public ShopItem createShopItem(ItemStack it, int slot, int page, boolean sell) {
        if (SQLShopItem.fromItemStack(this, it, sell) == null) {
            ShopItem item = SQLShopItem.createShopItem(this, it, getNextAvailableId(), page, slot, sell);
            items.add(item);
            setObject("NextShopId", getNextAvailableId() + 1);
            if (sell) {
                this.sell.add(item);
            } else {
                buy.add(item);
            }
            return item;
        }
        return null;
    }

    public int getNextAvailableId() {
        return (int) getObject("NextShopId");
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

    }
}