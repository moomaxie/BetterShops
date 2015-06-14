package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Events.ShopSellItemEvent;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Utils.Stocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
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
public class SellItem implements ShopMenu {

    Shop shop;
    Inventory inv;

    public SellItem(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }

    @Override
    public MenuType getType() {
        return MenuType.SELL_ITEM;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }


    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack it = (ItemStack) obj[0];

        final ShopItem shopItem = (ShopItem) obj[1];

        ItemStack buy = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(Language.getString("BuyingAndSelling", "SellItem"));
        buyMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "SellItemLore")));
        buy.setItemMeta(buyMeta);
        ClickableItem enableClick = new ClickableItem(new ShopItemStack(buy), inv, p);
        enableClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                sellItem(shopItem, p);
                shop.getMenu(MenuType.MAIN_SELLING).draw(p, shopItem.getPage());
            }
        });

        ItemStack all = new ItemStack(Material.WOOL, 1, (byte) 1);
        ItemMeta allMeta = all.getItemMeta();
        allMeta.setDisplayName(Language.getString("BuyingAndSelling", "SellAll"));
        allMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "SellAllLore")));
        all.setItemMeta(allMeta);
        ClickableItem allClick = new ClickableItem(new ShopItemStack(all), inv, p);
        allClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                sellAll(shopItem, p);
                shop.getMenu(MenuType.MAIN_SELLING).draw(p, shopItem.getPage());
            }
        });

        ItemStack cancel = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(Language.getString("BuyingAndSelling", "Cancel"));
        cancelMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "SellCancelLore")));
        cancel.setItemMeta(cancelMeta);
        ClickableItem cancelClick = new ClickableItem(new ShopItemStack(cancel), inv, p);
        cancelClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                shop.getMenu(MenuType.MAIN_SELLING).draw(p, shopItem.getPage());
            }
        });

        ItemStack enough = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enoughMeta = enough.getItemMeta();
        enoughMeta.setDisplayName(Language.getString("BuyingAndSelling", "NotEnough"));
        enoughMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "NotEnoughItems")));
        enough.setItemMeta(enoughMeta);


        ItemStack enough2 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough2Meta = enough2.getItemMeta();
        enough2Meta.setDisplayName(Language.getString("BuyingAndSelling", "NotEnough"));
        enough2Meta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "OutOfMoney")));
        enough2.setItemMeta(enough2Meta);

        ItemStack enough3 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough3Meta = enough3.getItemMeta();
        enough3Meta.setDisplayName(Language.getString("BuyingAndSelling", "NoAccount"));
        enough3Meta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "NoAccountLore")));
        enough3.setItemMeta(enough3Meta);

        inv.setItem(4, it);

        boolean can = false;

        ItemStack i = null;

        ItemMeta meta = it.getItemMeta();
        List<String> l = meta.getLore();

        if (l != null) {

            if (l.contains(Language.getString("MainGUI", "SellItem"))) {
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


        if (i != null && Core.getEconomy().hasAccount(Bukkit.getOfflinePlayer(p.getUniqueId())) && Core.getEconomy().hasAccount(shop.getOwner())) {
            if (Core.getEconomy().getBalance(shop.getOwner()) >= shopItem.getPrice() && i.getAmount() >= shopItem.getAmount() || shop.isServerShop() && i.getAmount() >= shopItem.getAmount() || Stocks.getNumberInInventory(shopItem, p, shop) >= shopItem.getAmount()) {

                inv.setItem(18, buy);
                inv.setItem(19, buy);

                if (Core.getEconomy().getBalance(shop.getOwner()) >= ((shopItem.getPrice() / shopItem.getAmount()) * Stocks.getNumberInInventory(shopItem, p, shop)))
                    inv.setItem(22, all);
            } else {
                if (i.getAmount() < shopItem.getAmount() || Stocks.getNumberInInventory(shopItem, p, shop) < shopItem.getAmount()) {

                    if (Stocks.getNumberInInventory(shopItem, p, shop) == 0) {
                        inv.setItem(18, enough);
                        inv.setItem(19, enough);
                    } else {

                        double price = shopItem.getPrice();

                        int amt = shopItem.getAmount();

                        double pr = price / amt;

                        int a = Stocks.getNumberInInventory(shopItem, p, shop);

                        double adj = pr * a;


                        ItemStack adjust = new ItemStack(Material.WOOL, 1, (byte) 5);
                        ItemMeta adjustMeta = adjust.getItemMeta();
                        adjustMeta.setDisplayName(Language.getString("BuyingAndSelling", "AdjustedPrice"));
                        adjustMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "AdjustedPriceLore").replaceAll("<Amount>", "" + adj)));
                        adjust.setItemMeta(adjustMeta);

                        inv.setItem(18, adjust);
                        inv.setItem(19, adjust);
                    }
                }
            }
            if (Core.getEconomy().getBalance(shop.getOwner()) < shopItem.getPrice() && !shop.isServerShop()) {
                inv.setItem(18, enough2);
                inv.setItem(19, enough2);
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

        new BukkitRunnable() {

            @Override
            public void run() {
                p.openInventory(inv);
            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void sellItem(ShopItem shopItem, Player p) {
        boolean c = true;

        if ((boolean) Config.getObject("Permissions")) {
            if (!Permissions.hasSellItemPerm(p, shopItem.getItem().getType())) {
                c = false;
            }
        }

        if (c) {
            ItemStack item = inv.getItem(4);

            if (item.getItemMeta() != null && item.getItemMeta().getLore() != null && !shopItem.getLiveEco()) {
                for (String s : item.getItemMeta().getLore()) {
                    if (s.contains(Language.getString("MainGUI", "AskingPrice"))) {
                        double pr = 0.0;
                        if (!s.substring(Language.getString("MainGUI", "Price").length() + 3).equals(Language.getString("MainGUI", "Free")))
                            pr = Double.parseDouble(s.substring(Language.getString("MainGUI", "Price").length() + 3));

                        if (pr != shopItem.getPrice()) {
                            p.closeInventory();
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "Fraud"));

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
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellLimit").replaceAll("<Amount>", "" + price));
                }

                double pri = pric / shopItem.getAmount();

                double pr = pri * price;

                if ((boolean) Config.getObject("SellToBuy") || Config.getObject("SellToBuy").equals("True")) {
                    if (shopItem.getSister() != null) {

                        int limit = (int) Config.getObject("StockLimit");

                        if (limit == 0 || shopItem.getSister().getStock() < limit) {

                            if (limit == 0 || shopItem.getSister().getStock() + shopItem.getStock() < (int) Config.getObject("StockLimit")) {
                                shopItem.getSister().setObject("Stock", shopItem.getSister().getStock() + shopItem.getStock());
                            }

                            if (limit == 0 || shopItem.getSister().getStock() + price <= (int) Config.getObject("StockLimit")) {
                                shopItem.getSister().setObject("Stock", shopItem.getSister().getStock() + price);
                                shopItem.setObject("Stock", 0);
                            } else {
                                shopItem.setObject("Stock", shopItem.getStock() + price);
                            }
                        } else {
                            shopItem.setObject("Stock", shopItem.getStock() + price);
                        }
                    } else {
                        shopItem.setObject("Stock", shopItem.getStock() + price);
                    }
                } else {
                    shopItem.setObject("Stock", shopItem.getStock() + price);
                }

                if (!shop.isServerShop()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                    if (shop.isNotify()) {
                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotifySell").replaceAll("<Player>", p.getDisplayName()));
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + pr));

                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + pr));


                            }
                        }
                    }
                } else {
                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                }

                Stocks.removeItemsFromInventory(shopItem, p, shop, price);

                shop.getMenu(MenuType.MAIN_SELLING).draw(p, shopItem.getPage());

                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellItem"));
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + pr));

                if (shop.getHistory() == null) {
                    shop.loadTransactions();
                }
                shop.getHistory().addTransaction(p, new Date(), shopItem, pr, price, true, true);

                ShopSellItemEvent ev = new ShopSellItemEvent(shopItem, shop, p);

                Bukkit.getPluginManager().callEvent(ev);

                if (shopItem.getLiveEco()) {
                    shopItem.setAmountTo(shopItem.getSister().getAmountTo() - 2);
                }
                if (shop.isHoloShop()) {
                    ShopHologram holo = shop.getHolographicShop();
                    holo.updateItemLines(holo.getItemLine(), true);
                }
            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LimitReached"));
            }
        }
    }

    public void sellAll(ShopItem shopItem, Player p) {
        boolean c = true;

        if ((boolean) Config.getObject("Permissions")) {
            if (!Permissions.hasSellItemPerm(p, shopItem.getItem().getType())) {
                c = false;
            }
        }

        if (c) {
            double pric = shopItem.getPrice();

            int amt = shopItem.getAmount();

            int a = Stocks.getNumberInInventory(shopItem, p, shop);

            if (shopItem.getLimit() == 0 || shopItem.getStock() < shopItem.getLimit()) {

                if (shopItem.getLimit() != 0 && a + shopItem.getStock() >= shopItem.getLimit()) {
                    a = shopItem.getLimit() - shopItem.getStock();
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellLimit").replaceAll("<Amount>", "" + a));
                }

                double pri = pric / amt;

                double pr = pri * a;

                pr = Double.parseDouble(new DecimalFormat("#.00").format(pr));

                int price = a;

                if ((boolean) Config.getObject("SellToBuy") || Config.getObject("SellToBuy").equals("True")) {
                    if (shopItem.getSister() != null) {

                        int limit = (int) Config.getObject("StockLimit");

                        if (limit == 0 || shopItem.getSister().getStock() < limit) {

                            if (limit == 0 || shopItem.getSister().getStock() + shopItem.getStock() < (int) Config.getObject("StockLimit")) {
                                shopItem.getSister().setObject("Stock", shopItem.getSister().getStock() + shopItem.getStock());
                            }

                            if (limit == 0 || shopItem.getSister().getStock() + price <= (int) Config.getObject("StockLimit")) {
                                shopItem.getSister().setObject("Stock", shopItem.getSister().getStock() + price);
                                shopItem.setObject("Stock", 0);
                            } else {
                                shopItem.setObject("Stock", shopItem.getStock() + price);
                            }
                        } else {
                            shopItem.setObject("Stock", shopItem.getStock() + price);
                        }
                    } else {
                        shopItem.setObject("Stock", shopItem.getStock() + price);
                    }
                } else {
                    shopItem.setObject("Stock", shopItem.getStock() + price);
                }

                if (!shop.isServerShop()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), pr);
                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);


                    if (shop.isNotify()) {
                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotifySell").replaceAll("<Player>", p.getDisplayName()));
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + pr));

                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "NotifySell").replaceAll("<Player>", p.getDisplayName()));
                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + pr));


                            }
                        }
                    }
                } else {
                    Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), pr);
                }

                Stocks.removeItemsFromInventory(shopItem, p, shop, price);

                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellItem"));
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + pr));


                if (shop.getHistory() == null) {
                    shop.loadTransactions();
                }
                shop.getHistory().addTransaction(p, new Date(), shopItem, pr, price, true, true);

                ShopSellItemEvent ev = new ShopSellItemEvent(shopItem, shop, p);

                Bukkit.getPluginManager().callEvent(ev);

                shop.getMenu(MenuType.MAIN_SELLING).draw(p, shopItem.getPage());

                if (shopItem.getLiveEco()) {
                    double o = price / shopItem.getAmount();
                    shopItem.setAmountTo(shopItem.getSister().getAmountTo() - (o * 2));
                }

                if (shop.isHoloShop()) {
                    ShopHologram holo = shop.getHolographicShop();
                    holo.updateItemLines(holo.getItemLine(), true);
                }


            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LimitReached"));
            }
        }
    }
}
