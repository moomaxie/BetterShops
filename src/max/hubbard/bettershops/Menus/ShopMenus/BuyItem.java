package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Events.ShopBuyItemEvent;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.ShopHologram;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Utils.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
public class BuyItem implements ShopMenu {

    Shop shop;
    Inventory inv;

    public BuyItem(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }


    @Override
    public MenuType getType() {
        return MenuType.BUY_ITEM;
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


        final ShopItem shopItem = (ShopItem) obj[1];

        ItemStack buy = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(Language.getString("BuyingAndSelling", "BuyItem"));
        buyMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "BuyItemLore")));
        buy.setItemMeta(buyMeta);
        ClickableItem enableClick = new ClickableItem(new ShopItemStack(buy), inv, p);
        enableClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                buyItem(shopItem, p);
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
            }
        });

        ItemStack cancel = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(Language.getString("BuyingAndSelling", "Cancel"));
        cancelMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "CancelLore")));
        cancel.setItemMeta(cancelMeta);
        ClickableItem cancelClick = new ClickableItem(new ShopItemStack(cancel), inv, p);
        cancelClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
            }
        });

        ItemStack enough = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enoughMeta = enough.getItemMeta();
        enoughMeta.setDisplayName(Language.getString("BuyingAndSelling", "NotEnough"));
        enoughMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "NotEnoughMoney")));
        enough.setItemMeta(enoughMeta);

        ItemStack enough2 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough2Meta = enough2.getItemMeta();
        enough2Meta.setDisplayName(Language.getString("BuyingAndSelling", "NotEnough"));
        enough2Meta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "NotEnoughStock")));
        enough2.setItemMeta(enough2Meta);

        ItemStack enough3 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough3Meta = enough3.getItemMeta();
        enough3Meta.setDisplayName(Language.getString("BuyingAndSelling", "NoAccount"));
        enough3Meta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "NoAccountLore")));
        enough3.setItemMeta(enough3Meta);

        ItemStack enough4 = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta enough4Meta = enough4.getItemMeta();
        enough4Meta.setDisplayName(Language.getString("Timings", "CannotBuy"));
        enough4Meta.setLore(Arrays.asList(Language.getString("Timings", "Available") + shopItem.getTransCooldownTiming().getDifferenceString()));
        enough4.setItemMeta(enough4Meta);

        ItemStack limit = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta limitMeta = limit.getItemMeta();

        final double a = Cooldowns.getAmount(p, shopItem);
        double price = shopItem.getPrice();
        price = price / shopItem.getAmount();
        price = price * a;
        limitMeta.setDisplayName(Language.getString("Timings", "OnlyBuy").replaceAll("<Amount>", "" + a).replaceAll("<Price>", price + ""));
        limitMeta.setLore(Arrays.asList(Language.getString("BuyingAndSelling", "BuyItemLore")));
        limit.setItemMeta(limitMeta);

        ClickableItem enabClick = new ClickableItem(new ShopItemStack(limit), inv, p);
        enabClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                buyItem(shopItem, p, (int) a);
                shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
            }
        });

        inv.setItem(4, (ItemStack) obj[0]);

        boolean c = true;

        if (shopItem.isTransCooldown()) {
            if (a > 0) {
                if (Cooldowns.canTransaction(p, shopItem, (int) a)) {
                    if (a < shopItem.getAmount()) {
                        c = false;
                        if (shopItem.getStock() > 0 && shopItem.getAmount() <= shopItem.getStock() && Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= shopItem.getPrice() || shopItem.isInfinite() && Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= shopItem.getPrice()) {
                            inv.setItem(18, limit);
                            inv.setItem(19, limit);
                        } else {
                            if (Core.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) < shopItem.getPrice()) {
                                inv.setItem(18, enough);
                                inv.setItem(19, enough);
                            } else {
                                if (!shopItem.isInfinite()) {
                                    inv.setItem(18, enough2);
                                    inv.setItem(19, enough2);
                                } else {
                                    inv.setItem(18, limit);
                                    inv.setItem(19, limit);
                                }
                            }
                        }
                    } else {
                        c = true;
                    }
                } else {
                    c = false;
                    inv.setItem(18, enough4);
                    inv.setItem(19, enough4);
                }
            } else {
                c = false;
                inv.setItem(18, enough4);
                inv.setItem(19, enough4);
            }
        }

        if (c) {
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

    public void buyItem(ShopItem shopItem, Player p) {
        if (!shop.isOpen() && !(boolean) Config.getObject("UseOnClose")) {
            p.closeInventory();
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ShopClosed"));
            return;
        }
        ItemStack item = inv.getItem(4);

        if (item.getItemMeta().getLore() != null && !shopItem.getLiveEco()) {

            for (String s : item.getItemMeta().getLore()) {
                if (s.contains(Language.getString("MainGUI", "Price"))) {
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


        if (item.getType() != Material.SKULL_ITEM) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = shopItem.getLore();

            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = shopItem.getLore();

            meta.setLore(lore);
            item.setItemMeta(meta);
        }


        final double price = shopItem.getPrice();

        boolean c = true;

        if ((boolean) Config.getObject("Permissions")) {
            if (!Permissions.hasBuyItemPerm(p, shopItem.getItem().getType())) {
                c = false;
            }
        }

        if (c) {

            if (!shop.isServerShop()) {
                if (shopItem.getStock() > 0 || shopItem.isInfinite()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), price);
                        }
                    });


                    if (shop.isNotify()) {
                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + price));

                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + price));


                            }
                        }
                    }
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
                    return;
                }
            } else {
                if (shopItem.getStock() > 0 || shopItem.isInfinite()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
                    return;
                }
            }

            int amt = shopItem.getAmount();

            ItemMeta meta = item.getItemMeta();

            meta.setLore(shopItem.getLore());
            item.setItemMeta(meta);

            item.setAmount(amt);

            Stocks.addItemsToInventory(shopItem, p, amt);

            if (!shopItem.isInfinite())
                shopItem.setObject("Stock", shopItem.getStock() - amt);

            shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());

            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "BuyItem"));
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + price));

            if (shopItem.isTransCooldown()) {
                Cooldowns.addAmount(p, shopItem, amt);
            }

            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                Core.getTitleManager().setTimes(p, 20, 60, 20);
                Core.getTitleManager().sendTitle(p, Language.getString("Messages", "BuyItem"));
                Core.getTitleManager().sendSubTitle(p, Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + price));

                p.closeInventory();
            }

            if (shop.getHistory() == null) {
                shop.loadTransactions();
            }

            shop.getHistory().addTransaction(p, new Date(), shopItem, price, amt, false, true);

            ShopBuyItemEvent ev = new ShopBuyItemEvent(shopItem, shop, p, new Transaction(p, new Date(), shopItem, price, amt, false));

            Bukkit.getPluginManager().callEvent(ev);

            if (shopItem.getLiveEco()) {
                shopItem.setAmountTo(shopItem.getAmountTo() + 1);
            }
            if (shop.isHoloShop()) {
                ShopHologram holo = shop.getHolographicShop();
                holo.updateItemLines(holo.getItemLine(), false);
            }
        } else {
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermissionItem").replaceAll("<Item>", shopItem.getItem().getType().name()));
        }
    }

    public void buyItem(ShopItem shopItem, Player p, int amt) {
        if (!shop.isOpen() && !(boolean) Config.getObject("UseOnClose")) {
            p.closeInventory();
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ShopClosed"));
            return;
        }
        ItemStack item = inv.getItem(4);

        if (item.getItemMeta().getLore() != null && !shopItem.getLiveEco()) {

            for (String s : item.getItemMeta().getLore()) {
                if (s.contains(Language.getString("MainGUI", "Price"))) {
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


        if (item.getType() != Material.SKULL_ITEM) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = shopItem.getLore();

            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = shopItem.getLore();

            meta.setLore(lore);
            item.setItemMeta(meta);
        }


        double pric = shopItem.getPrice();
        pric = pric / shopItem.getAmount();
        pric = pric * amt;
        final double price = pric;

        boolean c = true;

        if ((boolean) Config.getObject("Permissions")) {
            if (!Permissions.hasBuyItemPerm(p, shopItem.getItem().getType())) {
                c = false;
            }
        }

        if (c) {

            if (!shop.isServerShop()) {
                if (shopItem.getStock() > 0 || shopItem.isInfinite()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        @Override
                        public void run() {
                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), price);
                        }
                    });

                    if (shop.isNotify()) {
                        if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                            shop.getOwner().getPlayer().sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + price));

                            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {

                                Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + price));


                            }
                        }
                    }
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
                    return;
                }
            } else {
                if (shopItem.getStock() > 0 || shopItem.isInfinite()) {
                    Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), price);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());
                    return;
                }
            }


            ItemMeta meta = item.getItemMeta();

            meta.setLore(shopItem.getLore());
            item.setItemMeta(meta);

            item.setAmount(amt);

            Stocks.addItemsToInventory(shopItem, p, amt);

            if (!shopItem.isInfinite())
                shopItem.setObject("Stock", shopItem.getStock() - amt);

            if (shopItem.isTransCooldown()) {
                Cooldowns.addAmount(p, shopItem, amt);
            }

            shop.getMenu(MenuType.MAIN_BUYING).draw(p, shopItem.getPage());

            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "BuyItem"));
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + price));


            if (Core.isAboveEight() && (boolean) Config.getObject("Titles") && Core.getTitleManager() != null) {


                Core.getTitleManager().setTimes(p, 20, 60, 20);
                Core.getTitleManager().sendTitle(p, Language.getString("Messages", "BuyItem"));
                Core.getTitleManager().sendSubTitle(p, Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + price));

                p.closeInventory();
            }

            if (shop.getHistory() == null) {
                shop.loadTransactions();
            }

            shop.getHistory().addTransaction(p, new Date(), shopItem, price, amt, false, true);

            ShopBuyItemEvent ev = new ShopBuyItemEvent(shopItem, shop, p, new Transaction(p, new Date(), shopItem, price, amt, false));

            Bukkit.getPluginManager().callEvent(ev);

            if (shopItem.getLiveEco()) {
                shopItem.setAmountTo(shopItem.getAmountTo() + 1);
            }
            if (shop.isHoloShop()) {
                ShopHologram holo = shop.getHolographicShop();
                holo.updateItemLines(holo.getItemLine(), false);
            }
        } else {
            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermissionItem").replaceAll("<Item>", shopItem.getItem().getType().name()));
        }
    }
}
