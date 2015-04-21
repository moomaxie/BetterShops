package me.moomaxie.BetterShops.Listeners.SellerOptions;

import BetterShops.Dev.API.Events.ShopSellItemEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.BuyingAndSelling;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopManager;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.ShopTypes.Holographic.ShopHologram;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class SellItem implements Listener {


    public static void openSellScreen(Inventory inv, Player p, Shop shop, ItemStack it) {

        boolean b = false;

        if (inv == null) {
            b = true;
            inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());
        } else {
            inv.clear();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack buy = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(BuyingAndSelling.getString("SellItem"));
        buyMeta.setLore(Arrays.asList(BuyingAndSelling.getString("SellItemLore")));
        buy.setItemMeta(buyMeta);

        ItemStack all = new ItemStack(Material.WOOL, 1, (byte) 1);
        ItemMeta allMeta = all.getItemMeta();
        allMeta.setDisplayName(BuyingAndSelling.getString("SellAll"));
        allMeta.setLore(Arrays.asList(BuyingAndSelling.getString("SellAllLore")));
        all.setItemMeta(allMeta);

        ItemStack cancel = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(BuyingAndSelling.getString("Cancel"));
        cancelMeta.setLore(Arrays.asList(BuyingAndSelling.getString("SellCancelLore")));
        cancel.setItemMeta(cancelMeta);

        ItemStack enough = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enoughMeta = enough.getItemMeta();
        enoughMeta.setDisplayName(BuyingAndSelling.getString("NotEnough"));
        enoughMeta.setLore(Arrays.asList(BuyingAndSelling.getString("NotEnoughItems")));
        enough.setItemMeta(enoughMeta);


        ItemStack enough2 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough2Meta = enough2.getItemMeta();
        enough2Meta.setDisplayName(BuyingAndSelling.getString("NotEnough"));
        enough2Meta.setLore(Arrays.asList(BuyingAndSelling.getString("OutOfMoney")));
        enough2.setItemMeta(enough2Meta);

        ItemStack enough3 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough3Meta = enough3.getItemMeta();
        enough3Meta.setDisplayName(BuyingAndSelling.getString("NoAccount"));
        enough3Meta.setLore(Arrays.asList(BuyingAndSelling.getString("NoAccountLore")));
        enough3.setItemMeta(enough3Meta);

        inv.setItem(4, it);

        boolean can = false;

        ItemStack i = null;

        ItemMeta meta = it.getItemMeta();
        List<String> l = meta.getLore();

        if (l != null) {

            if (l.contains(MainGUI.getString("SellItem"))) {
                int s = l.size();
                for (int itt = s - 1; itt > s - 5; itt--) {
                    if (itt > -1) {
                        l.remove(itt);
                    } else break;
                }
            }

            meta.setLore(l);
            it.setItemMeta(meta);
        }

        for (ItemStack ite : p.getInventory().getContents()) {
            if (ite != null) {
                int amt = ite.getAmount();

                ite.setAmount(1);

                if (it.equals(ite)) {
                    can = true;
                    ite.setAmount(amt);
                    i = ite;
                }

                ite.setAmount(amt);

            }
        }

        ShopItem shopItem = ShopItem.fromItemStack(shop, it, true);

        if (i != null && Core.getEconomy().hasAccount(Bukkit.getOfflinePlayer(p.getUniqueId())) && Core.getEconomy().hasAccount(shop.getOwner())) {
            if (Core.getEconomy().getBalance(shop.getOwner()) >= shopItem.getPrice() && i.getAmount() >= shopItem.getAmount() || shop.isServerShop() && i.getAmount() >= shopItem.getAmount() || Stocks.getNumberInInventory(it, p, shop) >= shopItem.getAmount()) {

                inv.setItem(18, buy);
                inv.setItem(19, buy);

                inv.setItem(22, all);
            } else {
                if (i.getAmount() < shopItem.getAmount() || Stocks.getNumberInInventory(it, p, shop) < shopItem.getAmount()) {

                    if (Stocks.getNumberInInventory(it, p, shop) == 0) {
                        inv.setItem(18, enough);
                        inv.setItem(19, enough);
                    } else {

                        double price = shopItem.getPrice();

                        int amt = shopItem.getAmount();

                        double pr = price / amt;

                        int a = Stocks.getNumberInInventory(it, p, shop);

                        double adj = pr * a;


                        ItemStack adjust = new ItemStack(Material.WOOL, 1, (byte) 5);
                        ItemMeta adjustMeta = adjust.getItemMeta();
                        adjustMeta.setDisplayName(BuyingAndSelling.getString("AdjustedPrice"));
                        adjustMeta.setLore(Arrays.asList(BuyingAndSelling.getString("AdjustedPriceLore").replaceAll("<Amount>", "" + adj)));
                        adjust.setItemMeta(adjustMeta);

                        inv.setItem(18, adjust);
                        inv.setItem(19, adjust);
                    }
                } else {
                    inv.setItem(18, enough2);
                    inv.setItem(19, enough2);
                }
            }
        } else {
            if (!Core.getEconomy().hasAccount(Bukkit.getOfflinePlayer(p.getUniqueId()))) {
                inv.setItem(18, enough3);
                inv.setItem(19, enough3);
            } else {
                inv.setItem(18, enough);
                inv.setItem(19, enough);
            }
        }


        inv.setItem(25, cancel);
        inv.setItem(26, cancel);

        if (b)
            p.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSettingsClick(final InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta() != null) {
                    String name = e.getInventory().getName();
                    name = name.substring(MainGUI.getString("ShopHeader").length());

                    final Shop shop = ShopManager.fromString(p, name);

                    ShopItem shopItem = ShopItem.fromItemStack(shop, e.getInventory().getItem(4), true);

                    if (shopItem != null) {

                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(BuyingAndSelling.getString("SellCancelLore"))) {
                            OpenSellShop.openSellerShop(e.getInventory(), p, shop, shopItem.getPage());
                            return;
                        }

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("SellItem"))) {
                            ItemStack item = e.getInventory().getItem(4);

                            if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                                for (String s : item.getItemMeta().getLore()) {
                                    if (s.contains(MainGUI.getString("AskingPrice"))) {
                                        double pr = Double.parseDouble(s.substring(MainGUI.getString("AskingPrice").length() + 3));

                                        if (pr != shopItem.getPrice()) {
                                            p.closeInventory();
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("Fraud"));

                                            return;
                                        }
                                    }
                                }
                            }

                            int price = shopItem.getAmount();

                            double pric = shopItem.getPrice();

                            if (shopItem.getLimit() == 0 || shopItem.getStock() < shopItem.getLimit()) {

                                if (shopItem.getLimit() != 0 && shopItem.getAmount() + shopItem.getStock() >= shopItem.getLimit()) {
                                    price = shopItem.getLimit() - shopItem.getStock();
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("SellLimit").replaceAll("<Amount>", "" + price));
                                }

                                double pri = pric / shopItem.getAmount();

                                double pr = pri * price;

                                shopItem.setStock(shopItem.getStock() + price);

                                if (!shop.isServerShop()) {
                                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                                    if (shop.isNotify()) {
                                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("TakenAmount").replaceAll("<Amount>", "" + pr));

                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getString("NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getString("TakenAmount").replaceAll("<Amount>", "" + pr));


                                            }
                                        }
                                    }
                                } else {
                                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                                }

                                Stocks.removeItemsFromInventory(shopItem, p, shop, price);

                                OpenSellShop.openSellerShop(null, p, shop, shopItem.getPage());

                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("SellItem"));
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + pr));

                                if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                    Core.getTitleManager().setTimes(p, 20, 60, 20);
                                    Core.getTitleManager().sendTitle(p, Messages.getString("SellItem"));
                                    Core.getTitleManager().sendSubTitle(p, Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + pr));


                                    p.closeInventory();
                                    p.closeInventory();

                                }
                                if (shop.getHistory() == null) {
                                    shop.loadTransactions();
                                }
                                shop.getHistory().addTransaction(p, new Date(), shopItem, pr, price, true, true);

                                ShopSellItemEvent ev = new ShopSellItemEvent(shopItem, shop,p);

                                Bukkit.getPluginManager().callEvent(ev);

                                if (shopItem.getLiveEco()) {
                                    shopItem.setAmountTo(shopItem.getSister().getAmountTo() - 2);
                                }
                                if (shop.isHoloShop()) {
                                    ShopHologram holo = shop.getHolographicShop();
                                    holo.updateItemLines(holo.getItemLine(), true);
                                }
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("LimitReached"));
                            }
                        }
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("AdjustedPrice")) || e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("SellAll"))) {

                            double pric = shopItem.getPrice();

                            int amt = shopItem.getAmount();

                            int a = Stocks.getNumberInInventory(e.getInventory().getItem(4), p, shop);

                            if (shopItem.getLimit() == 0 || shopItem.getStock() < shopItem.getLimit()) {

                                if (shopItem.getLimit() != 0 && a + shopItem.getStock() >= shopItem.getLimit()){
                                    a = shopItem.getLimit() - shopItem.getStock();
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("SellLimit").replaceAll("<Amount>","" + a));
                                }

                                double pri = pric / amt;

                                double pr = pri * a;

                                int price = a;

                                shopItem.setStock(shopItem.getStock() + price);

                                if (!shop.isServerShop()) {
                                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                                    if (shop.isNotify()) {
                                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("TakenAmount").replaceAll("<Amount>", "" + pr));

                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getString("NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getString("TakenAmount").replaceAll("<Amount>", "" + pr));


                                            }
                                        }
                                    }
                                } else {
                                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                                }

                                Stocks.removeItemsFromInventory(shopItem, p, shop, price);

                                OpenSellShop.openSellerShop(null, p, shop, shopItem.getPage());

                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("SellItem"));
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + pr));

//                                if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {
//
//                                    Core.getTitleManager().setTimes(p, 20, 60, 20);
//                                    Core.getTitleManager().sendTitle(p, Messages.getString("SellItem"));
//                                    Core.getTitleManager().sendSubTitle(p, Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + pr));
//
//
//                                }

                                if (shop.getHistory() == null) {
                                    shop.loadTransactions();
                                }
                                shop.getHistory().addTransaction(p, new Date(), shopItem, pr, price, true, true);

                                ShopSellItemEvent ev = new ShopSellItemEvent(shopItem, shop,p);

                                Bukkit.getPluginManager().callEvent(ev);

                                OpenSellShop.openSellerShop(e.getInventory(),p,shop,shopItem.getPage());

                                if (shopItem.getLiveEco()) {
                                    double o = price / shopItem.getAmount();
                                    shopItem.setAmountTo(shopItem.getSister().getAmountTo() - (o * 2));
                                }

                                if (shop.isHoloShop()) {
                                    ShopHologram holo = shop.getHolographicShop();
                                    holo.updateItemLines(holo.getItemLine(), true);
                                }
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("LimitReached"));
                            }
                        }
                    }
                }
            }
        }
    }
}
