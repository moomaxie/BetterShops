package max.hubbard.bettershops.Shops;

import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Menus.ShopMenus.*;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Items.SQLShopItem;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Types.Holo.CreateHologram;
import max.hubbard.bettershops.Shops.Types.Holo.HologramManager;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Shops.Types.NPC.*;
import max.hubbard.bettershops.TradeManager;
import max.hubbard.bettershops.Utils.NPCInfo;
import max.hubbard.bettershops.Utils.SQLUtil;
import max.hubbard.bettershops.Utils.TimingsManager;
import max.hubbard.bettershops.Utils.Transaction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
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
    public HashMap<UUID, ShopItem> arrange = new HashMap<>();

    public SQLShop(String name) throws SQLException {

        this.statement = Core.getConnection().createStatement();


        final ResultSet rs = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "'");

        if (rs.next()) {
            this.name = name;
            this.owner = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("Owner")));
            l = new Location(Bukkit.getWorld(rs.getString("World")), rs.getInt("X"), rs.getInt("Y"), rs.getInt("Z"));
            history = new History(this);

            final SQLShop t = this;

            Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                @Override
                public void run() {

                    loadItems();
                    loadMenus();
                    loadKeepers();
                    loadBlacklist();
                    TradeManager.loadTrades(t);
                    loadTransactions();

                    if (isNPCShop()) {
                        boolean made = false;
                        if (Core.useCitizens()) {
                            for (NPC n : CitizensAPI.getNPCRegistry().sorted()) {
                                if (n.getEntity() instanceof LivingEntity) {
                                    if (n.getName().equals("§a§l" + t.name)) {
                                        if (getNPCShop() == null) {
                                            CitizensShop s = new CitizensShop(n, (LivingEntity) n.getEntity(), t);
                                            NPCManager.addNPCShop(s);
                                            s.removeChest();
                                            setObject("NPC", true);
                                            if (getObject("NPCInfo") != null) {
                                                EntityInfo in = EntityInfo.fromString((String) getObject("NPCInfo"));
                                                s.setInfo(in);
                                            }
                                            made = true;
                                        } else {
                                            CitizensAPI.getNPCRegistry().deregister(n);
                                            n.destroy();
                                        }
                                    }
                                }
                            }
                        }

                        if (!made) {
                            if (l != null && l.getWorld() != null)
                                for (LivingEntity entity : l.getWorld().getLivingEntities()) {
                                    if (entity.getCustomName() != null) {
                                        if (entity.getCustomName().equals("§a§l" + t.name)) {
                                            if (getNPCShop() == null) {
                                                try {

                                                    if (Core.useCitizens()) {
                                                        if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                                                            CitizensShop s = new CitizensShop(CitizensAPI.getNPCRegistry().getNPC(entity), entity, t);
                                                            NPCManager.addNPCShop(s);
                                                            s.removeChest();
                                                            setObject("NPC", true);
                                                            if (getObject("NPCInfo") != null) {
                                                                EntityInfo in = EntityInfo.fromString((String) getObject("NPCInfo"));
                                                                s.setInfo(in);
                                                            }
                                                        } else {
                                                            CitizensShop s = new CitizensShop(EntityInfo.getInfo(entity), t);
                                                            s.spawn();
                                                            NPCManager.addNPCShop(s);
                                                            s.removeChest();
                                                            setObject("NPC", true);
                                                        }

                                                    } else {
                                                        ShopsNPC s = NPCInfo.createNewShopsNPC(entity, t);
                                                        NPCManager.addNPCShop(s);
                                                        s.removeChest();
                                                        setObject("NPC", true);
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    made = true;
                                                }
                                            } else {
                                                if (Core.useCitizens()) {
                                                    if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
                                                        net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                                                        npc.destroy();
                                                        CitizensAPI.getNPCRegistry().deregister(npc);
                                                    } else {
                                                        entity.remove();
                                                    }
                                                } else {
                                                    entity.remove();
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        if (!made) {

                            if (getObject("NPCInfo") != null) {
                                if (Core.useCitizens()) {

                                    CitizensShop s = new CitizensShop(EntityInfo.fromString((String) getObject("NPCInfo")), t);
                                    s.spawn();
                                    NPCManager.addNPCShop(s);
                                    s.removeChest();
                                    setObject("NPC", true);

                                } else {
                                    ShopsNPC s = new NPCShop(EntityInfo.fromString((String) getObject("NPCInfo")), t);
                                    NPCManager.addNPCShop(s);
                                    s.removeChest();
                                    setObject("NPC", true);
                                }
                            } else {

                                setObject("NPC", false);
                                DeleteNPC.addChest(t);
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {
                            if (!isHoloShop() && !isNPCShop()) {
                                if (l != null && l.getWorld() != null) {
                                    if (l.getBlock() != null && l.getBlock().getType() != Material.CHEST && l.getBlock().getType() != Material.TRAPPED_CHEST) {
                                        DeleteNPC.addChest(t);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
        new TimingsManager(this).startTime();
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public Object getObject(final String s) {


        try {
            if (statement == null || statement.isClosed()) {
                statement = Core.getConnection().createStatement();
            }
            ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
            if (set.next()) {
                return set.getObject(s);
            } else {
                return null;
            }
        } catch (Exception e) {

            return null;
        }

    }

    public void setObject(String path, Object obj) {

        if (obj instanceof Boolean) {
            obj = SQLUtil.getBoolValue((boolean) obj);
        }

        try {
            statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Shops SET `" + path + "` = '" + obj + "' WHERE Name = '" + name + "';");
        } catch (SQLException ignored) {

        }
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (ShopManager.fromString(name) == null) {
            if (isNPCShop()) {
                if (Core.useCitizens()) {
                    ((CitizensShop) getNPCShop()).getNPC().setName("§a§l" + name);
                } else {
                    ((NPCShop) getNPCShop()).entity.setCustomName("§a§l" + name);
                }

            }
            if (isHoloShop()) {
                getHolographicShop().getNameLine().setText("§a§l" + name);
            }

            setObject("Name", name);

            try {
                statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Trades SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Keepers SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Blacklist SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
                statement.executeUpdate("UPDATE " + Config.getObject("prefix") + "Items SET Shop = '" + name + "' WHERE Shop = '" + this.name + "';");
            } catch (Exception ignored) {

            }


            ShopManager.names.remove(this.name);
            ShopManager.names.put(name, this);
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
            ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Items WHERE Shop = '" + name + "';");

            List<Integer> ids = new ArrayList<>();

            while (!set.isClosed() && set.next()) {
                ids.add(set.getInt("Id"));
            }
            for (int id : ids) {
                ShopItem item = SQLShopItem.loadShopItem(this, id);
                items.add(item);
                if (!item.isSelling()) {
                    buy.add(item);
                } else {
                    sell.add(item);
                }
            }

        } catch (Exception ignored) {

        } finally {
            if (isHoloShop()) {
                String s = "BS" + getName();
                try {
                    NamedHologram holo = HologramDatabase.loadHologram(s);
//                            if (holo.getLine(0) instanceof TextLine && shop.isHoloShop()) {
//                                if (((TextLine) holo.getLine(0)).getText().equals("§a§l" + shop.getName())) {
                    if (getHolographicShop() == null) {
                        NamedHologramManager.removeHologram(holo);
                        holo.delete();
                        HologramDatabase.deleteHologram(s);
                        HologramDatabase.saveToDisk();

                    } else {
                        holo.delete();
                    }
//                                }
//                            }
                } catch (Exception e) {
                    HologramDatabase.deleteHologram(s);
                    HologramDatabase.trySaveToDisk();

                } finally {
                    CreateHologram.createHolographicShop(this);
                }
            }
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
        menus.put(MenuType.AUTO_STOCK, new AutoStock(this));
        menus.put(MenuType.COOLDOWNS, new Cooldowns(this));
    }

    public ShopMenu getMenu(MenuType type) {
        switch (type) {
            case OWNER_BUYING:
                return new OwnerBuying(this);
            case OWNER_SELLING:
                return new OwnerSelling(this);
            case MAIN_BUYING:
                return new MainBuying(this);
            case MAIN_SELLING:
                return new MainSelling(this);
            case KEEPER_MANAGER:
                return new KeeperManager(this);
            case SHOP_SETTINGS:
                return new ShopSettings(this);
            case HISTORY:
                return new max.hubbard.bettershops.Menus.ShopMenus.History(this);
            case ITEM_MANAGER_BUYING:
                return new ItemManagerBuying(this);
            case ITEM_MANAGER_SELLING:
                return new ItemManagerSelling(this);
            case LIVE_ECONOMY:
                return new LiveEconomy(this);
            case KEEPER_ITEM_MANAGER:
                return new KeeperItemManager(this);
            case BUY_ITEM:
                return new BuyItem(this);
            case SELL_ITEM:
                return new SellItem(this);
            case NPC_CHOOSE:
                return new NPCChoose(this);
            case NPC_CONFIGURE:
                return new NPCConfigure(this);
            case REARRANGE:
                return new Rearrange(this);
            case AMOUNT_CHOOSER:
                return new AmountChooser(this);
            case CART:
                return new Cart(this);
            case SEARCH_ENGINE:
                return new SearchEngine(this);
            case TRADING:
                return new Trading(this);
            case TRADE_MANAGER:
                return new max.hubbard.bettershops.Menus.ShopMenus.TradeManager(this);
            case TRADE_CONFIRM:
                return new TradeConfirm(this);
            case TRADE_CHOOSE:
                return new TradeChoose(this);
            case PLAYER_BLACKLIST:
                return new PlayerBlacklist(this);
            case AUTO_STOCK:
                return new AutoStock(this);
            case COOLDOWNS:
                return new Cooldowns(this);
        }
        return menus.get(type);
    }

    public void clearTransactions() {

    }

    public void loadTransactions() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Transactions WHERE Shop = '" + getName() + "';");

            while (set.next()) {
                String p = set.getString("Player");
                OfflinePlayer buy = null;
                if (p.contains("-")) {
                    buy = Bukkit.getOfflinePlayer(UUID.fromString(p));
                }
                Date date = new Date(set.getString("Date"));
                double price = set.getDouble("Price");
                int amt = set.getInt("Amount");
                boolean sell = set.getBoolean("Selling");
                String item = set.getString("Item");

                if (buy != null) {
                    history.addTransaction(buy, date, item, price, amt, sell, false);
                } else {
                    history.addTransaction(p, date, item, price, amt, sell, false);
                }
            }

        } catch (SQLException e) {
//            e.printStackTrace();
        }
        transLoaded = true;
    }

    public void deleteShopItem(ShopItem item) {
        try {
            statement.executeUpdate("DELETE FROM " + Config.getObject("prefix") + "Items WHERE Shop = '" + getName() + "' AND Id = '" + item.getId() + "';");
            items.remove(item);

            if (item.isSelling()) {
                sell.remove(item);
            } else {
                buy.remove(item);
            }
        } catch (SQLException ignored) {

        }

    }

    @Override
    public HashMap<UUID, ShopItem> getArrange() {
        return arrange;
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

        String item = t.getItem();
        String player;
        if (t.getPlayer() != null) {
            player = t.getPlayer().getUniqueId().toString();
        } else {
            player = t.getPlayerName();
        }
        String owner = getOwner().getUniqueId().toString();
        String date = t.getDate().toLocaleString();
        boolean sell = t.isSell();
        double price = t.getPrice();
        int amt = t.getAmount();

        try {
            statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Transactions (`Shop`, `Item`, `Player`, `Owner`, `Price`, `Amount`, `Selling`, `Date`) VALUES" +
                    " ('" + getName() + "', '" + item + "', '" + player + "', '" + owner + "', '" + price + "', '" + amt + "', "
                    + sell + ", '" + date + "'" +
                    ");");
        } catch (Exception ignored) {

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
            ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Keepers WHERE Shop = '" + getName() + "';");

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
            statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Keepers (Shop, Players) VALUES ('" + getName() + "', '" + p.getUniqueId().toString() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeKeeper(OfflinePlayer p) {
        keepers.remove(p);
        try {
            statement.executeUpdate("DELETE FROM " + Config.getObject("prefix") + "Keepers WHERE Shop = '" + getName() + "' AND Players = '" + p.getUniqueId().toString() + "';");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OfflinePlayer> getKeepers() {
        return keepers;
    }

    public void loadBlacklist() {

        try {
            ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Blacklist WHERE Shop = '" + getName() + "';");

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
            statement.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Blacklist VALUES ('" + getName() + "', '" + p.getUniqueId().toString() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBlacklist(OfflinePlayer p) {
        blacklist.remove(p);
        try {
            statement.executeUpdate("DELETE FROM " + Config.getObject("prefix") + "Blacklist WHERE Shop = '" + getName() + "' AND Players = '" + p.getUniqueId().toString() + "';");

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

        if (l.getWorld().getBlockAt(l).getState() instanceof Chest) {

            Chest chest = (Chest) l.getWorld().getBlockAt(l).getState();

            Block block = chest.getBlock();

            Sign sign = null;
            if (block.getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(1, 0, 0).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            if (b) {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                            } else {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                            }
                            sign.update();
                            ShopManager.signLocs.put(sign.getLocation(), this);
                            return;
                        }
                    }
                }

            }
            if (block.getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(-1, 0, 0).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            if (b) {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                            } else {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                            }
                            sign.update();
                            ShopManager.signLocs.put(sign.getLocation(), this);
                            return;
                        }
                    }
                }
            }
            if (block.getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(0, 0, 1).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            if (b) {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                            } else {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                            }
                            sign.update();
                            ShopManager.signLocs.put(sign.getLocation(), this);
                        }
                    }
                }
            }
            if (block.getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(0, 0, -1).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            if (b) {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                            } else {
                                sign.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                            }
                            sign.update();
                            ShopManager.signLocs.put(sign.getLocation(), this);
                        }
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
        try {
            return getObject("Open") != null && (boolean) getObject("Open");
        } catch (Exception e) {
            try {
                if (statement == null || statement.isClosed()) {
                    statement = Core.getConnection().createStatement();
                }
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
                return set.next() && (boolean) set.getObject("Open");
            } catch (Exception e1) {

                return false;
            }
        }
    }

    public boolean isNPCShop() {
        try {
            return getObject("NPC") != null && (boolean) getObject("NPC");
        } catch (Exception e) {
            try {
                if (statement == null || statement.isClosed()) {
                    statement = Core.getConnection().createStatement();
                }
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
                return set.next() && (boolean) set.getObject("NPC");
            } catch (Exception e1) {

                return false;
            }
        }
    }

    public boolean isHoloShop() {
        try {
            return getObject("Holo") != null && (boolean) getObject("Holo");
        } catch (Exception e) {
            try {
                if (statement == null || statement.isClosed()) {
                    statement = Core.getConnection().createStatement();
                }
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
                return set.next() && (boolean) set.getObject("Holo");
            } catch (Exception e1) {

                return false;
            }
        }
    }

    public boolean isServerShop() {
        try {
            return getObject("Server") != null && (boolean) getObject("Server");
        } catch (Exception e) {
            try {
                if (statement == null || statement.isClosed()) {
                    statement = Core.getConnection().createStatement();
                }
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
                return set.next() && (boolean) set.getObject("Server");
            } catch (Exception e1) {

                return false;
            }
        }
    }

    public boolean isNotify() {
        try {
            return getObject("Notify") != null && (boolean) getObject("Notify");
        } catch (Exception e) {
            try {
                if (statement == null || statement.isClosed()) {
                    statement = Core.getConnection().createStatement();
                }
                ResultSet set = statement.executeQuery("SELECT * FROM " + Config.getObject("prefix") + "Shops WHERE Name = '" + name + "';");
                return set.next() && (boolean) set.getObject("Notify");
            } catch (Exception e1) {

                return false;
            }
        }
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

    @Override
    public void convert() {

    }

    public void saveConfig() {
    }

    public void syncSaveConfig() {
    }

    public Sign getSign() {
        if (l.getWorld().getBlockAt(l).getState() instanceof Chest) {

            Chest chest = (Chest) l.getWorld().getBlockAt(l).getState();

            Block block = chest.getBlock();

            Sign sign = null;
            if (block.getRelative(1, 0, 0).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(1, 0, 0).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            return sign;
                        }
                    }
                }

            }
            if (block.getRelative(-1, 0, 0).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(-1, 0, 0).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {
                            return sign;
                        }
                    }
                }
            }
            if (block.getRelative(0, 0, 1).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(0, 0, 1).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {

                            return sign;
                        }
                    }
                }
            }
            if (block.getRelative(0, 0, -1).getType() == Material.WALL_SIGN) {
                sign = (Sign) block.getRelative(0, 0, -1).getState();

                if (sign.getLine(0).contains(Language.getString("MainGUI", "SignLine1"))) {
                    if (sign.getLine(3).contains(Language.getString("MainGUI", "SignLine4"))) {
                        if (sign.getLine(1).contains(Language.getString("MainGUI", "SignLine2"))) {

                            return sign;
                        }
                    }
                }
            }
        }
        return null;
    }
}