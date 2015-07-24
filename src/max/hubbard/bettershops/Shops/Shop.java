package max.hubbard.bettershops.Shops;

import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Shops.Types.NPC.ShopsNPC;
import max.hubbard.bettershops.Utils.Transaction;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
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
public interface Shop {

    OfflinePlayer owner = null;
    List<ShopItem> items = new ArrayList<ShopItem>();
    List<ShopItem> sell = new ArrayList<ShopItem>();
    List<ShopItem> buy = new ArrayList<ShopItem>();
    List<OfflinePlayer> blacklist = new ArrayList<OfflinePlayer>();
    List<OfflinePlayer> keepers = new ArrayList<OfflinePlayer>();
    Location l = null;
    HashMap<MenuType, ShopMenu> menus = new HashMap<>();
    boolean transLoaded = false;
    History history = null;


    public OfflinePlayer getOwner();

    public Object getObject(String s);

    public void setObject(String path, Object obj);

    public String getName();

    public boolean setName(String name);

    public boolean setOwner(OfflinePlayer owner);

    public void loadItems();

    public void loadMenus();

    public ShopMenu getMenu(MenuType type);

    public void clearTransactions();

    public void loadTransactions();

    public void deleteShopItem(ShopItem item);

    public void deleteFirstTransaction();

    /**
     * @param t    - the Transaction to save
     * @param save - a boolean whether to save to the file or not
     */
    public void saveTransaction(Transaction t, boolean save);

    public List<ShopItem> getShopItems();

    public List<ShopItem> getShopItems(boolean sell);

    public void loadKeepers();

    public void addKeeper(OfflinePlayer p);

    public void removeKeeper(OfflinePlayer p) ;

    public List<OfflinePlayer> getKeepers();

    public void loadBlacklist();

    public void addBlacklist(OfflinePlayer p);

    public void removeBlacklist(OfflinePlayer p);

    public List<OfflinePlayer> getBlacklist();

    public Location getLocation();

    public void setOpen(boolean b);

    public History getHistory();

    public ShopHologram getHolographicShop();

    public ShopsNPC getNPCShop();

    public boolean isOpen();

    public boolean isNPCShop();

    public boolean isHoloShop();

    public boolean isServerShop();

    public boolean isNotify();

    public byte getFrameColor();

    public ShopItem createShopItem(ItemStack it, int slot, int page, boolean sell);

    public int getNextAvailableId();

    public boolean pageFull(int page, boolean sell);

    public int getNumberOfItemsOnPage(int page, boolean sell);

    public int getNextAvailablePage(boolean sell);

    public int getNextSlotForPage(int page, boolean sell);

    public Sign getSign();

    public void convert();

    public void saveConfig();
}
