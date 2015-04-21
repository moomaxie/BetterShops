package me.moomaxie.BetterShops.Listeners.Misc;

import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopKeeperManager;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopSettings;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.SearchEngine.DisplayNameCheck;
import me.moomaxie.BetterShops.Listeners.SearchEngine.IdCheck;
import me.moomaxie.BetterShops.Listeners.SearchEngine.MaterialCheck;
import me.moomaxie.BetterShops.Listeners.SearchEngine.PriceCheck;
import me.moomaxie.BetterShops.ShopTypes.Holographic.ShopHologram;
import me.moomaxie.BetterShops.Shops.AddShop;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import me.moomaxie.BetterShops.SupplyandDemand.LiveEco;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ChatMessages implements Listener {

    public static Map<Player, Chest> shopCreate = new HashMap<>();
    public static Map<Player, Block> shopCreate2 = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> collectStock = new HashMap<>();
    public static Map<Player, Map<Shop, ShopItem>> addStock = new HashMap<>();
    public static Map<Player, Map<Shop, ShopItem>> removeStock = new HashMap<>();

    public static Map<Player, Shop> description = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> setSellAmount = new HashMap<>();
    public static Map<Player, Map<Shop, ShopItem>> setBuyAmount = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> setDoubleAmount = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> setSellPrice = new HashMap<>();
    public static Map<Player, Map<Shop, ShopItem>> setBuyPrice = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> setLimit = new HashMap<>();

    public static Map<Player, Map<Shop, ShopItem>> changeData = new HashMap<>();

    public static Map<Player, Shop> addKeeper = new HashMap<>();
    public static Map<Player, Shop> removeKeeper = new HashMap<>();

    public static Map<Player, Map<Shop, Inventory>> addSellItem = new HashMap<>();

    public static Map<Player, Map<Shop, Boolean>> searchMaterial = new HashMap<>();
    public static Map<Player, Map<Shop, Boolean>> searchName = new HashMap<>();
    public static Map<Player, Map<Shop, Boolean>> searchID = new HashMap<>();
    public static Map<Player, Map<Shop, Boolean>> searchPrice = new HashMap<>();

    @EventHandler
    public void onStopChat(AsyncPlayerChatEvent e) {

        try {
            for (Player p : e.getRecipients()) {
                if (!shopCreate.containsKey(p) && !collectStock.containsKey(p) && !addStock.containsKey(p) && !description.containsKey(p)
                        && !setSellAmount.containsKey(p) && !setBuyAmount.containsKey(p) && !setSellPrice.containsKey(p) && !setBuyPrice.containsKey(p)
                        && !searchID.containsKey(p) && !searchMaterial.containsKey(p) && !searchName.containsKey(p) && !searchPrice.containsKey(p) && !changeData.containsKey(p)
                        && !addKeeper.containsKey(p) && !removeKeeper.containsKey(p) && !addSellItem.containsKey(p) && !setDoubleAmount.containsKey(p) && !setLimit.containsKey(p)) {

                } else {
                    e.getRecipients().remove(p);
                }
            }
        } catch (Exception ex) {

        }
    }

    @EventHandler
    public void onAddSellItem(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (addSellItem.containsKey(p)) {
            String name = e.getMessage();
            if (!name.equalsIgnoreCase("cancel")) {

                Shop shp = null;

                for (Shop s : addSellItem.get(p).keySet()) {
                    shp = s;
                }


                final Shop shop = shp;
                Inventory inv = addSellItem.get(p).get(shop);
                boolean can = false;
                int amt;
                try {
                    amt = Integer.parseInt(name);

                    ItemStack item = new ItemStack(amt);

                    if (shop != null) {
                        if (ShopItem.fromItemStack(shop, item, true) == null) {
                            int page = shop.getNextAvailablePage(true);
                            int s = shop.getNextSlotForPage(page, true);

                            ShopItem sItem = shop.createShopItem(item, s, page, true);

                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                            if (shop.isServerShop()) {
                                OpenShop.openShopItems(inv, p, shop, page);
                            } else {
                                OpenShopOptions.openShopOwnerOptionsInventory(inv, p, shop, page);
                            }

                        } else {
                            can = true;
                        }
                    }


                } catch (Exception ex) {

                    if (Material.getMaterial(name.toUpperCase()) != null) {
                        ItemStack item = new ItemStack(Material.valueOf(name.toUpperCase()));

                        if (shop != null) {
                            if (ShopItem.fromItemStack(shop, item, true) == null) {
                                int page = shop.getNextAvailablePage(true);
                                int s = shop.getNextSlotForPage(page, true);

                                ShopItem sItem = shop.createShopItem(item, s, page, true);

                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                if (shop.isServerShop()) {
                                    OpenShop.openShopItems(inv, p, shop, page);
                                } else {
                                    OpenShopOptions.openShopOwnerOptionsInventory(inv, p, shop, page);
                                }

                            } else {
                                can = true;
                            }
                        }
                    } else {
                        for (Material m : Material.values()) {
                            if (m.name().contains(name.toUpperCase().replaceAll(" ", "_"))) {
                                ItemStack item = new ItemStack(m);

                                if (shop != null) {
                                    if (ShopItem.fromItemStack(shop, item, true) == null) {
                                        int page = shop.getNextAvailablePage(true);
                                        int s = shop.getNextSlotForPage(page, true);

                                        ShopItem sItem = shop.createShopItem(item, s, page, true);

                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddItem"));
                                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 400, 400);


                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(inv, p, shop, page);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(inv, p, shop, page);
                                        }

                                    } else {
                                        can = true;
                                    }
                                }
                            }
                        }
                    }
                }


                if (!can) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidItem"));
                }

                addSellItem.remove(p);
                e.setCancelled(true);

            } else {
                addSellItem.remove(p);
                e.setCancelled(true);
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
        }
    }

    public boolean isAlphaNumeric(String str) {
        if (str.trim().length() < 1) {
            return false;
        }
        String acceptable = "abcdefghijklmnopqrstuvwxyz0123456789 &/$";
        for (int i = 0; i < str.length(); i++) {
            if (!acceptable.contains(str.substring(i, i + 1).toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onCreate(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (shopCreate.containsKey(p)) {
            String name = e.getMessage();
            if (!name.equalsIgnoreCase("cancel")) {
                final Chest finalChest = shopCreate.get(p);
                Block b = shopCreate2.get(p);

                if (isAlphaNumeric(name)) {


                    boolean can = true;
                    boolean Long = false;

                    if (name.length() > 16) {
                        Long = true;
                    }

                    if (new File(Core.getCore().getDataFolder(), "Shops").listFiles() != null) {

                        for (File file : new File(Core.getCore().getDataFolder(), "Shops").listFiles()) {
                            if (file.getName().contains(".yml")) {
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                                for (String s : config.getKeys(false)) {
                                    if (s.equals(name)) {
                                        can = false;
                                    }
                                }
                            }
                        }
                    }

                    if (can && !Long) {

                        if (b.getType() == Material.WALL_SIGN) {

                            new AddShop(e.getPlayer(), finalChest, name);
                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("CreateShop"));

                            shopCreate.remove(p);
                            shopCreate2.remove(p);
                            e.setCancelled(true);


                            Sign s = (Sign) b.getState();

                            s.setLine(0, MainGUI.getString("SignLine1"));
                            s.setLine(1, MainGUI.getString("SignLine2"));
                            s.setLine(2, MainGUI.getString("SignLine3Closed"));
                            s.setLine(3, MainGUI.getString("SignLine4"));

                            s.update();

                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getString("CreateShop"));

                            }

                            ShopManager.loadShops();

                            if (Config.autoAddItems()) {

                                if (finalChest != null && finalChest.getBlockInventory() != null) {
                                    Shop shop = ShopManager.fromLocation(finalChest.getLocation());
                                    int i = 18;
                                    for (final ItemStack items : finalChest.getBlockInventory().getContents()) {
                                        if (items != null && items.getType() != Material.AIR) {

                                            int am = items.getAmount();

                                            items.setAmount(1);

                                            int page = shop.getNextAvailablePage(false);
                                            int sl = shop.getNextSlotForPage(page, false);

                                            ShopItem shopItem = shop.createShopItem(items, sl, page, false);

                                            items.setAmount(am);

                                            if (items.getAmount() > 1) {
                                                int amt = items.getAmount();

                                                amt = amt - 1;

                                                shopItem.setStock(shopItem.getStock() + amt);

                                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                    public void run() {
                                                        for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                            if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                    finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                }
                                                            }
                                                        }

                                                    }
                                                }, 5L);

                                            } else {
                                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                                    public void run() {
                                                        for (int in = 0; in < finalChest.getBlockInventory().getSize(); in++) {
                                                            if (finalChest.getBlockInventory().getItem(in) != null) {

                                                                if (finalChest.getBlockInventory().getItem(in).equals(items)) {
                                                                    finalChest.getBlockInventory().setItem(in, new ItemStack(Material.AIR));
                                                                }
                                                            }
                                                        }

                                                    }
                                                }, 5L);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            shopCreate.remove(p);
                            shopCreate2.remove(p);
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("SignRemoved"));

                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                Core.getTitleManager().sendSubTitle(p, Messages.getString("SignRemoved"));


                            }

                        }
                    } else {
                        shopCreate.remove(p);
                        shopCreate2.remove(p);
                        e.setCancelled(true);
                        if (Long) {
                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("LongName"));

                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getString("LongName"));


                            }
                        }

                        if (!can) {
                            e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NameTaken"));

                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getString("NameTaken"));


                            }
                        }
                    }
                } else {
                    e.getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperName"));

                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                        Core.getTitleManager().sendTitle(p, Messages.getString("ImproperName"));


                    }
                }
            } else {
                shopCreate.remove(p);
                shopCreate2.remove(p);
                e.setCancelled(true);
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
        }
    }

    @EventHandler
    public void onSearches(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();

        if (searchMaterial.containsKey(p)) {
            final String name = e.getMessage();

            if (!name.equalsIgnoreCase("cancel")) {

                Shop shp = null;

                for (Shop s : searchMaterial.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                final boolean sell = searchMaterial.get(p).get(shop);

                int amt = 0;
                boolean can = true;
                try {
                    amt = Integer.parseInt(name);
                    can = false;


                } catch (Exception ex) {
                }

                if (can) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            MaterialCheck.searchByMaterial(null, p, shop, name, sell);
                        }
                    }, 1L);

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperSearch"));
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            searchMaterial.remove(p);
            e.setCancelled(true);
        }

        if (searchID.containsKey(p)) {
            final String name = e.getMessage();
            if (!name.equalsIgnoreCase("Cancel")) {

                Shop shp = null;

                for (Shop s : searchID.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                final boolean sell = searchID.get(p).get(shop);

                int amt = 0;
                boolean can = false;
                try {
                    amt = Integer.parseInt(name);
                    can = true;


                } catch (Exception ex) {
                }

                if (can) {
                    final int carl = amt;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            IdCheck.searchById(null, p, shop, carl, sell);
                        }
                    }, 1L);

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperSearch"));
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            searchID.remove(p);
            e.setCancelled(true);
        }

        if (searchPrice.containsKey(p)) {
            final String name = e.getMessage();
            if (!name.equalsIgnoreCase("Cancel")) {

                Shop shp = null;

                for (Shop s : searchPrice.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                final boolean sell = searchPrice.get(p).get(shop);

                int amt = 0;
                boolean can = false;
                try {
                    amt = Integer.parseInt(name);
                    can = true;


                } catch (Exception ex) {
                }

                if (can) {
                    final int carl = amt;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            PriceCheck.searchByPrice(null, p, shop, carl, sell);
                        }
                    }, 1L);

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperSearch"));
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            searchPrice.remove(p);
            e.setCancelled(true);
        }

        if (searchName.containsKey(p)) {
            final String name = e.getMessage();
            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : searchName.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                final boolean sell = searchName.get(p).get(shop);

                int amt = 0;
                boolean can = true;
                try {
                    amt = Integer.parseInt(name);
                    can = false;


                } catch (Exception ex) {
                }

                if (can) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            DisplayNameCheck.searchByName(null, p, shop, name, sell);
                        }
                    }, 1L);

                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperSearch"));
                }

            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            searchName.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCollectStock(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();


        if (collectStock.containsKey(p)) {

            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : collectStock.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = collectStock.get(p).get(shop);

                int amt;
                try {
                    amt = Integer.parseInt(name);
                } catch (Exception ex) {
                    if (name.equalsIgnoreCase("all")) {
                        Stocks.collectAll(ite, shop, p);
                        assert shop != null;
                        if (shop.isHoloShop()){
                            ShopHologram h = shop.getHolographicShop();
                            h.updateItemLines(h.getItemLine(),true);
                        }
                        return;
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                        OpenSellingOptions.openShopSellingOptions(null, p, shop, ite.getPage());
                        return;
                    }
                }

                Stocks.collectStock(ite, amt, p, shop);


            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            collectStock.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAddStock(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (addStock.containsKey(p)) {

            e.setCancelled(true);


            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : addStock.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = addStock.get(p).get(shop);

                int amt;
                try {
                    amt = Integer.parseInt(name);
                } catch (Exception ex) {
                    if (name.equalsIgnoreCase("all")) {
                        Stocks.addAll(ite, shop, p);
                        assert shop != null;
                        if (shop.isHoloShop()){
                            ShopHologram h = shop.getHolographicShop();
                            h.updateItemLines(h.getItemLine(),ite.isSelling());
                        }
                        return;
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                        OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
                        return;
                    }
                }

                Stocks.addStock(ite, amt, p, shop);


            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            addStock.remove(p);
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onRemoveStock(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (removeStock.containsKey(p)) {

            e.setCancelled(true);


            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : removeStock.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = removeStock.get(p).get(shop);

                int amt;
                try {
                    amt = Integer.parseInt(name);
                } catch (Exception ex) {
                    if (name.equalsIgnoreCase("all")) {
                        Stocks.removeAll(ite, shop, p);
                        assert shop != null;
                        if (shop.isHoloShop()){
                            ShopHologram h = shop.getHolographicShop();
                            h.updateItemLines(h.getItemLine(),ite.isSelling());
                        }
                        return;
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                        OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
                        return;
                    }
                }

                Stocks.removeStock(ite, amt, p, shop);


            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            removeStock.remove(p);
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onSetBuyPrice(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setBuyPrice.containsKey(p)) {

            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : setBuyPrice.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setBuyPrice.get(p).get(shop);

                boolean can;
                double amt = 0.0;
                try {
                    amt = Double.parseDouble(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {
                    if (amt >= 0) {
                        if (amt <= Config.getMaxPrice()) {
                            assert shop != null;
                            ite.setPrice(amt);
                            if (shop.isHoloShop()){
                                ShopHologram h = shop.getHolographicShop();
                                h.updateItemLines(h.getItemLine(),ite.isSelling());
                            }
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangePrice"));
                        } else {
                            if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPriceAsString() + ")");
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPrice() + ")");
                            }
                        }
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
                    }
                }
                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            setBuyPrice.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetSellPrice(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setSellPrice.containsKey(p)) {

            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : setSellPrice.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setSellPrice.get(p).get(shop);

                boolean can;
                double amt = 0.0;
                try {
                    amt = Double.parseDouble(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {
                    if (amt >= 0) {
                        if (amt <= Config.getMaxPrice()) {
                            assert shop != null;
                            ite.setPrice(amt);
                            if (shop.isHoloShop()){
                                ShopHologram h = shop.getHolographicShop();
                                h.updateItemLines(h.getItemLine(),ite.isSelling());
                            }
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangePrice"));
                        } else {
                            if (String.valueOf(Config.getMaxPrice()).contains("E")) {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPriceAsString() + ")");
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighPrice") + " §7(Max: " + Config.getMaxPrice() + ")");
                            }
                        }
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("Zero"));
                    }
                }

                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            setSellPrice.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetBuyAmount(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setBuyAmount.containsKey(p)) {
            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : setBuyAmount.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setBuyAmount.get(p).get(shop);

                boolean can;
                int amt = 0;
                try {
                    amt = Integer.parseInt(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {
                    if (amt > 0 && amt <= 2304) {
                        assert shop != null;
                        ite.setAmount(amt);
                        if (shop.isHoloShop()){
                            ShopHologram h = shop.getHolographicShop();
                            h.updateItemLines(h.getItemLine(),ite.isSelling());
                        }
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeAmount"));
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighAmount"));
                    }
                }
                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            setBuyAmount.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetSellAmount(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setSellAmount.containsKey(p)) {
            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : setSellAmount.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setSellAmount.get(p).get(shop);

                boolean can;
                int amt = 0;
                try {
                    amt = Integer.parseInt(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {
                    if (amt > 0 && amt <= 2304) {
                        assert shop != null;
                        ite.setAmount(amt);
                        if (shop.isHoloShop()){
                            ShopHologram h = shop.getHolographicShop();
                            h.updateItemLines(h.getItemLine(),ite.isSelling());
                        }
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeAmount"));
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("HighAmount"));
                    }
                }

                OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            setSellAmount.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetDoubleAmount(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setDoubleAmount.containsKey(p)) {
            if (!name.equalsIgnoreCase("Cancel")) {
                Shop shp = null;

                for (Shop s : setDoubleAmount.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setDoubleAmount.get(p).get(shop);

                boolean can;
                int amt = 0;
                try {
                    amt = Integer.parseInt(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {
                    assert shop != null;
                    ite.setAmountToDouble(amt);
                }
                LiveEco.openLiveEcoInventory(shop, p, ite, ite.getItem());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            setDoubleAmount.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChangeData(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (changeData.containsKey(p)) {
            if (!name.equalsIgnoreCase("Cancel")) {
                e.setCancelled(true);
                Shop shp = null;

                for (Shop s : changeData.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = changeData.get(p).get(shop);

                boolean can;
                int amt = 0;
                try {
                    amt = Integer.parseInt(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {

                    assert shop != null;

                    boolean cal = false;

                    if (shop.isHoloShop()) {
                        ShopHologram h = shop.getHolographicShop();
                        if (h.getItemLine().getItemStack().equals(ite.getItem())) {
                            cal = true;
                        }
                    }

                    ite.setData((byte) amt);

                    if (cal) {
                        ShopHologram h = shop.getHolographicShop();
                        h.getItemLine().setItemStack(ite.getItem());
                    }
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeData"));

                }
                OpenSellingOptions.openShopSellingOptions(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            e.setCancelled(true);
            changeData.remove(p);
        }
    }

    @EventHandler
    public void onSetLimit(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String name = e.getMessage();

        if (setLimit.containsKey(p)) {
            if (!name.equalsIgnoreCase("Cancel")) {
                e.setCancelled(true);
                Shop shp = null;

                for (Shop s : setLimit.get(p).keySet()) {
                    shp = s;
                }

                final Shop shop = shp;

                ShopItem ite = setLimit.get(p).get(shop);

                boolean can;
                int amt = 0;
                try {
                    amt = Integer.parseInt(name);
                    can = true;
                } catch (Exception ex) {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidNumber"));
                    can = false;
                }

                if (can) {

                    assert shop != null;

                    ite.setLimit(amt);

                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeLimit"));

                }
                OpenSellingOptions.openShopSellingOptions(null, p, shop, ite.getPage());
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            e.setCancelled(true);
            setLimit.remove(p);
        }
    }

    @EventHandler
    public void onDescription(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (description.containsKey(p)) {
            String name = e.getMessage();
            if (!name.equalsIgnoreCase("Cancel")) {

                Shop shop = description.get(p);

                if (isAlphaNumeric(name)) {
                    if (name.length() <= 26) {

                        shop.setDescription(name);

                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ChangeDescription"));
                        p.closeInventory();
                        ShopSettings.openShopManager(null, p, shop);
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("LongDescription"));
                        p.closeInventory();
                        ShopSettings.openShopManager(null, p, shop);
                    }
                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ImproperDescription"));

                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                        p.closeInventory();
                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                        Core.getTitleManager().sendSubTitle(p, Messages.getString("ImproperDescription"));


                    }
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            description.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAddKeeper(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (addKeeper.containsKey(p)) {
            String name = e.getMessage();

            if (!name.equalsIgnoreCase("Cancel")) {

                Shop shop = addKeeper.get(p);

                OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                if (player.hasPlayedBefore()) {

                    if (!shop.getManagers().contains(player)) {
                        shop.addManager(player);
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("AddedKeeper"));
                        ShopKeeperManager.openKeeperManager(p, shop);
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("AlreadyAKeeper"));
                        ShopKeeperManager.openKeeperManager(p, shop);
                    }
                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidKeeper"));
                    ShopKeeperManager.openKeeperManager(p, shop);
                }
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            addKeeper.remove(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRemoveKeeper(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (removeKeeper.containsKey(p)) {
            String name = e.getMessage();

            if (!name.equalsIgnoreCase("Cancel")) {

                Shop shop = removeKeeper.get(p);

                OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                if (shop.getManagers().contains(player) && player.hasPlayedBefore()) {

                    shop.removeManager(player);
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("RemovedKeeper"));
                } else {
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("InvalidKeeper"));
                }
                ShopKeeperManager.openKeeperManager(p, shop);
            } else {
                p.sendMessage(Messages.getString("Prefix") + Messages.getString("Cancelled"));
            }
            removeKeeper.remove(p);
            e.setCancelled(true);
        }
    }
}
