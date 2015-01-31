package me.moomaxie.BetterShops.Listeners.SellerOptions;

import BetterShops.Dev.API.Events.ShopSellItemEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.BuyingAndSelling;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.Stocks;
import me.moomaxie.BetterShops.Shops.Shop;
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
            inv = Bukkit.createInventory(p, 54, "§7[Shop] §a" + shop.getName());
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


        if (i != null && can && Core.getEconomy().hasAccount(Bukkit.getOfflinePlayer(p.getUniqueId())) && Core.getEconomy().hasAccount(shop.getOwner())) {
            if (Core.getEconomy().getBalance(shop.getOwner()) >= shop.getPrice(it, true) && i.getAmount() >= shop.getAmount(it, true) || shop.isServerShop() && i.getAmount() >= shop.getAmount(it, true) || Stocks.getNumberInInventory(it, p, shop) >= shop.getAmount(it, true)) {

                inv.setItem(18, buy);
                inv.setItem(19, buy);
            } else {
                if (i.getAmount() < shop.getAmount(it, true) || Stocks.getNumberInInventory(it, p, shop) < shop.getAmount(it, true)) {

                    if (Stocks.getNumberInInventory(it, p, shop) == 0) {
                        inv.setItem(18, enough);
                        inv.setItem(19, enough);
                    } else {

                        double price = shop.getPrice(it, true);

                        int amt = shop.getAmount(it, true);

                        double pr = price / amt;

                        int a = Stocks.getNumberInInventory(it, p, shop);

                        double adj = pr * a;


                        ItemStack adjust = new ItemStack(Material.WOOL, 1, (byte) 5);
                        ItemMeta adjustMeta = adjust.getItemMeta();
                        adjustMeta.setDisplayName(BuyingAndSelling.getString("AdjustedPrice"));
                        adjustMeta.setLore(Arrays.asList(BuyingAndSelling.getString("AdjustedPriceLore").replaceAll("<Amount>","" + adj)));
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
        if (e.getInventory().getName().contains("§7[Shop]")) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(BuyingAndSelling.getString("SellCancelLore"))) {
                        OpenSellShop.openSellerShop(e.getInventory(), p, shop, 1);
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("SellItem"))) {
                        ItemStack item = e.getInventory().getItem(4);

                        int price = shop.getAmount(item, true);

                        double pr = shop.getPrice(item, true);

                        shop.setStock(item, shop.getStock(item, true) + price, true);

                        if (!shop.isServerShop()) {
                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                            if (shop.isNotify()) {
                                if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getNotifySellItem().replaceAll("<Player>", p.getDisplayName()));
                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + pr));

                                    if (Core.isAboveEight() && Config.useTitles()) {

                                            Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                            Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getNotifySellItem().replaceAll("<Player>", p.getDisplayName()));
                                            Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + pr));


                                    }
                                }
                            }
                        } else {
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                        }

                        Stocks.removeItemsFromInventory(item, p, shop, price);

                        OpenSellShop.openSellerShop(null, p, shop, 1);

                        p.sendMessage(Messages.getPrefix() + Messages.getSellItem());
                        p.sendMessage(Messages.getPrefix() + Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + pr));

                        if (Core.isAboveEight() && Config.useTitles()) {

                                Core.getTitleManager().setTimes(p, 20, 60, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getSellItem());
                                Core.getTitleManager().sendSubTitle(p, Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + pr));


                                p.closeInventory();
                                p.closeInventory();

                        }
                        if (shop.getHistory() == null){
                            shop.loadTransactions();
                        }
                        shop.getHistory().addTransaction(p,new Date(),item,pr,price,true,true);

                        ShopSellItemEvent ev = new ShopSellItemEvent(item,shop);

                        Bukkit.getPluginManager().callEvent(ev);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("AdjustedPrice"))) {

                        double pric = shop.getPrice(e.getInventory().getItem(4), true);

                        int amt = shop.getAmount(e.getInventory().getItem(4), true);

                        double pri = pric / amt;

                        int a = Stocks.getNumberInInventory(e.getInventory().getItem(4), p, shop);

                        double pr = pri * a;


                        ItemStack item = e.getInventory().getItem(4);

                        int price = Stocks.getNumberInInventory(item, p, shop);

                        shop.setStock(item, shop.getStock(item, true) + price, true);

                        if (!shop.isServerShop()) {
                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                            if (shop.isNotify()) {
                                if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getNotifySellItem().replaceAll("<Player>", p.getDisplayName()));
                                    shop.getOwner().getPlayer().sendMessage(Messages.getPrefix() + Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + pr));

                                    if (Core.isAboveEight() && Config.useTitles()) {

                                            Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                            Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getNotifySellItem().replaceAll("<Player>", p.getDisplayName()));
                                            Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getTakenAmountMessage().replaceAll("<Amount>", "" + pr));


                                    }
                                }
                            }
                        } else {
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                        }

                        Stocks.removeItemsFromInventory(item, p, shop, price);

                        OpenSellShop.openSellerShop(null, p, shop, 1);

                        p.sendMessage(Messages.getPrefix() + Messages.getSellItem());
                        p.sendMessage(Messages.getPrefix() + Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + pr));

                        if (Core.isAboveEight() && Config.useTitles()) {

                                Core.getTitleManager().setTimes(p, 20, 60, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getSellItem());
                                Core.getTitleManager().sendSubTitle(p, Messages.getGainedAmountMessage().replaceAll("<Amount>", "" + pr));


                                p.closeInventory();

                        }

                        if (shop.getHistory() == null){
                            shop.loadTransactions();
                        }
                        shop.getHistory().addTransaction(p,new Date(),item,pr,price,true,true);

                        ShopSellItemEvent ev = new ShopSellItemEvent(item,shop);

                        Bukkit.getPluginManager().callEvent(ev);
                    }
                }
            }
        }
    }
}
