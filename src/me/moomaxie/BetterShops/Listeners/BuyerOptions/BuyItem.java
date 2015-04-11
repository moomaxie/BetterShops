package me.moomaxie.BetterShops.Listeners.BuyerOptions;

import BetterShops.Dev.API.Events.ShopBuyItemEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.BuyingAndSelling;
import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.SellerOptions.SellItem;
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
import org.bukkit.inventory.meta.SkullMeta;

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
public class BuyItem implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSettings(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);

            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop.getOwner() != p || shop.isServerShop()) {

                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("SellItem")) && !e.isShiftClick()) {
                            if (!shop.isOpen() && !Config.useWhenClosed()) {
                                p.closeInventory();
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                return;
                            }
                            if (Config.usePerms() && Permissions.hasSellPerm(p) || !Config.usePerms()) {
                                SellItem.openSellScreen(null, p, shop, e.getCurrentItem());
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                            }
                        }

                        if (shop.getManagers().contains(p)) {
                            if (e.isRightClick()) {
                                if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("ShopKeeperManage"))) {
                                    openItemManager(p, shop, ShopItem.fromItemStack(shop, e.getCurrentItem(), false));
                                }
                            } else {
                                if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("LeftClickToBuy")) && !e.isShiftClick()) {
                                    if (!shop.isOpen() && !Config.useWhenClosed()) {
                                        p.closeInventory();
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                        return;
                                    }
                                    if (Config.usePerms() && Permissions.hasBuyPerm(p) || !Config.usePerms()) {
                                        openBuyScreen(null, p, shop, e.getCurrentItem());
                                    } else {
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                                    }
                                }
                            }
                        } else {
                            if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("LeftClickToBuy")) && !e.isShiftClick()) {
                                if (!shop.isOpen() && !Config.useWhenClosed()) {
                                    p.closeInventory();
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                    return;
                                }
                                if (Config.usePerms() && Permissions.hasBuyPerm(p) || !Config.usePerms()) {
                                    openBuyScreen(null, p, shop, e.getCurrentItem());
                                } else {
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void openItemManager(Player p, Shop shop, ShopItem it) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack stock = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
        ItemMeta stockMeta = stock.getItemMeta();
        stockMeta.setDisplayName(ItemTexts.getString("AddStockDisplayName"));
        stockMeta.setLore(Arrays.asList(ItemTexts.getString("AddStockLore")));
        stock.setItemMeta(stockMeta);


        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        inv.setItem(0, back);

        inv.setItem(4, it.getItem());

        inv.setItem(inv.firstEmpty(), stock);


        p.openInventory(inv);
    }

    public static void openBuyScreen(Inventory inv, Player p, Shop shop, ItemStack it) {
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
        buyMeta.setDisplayName(BuyingAndSelling.getString("BuyItem"));
        buyMeta.setLore(Arrays.asList(BuyingAndSelling.getString("BuyItemLore")));
        buy.setItemMeta(buyMeta);

        ItemStack cancel = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(BuyingAndSelling.getString("Cancel"));
        cancelMeta.setLore(Arrays.asList(BuyingAndSelling.getString("CancelLore")));
        cancel.setItemMeta(cancelMeta);

        ItemStack enough = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enoughMeta = enough.getItemMeta();
        enoughMeta.setDisplayName(BuyingAndSelling.getString("NotEnough"));
        enoughMeta.setLore(Arrays.asList(BuyingAndSelling.getString("NotEnoughMoney")));
        enough.setItemMeta(enoughMeta);

        ItemStack enough2 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough2Meta = enough2.getItemMeta();
        enough2Meta.setDisplayName(BuyingAndSelling.getString("NotEnough"));
        enough2Meta.setLore(Arrays.asList(BuyingAndSelling.getString("NotEnoughStock")));
        enough2.setItemMeta(enough2Meta);

        ItemStack enough3 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough3Meta = enough3.getItemMeta();
        enough3Meta.setDisplayName(BuyingAndSelling.getString("NoAccount"));
        enough3Meta.setLore(Arrays.asList(BuyingAndSelling.getString("NoAccountLore")));
        enough3.setItemMeta(enough3Meta);

        inv.setItem(4, it);

        ShopItem shopItem = ShopItem.fromItemStack(shop, it, false);

        if (Core.getEconomy().hasAccount(Bukkit.getOfflinePlayer(p.getUniqueId()))) {
            if (shopItem.getStock() > 0 && shopItem.getAmount() <= shopItem.getStock() && Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= shopItem.getPrice() || shopItem.isInfinite() && Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= shopItem.getPrice()) {
                inv.setItem(18, buy);
                inv.setItem(19, buy);
            } else {
                if (Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) < shopItem.getPrice()) {
                    inv.setItem(18, enough);
                    inv.setItem(19, enough);
                } else {
                    if (!shopItem.isInfinite()) {
                        inv.setItem(18, enough2);
                        inv.setItem(19, enough2);
                    } else {
                        inv.setItem(18, buy);
                        inv.setItem(19, buy);
                    }
                }
            }
        } else {
            inv.setItem(18, enough3);
            inv.setItem(19, enough3);
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
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    final Shop shop = ShopLimits.fromString(p, name);

                    ShopItem shopItem = ShopItem.fromItemStack(shop, e.getInventory().getItem(4), false);

                    if (shopItem != null) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("Cancel"))) {
                            OpenShop.openShopItems(e.getInventory(), p, shop, shopItem.getPage());
                        }

                        if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(BuyingAndSelling.getString("BuyItem"))) {
                            if (!shop.isOpen() && !Config.useWhenClosed()) {
                                p.closeInventory();
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                return;
                            }
                            ItemStack item = e.getInventory().getItem(4);

                            if (item.getItemMeta().getLore() != null) {

                                for (String s : item.getItemMeta().getLore()) {
                                    if (s.contains(MainGUI.getString("Price"))) {
                                        double pr = Double.parseDouble(s.substring(MainGUI.getString("Price").length() + 3));

                                        if (pr != shopItem.getPrice()) {
                                            p.closeInventory();
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("Fraud"));

                                            return;
                                        }
                                    }
                                }
                            }


                            if (item.getType() != Material.SKULL_ITEM) {

                                ItemMeta meta = item.getItemMeta();
                                List<String> lore = shop.getLore(item);

                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            } else {
                                SkullMeta meta = (SkullMeta) item.getItemMeta();
                                List<String> lore = shop.getLore(item);

                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }


                            double price = shopItem.getPrice();


                            if (!shop.isServerShop()) {
                                if (shopItem.getStock() > 0) {
                                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), price);

                                    if (shop.isNotify()) {
                                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                            shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + price));

                                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + price));


                                            }
                                        }
                                    }
                                } else {
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("LowStock"));
                                    OpenShop.openShopItems(e.getInventory(), p, shop, shopItem.getPage());
                                    return;
                                }
                            } else {
                                if (shopItem.getStock() > 0) {
                                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                                } else {
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("LowStock"));
                                    OpenShop.openShopItems(e.getInventory(), p, shop, shopItem.getPage());
                                    return;
                                }
                            }

                            int amt = shopItem.getAmount();

                            ItemMeta meta = item.getItemMeta();

                            meta.setLore(shopItem.getLore());
                            item.setItemMeta(meta);

                            item.setAmount(amt);

                            p.getInventory().addItem(item);

                            shopItem.setStock(shopItem.getStock() - amt);

                            OpenShop.openShopItems(null, p, shop, shopItem.getPage());

                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("BuyItem"));
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("TakenAmount").replaceAll("<Amount>", "" + price));


                            if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                Core.getTitleManager().setTimes(p, 20, 60, 20);
                                Core.getTitleManager().sendTitle(p, Messages.getString("BuyItem"));
                                Core.getTitleManager().sendSubTitle(p, Messages.getString("TakenAmount").replaceAll("<Amount>", "" + price));

                                p.closeInventory();
                            }

                            if (shop.getHistory() == null) {
                                shop.loadTransactions();
                            }

                            shop.getHistory().addTransaction(p, new Date(), shopItem, price, amt, false, true);

                            ShopBuyItemEvent ev = new ShopBuyItemEvent(shopItem, shop);

                            Bukkit.getPluginManager().callEvent(ev);

                            if (shopItem.getLiveEco()) {
                                shopItem.setAmountTo(shopItem.getAmountTo() + 1);
                            }
                            if (shop.isHoloShop()) {
                                ShopHologram holo = shop.getHolographicShop();
                                holo.updateItemLines(holo.getItemLine(), false);
                            }
                        }
                    }
                }
            }
        }
    }
}
