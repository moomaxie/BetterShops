package me.moomaxie.BetterShops.Listeners.Checkout;

import BetterShops.Dev.API.Events.ShopBuyItemEvent;
import me.moomaxie.BetterShops.Configurations.Config;
import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Configurations.WordsCapitalizer;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
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
import org.bukkit.material.MaterialData;

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

public class CheckoutMenu implements Listener {

    private static HashMap<HashMap<UUID, Shop>, List<HashMap<HashMap<ShopItem, Byte>, Integer>>> cart = new HashMap<>();

    public static void addToCart(Player p, Shop shop, ShopItem item, int amt) {
        HashMap<UUID, Shop> k = null;
        boolean can = false;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {

            HashMap<HashMap<ShopItem, Byte>, Integer> value = new HashMap<>();
            HashMap<ShopItem, Byte> by = new HashMap<>();
            by.put(item, item.getData());
            value.put(by, amt);

            List<HashMap<HashMap<ShopItem, Byte>, Integer>> v = cart.get(k);

            v.add(value);

            cart.put(k, v);


        } else {
            HashMap<UUID, Shop> key = new HashMap<>();
            key.put(p.getUniqueId(), shop);

            HashMap<HashMap<ShopItem, Byte>, Integer> value = new HashMap<>();
            HashMap<ShopItem, Byte> by = new HashMap<>();
            by.put(item, item.getData());
            value.put(by, amt);

            List<HashMap<HashMap<ShopItem, Byte>, Integer>> v = new ArrayList<>();
            v.add(value);

            cart.put(key, v);
        }
    }

    public static void openCheckoutMenu(Player p, Shop shop) {

        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        ItemStack price = new ItemStack(Material.EMERALD);
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName(Checkout.getString("Prices"));
        priceMeta.setLore(Arrays.asList(Checkout.getString("NothingInCart")));
        price.setItemMeta(priceMeta);

        ItemStack buy = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName(Checkout.getString("BuyItems"));
        buy.setItemMeta(buyMeta);

        ItemStack no = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noMeta = no.getItemMeta();
        noMeta.setDisplayName(Checkout.getString("CannotAfford"));
        no.setItemMeta(noMeta);

        ItemStack noItems = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta noItemsMeta = noItems.getItemMeta();
        noItemsMeta.setDisplayName(Checkout.getString("NothingInCart"));
        noItems.setItemMeta(noItemsMeta);

        inv.setItem(7, noItems);
        inv.setItem(8, noItems);

        inv.setItem(4, price);

        double total = 0.00;

        inv.setItem(0, back);

        inv.setItem(0, back);

        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        if (can) {
            List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                    for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                        for (ShopItem it : i.keySet()) {

                            if (it != null && ie.get(i) != null) {
                                MaterialData data = new MaterialData(it.getItem().getType(), i.get(it));
                                it.getItem().setData(data);

                                total = total + (ie.get(i) * (it.getPrice() / it.getAmount()));

                                ItemMeta meta = it.getItem().getItemMeta();
                                List<String> lore = new ArrayList<>();
                                if (it.getLore() != null) {
                                    for (String s : it.getLore()) {
                                        lore.add(s);
                                    }
                                }

                                ItemStack ite = it.getItem().clone();

                                if (lore != null) {

                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Amount") + " §7" + ie.get(i));
                                    } else {
                                        lore.add(MainGUI.getString("Amount") + " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));

                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());

                                    }
                                    lore.add(" ");
                                    lore.add(Checkout.getString("ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                                inv.setItem(inv.firstEmpty(), ite);

                                BigDecimal bd = new BigDecimal(total);
                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                                BigDecimal bd2 = new BigDecimal((Core.getEconomy().getBalance(p) - Double.parseDouble(new DecimalFormat("#.00").format(total).replaceFirst(",", ".").replaceAll(",", ""))));
                                bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);

                                priceMeta.setLore(Arrays.asList(Checkout.getString("TotalPrice") + " §7" + bd.doubleValue(),
                                        Checkout.getString("Balance") + " §7" + Core.getEconomy().getBalance(p),
                                        Checkout.getString("NewBalance") + " §7" + bd2.doubleValue()));
                                price.setItemMeta(priceMeta);
                                inv.setItem(4, price);

                                inv.setItem(7, buy);
                                inv.setItem(8, buy);
                            }
                        }
                    }
                }
            }
        }

        if ((Core.getEconomy().getBalance(p) - total) < 0) {
            inv.setItem(7, no);
            inv.setItem(8, no);
        }


        p.openInventory(inv);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrange(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(p, name);

                    if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().hasItemMeta()) {

                        if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("CheckoutLore")) && e.isLeftClick()) {
                            openCheckoutMenu(p, shop);
                            return;
                        }

                        if (e.getCurrentItem().getItemMeta().getLore() != null) {
                            if (e.getCurrentItem().getItemMeta().getLore().contains(MainGUI.getString("AddToCart")) && e.isShiftClick()) {
                                AmountChooser.openAmountChooser(1, e.getCurrentItem(), p, shop, e.getInventory());
                                return;
                            }
                        }

                        if (e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains(Checkout.getString("BuyItems"))) {
                            if (!shop.isOpen() && !Config.useWhenClosed()) {
                                p.closeInventory();
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                return;
                            }

                            for (int i = 9; i < e.getInventory().getContents().length; i++) {
                                ItemStack item = e.getInventory().getItem(i);
                                ShopItem shopItem = ShopItem.fromItemStack(shop, item, false);
                                if (shopItem != null && item != null && item.getItemMeta().getLore() != null) {
                                    List<String> lore = item.getItemMeta().getLore();

                                    double pr = Config.getDefaultPrice();
                                    int amt = 1;
                                    for (String s : lore) {
                                        if (s.contains(MainGUI.getString("Price"))) {
                                            pr = Double.parseDouble(s.substring(MainGUI.getString("Price").length() + 3));
                                        }
                                        if (s.contains(MainGUI.getString("Amount"))) {
                                            amt = Integer.parseInt(s.substring(MainGUI.getString("Amount").length() + 3));
                                        }
                                    }

                                    BigDecimal bd = new BigDecimal((pr / amt) * shopItem.getAmount());

                                    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                                    if (bd.doubleValue() != shopItem.getPrice()) {
                                        p.closeInventory();
                                        if (item.getItemMeta().getDisplayName() != null) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("PriceChange").replaceAll("<Item>", item.getItemMeta().getDisplayName()));
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("PriceChange").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(item.getType().name().replaceAll("_", " "))));
                                        }

                                        return;
                                    }
                                }
                            }

                            buyCheckoutItems(p, shop);
                            return;

                        }

                        if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().contains(Checkout.getString("ClickToRemove")) && e.isLeftClick()) {
                            boolean can = false;

                            HashMap<UUID, Shop> k = null;

                            for (HashMap<UUID, Shop> key : cart.keySet()) {
                                if (key.keySet().contains(p.getUniqueId())) {
                                    if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                                        can = true;
                                        k = key;
                                        break;
                                    }
                                }
                            }

                            if (can) {
                                List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
                                if (items != null) {
                                    for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                                        for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                                            for (ShopItem it : i.keySet()) {

                                                if (it != null && ie.get(i) != null) {
                                                    MaterialData data = new MaterialData(it.getItem().getType(), i.get(it));
                                                    it.getItem().setData(data);

                                                    ItemMeta meta = it.getItem().getItemMeta();
                                                    List<String> lore = new ArrayList<>();
                                                    if (it.getLore() != null) {
                                                        for (String s : it.getLore()) {
                                                            lore.add(s);
                                                        }
                                                    }

                                                    ItemStack ite = it.getItem().clone();

                                                    if (lore != null) {

                                                        if (ie.get(i) != null) {
                                                            lore.add(MainGUI.getString("Amount") + " §7" + ie.get(i));
                                                        } else {
                                                            lore.add(MainGUI.getString("Amount") + " §71");
                                                        }
                                                        if (ie.get(i) != null) {
                                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));

                                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                                            lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());
                                                        } else {
                                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                                            lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());

                                                        }
                                                        lore.add(" ");
                                                        lore.add(Checkout.getString("ClickToRemove"));
                                                        meta.setLore(lore);
                                                        ite.setItemMeta(meta);

                                                    }

                                                    if (e.getCurrentItem().equals(ite) && e.getCurrentItem().getData().getData() == ite.getData().getData() || e.getCurrentItem().toString().equals(ite.toString()) && e.getCurrentItem().getData().getData() == ite.getData().getData()) {

                                                        cart.get(k).remove(ie);
                                                        openCheckoutMenu(p, shop);
                                                        return;
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
            }
        }
    }

    public static void buyCheckoutItems(Player p, Shop shop) {
        double total = 0.0;
        boolean can = false;

        HashMap<UUID, Shop> k = null;

        for (HashMap<UUID, Shop> key : cart.keySet()) {
            if (key.keySet().contains(p.getUniqueId())) {
                if (key.get(p.getUniqueId()).getName().equals(shop.getName())) {
                    can = true;
                    k = key;
                    break;
                }
            }
        }

        double t = 0;
        if (can) {
            List<HashMap<HashMap<ShopItem, Byte>, Integer>> items = cart.get(k);
            if (items != null) {
                for (HashMap<HashMap<ShopItem, Byte>, Integer> ie : items) {
                    for (HashMap<ShopItem, Byte> i : ie.keySet()) {
                        for (ShopItem it : i.keySet()) {
                            ItemMeta meta = it.getItem().getItemMeta();
                            List<String> lore = new ArrayList<>();
                            if (it.getLore() != null) {
                                for (String s : it.getLore()) {
                                    lore.add(s);
                                }
                            }

                            int amt = 1;

                            ItemStack ite = it.getItem().clone();

                            if (lore != null) {
                                if (!lore.contains(Checkout.getString("ClickToRemove"))) {
                                    if (ie.get(i) != null) {
                                        lore.add(MainGUI.getString("Amount") + " §7" + ie.get(i));
                                        amt = ie.get(i);
                                    } else {
                                        lore.add(MainGUI.getString("Amount") + " §71");
                                    }
                                    if (ie.get(i) != null) {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * ie.get(i));
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());
                                        total = total + bd.doubleValue();
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * 1);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        lore.add(MainGUI.getString("Price") + " §7" + bd.doubleValue());
                                        total = total + bd.doubleValue();
                                    }
                                    lore.add(" ");
                                    lore.add(Checkout.getString("ClickToRemove"));
                                    meta.setLore(lore);
                                    ite.setItemMeta(meta);
                                }
                            }

                            if (it.getStock() >= amt) {
                                if (Core.getEconomy().getBalance(p) >= it.getPrice() * amt) {
                                    double value;
                                    if (!shop.isServerShop()) {


                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                        Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), bd.doubleValue());
                                        t = t + bd.doubleValue();
                                        value = bd.doubleValue();


                                        if (shop.isNotify()) {
                                            if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));

                                                if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                    Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                    Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                    Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));


                                                }
                                            }
                                        }
                                    } else {
                                        BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                        t = t + bd.doubleValue();
                                        value = bd.doubleValue();
                                    }

                                    for (int o = 0; o < amt; o++) {
                                        p.getInventory().addItem(it.getItem());
                                    }
                                    if (!it.isInfinite())
                                        it.setStock(it.getStock() - amt);

                                    shop.getHistory().addTransaction(p, new Date(), it, value, amt, false, true);

                                    ShopBuyItemEvent ev = new ShopBuyItemEvent(it, shop);

                                    Bukkit.getPluginManager().callEvent(ev);

                                    double a = amt / it.getAmount();

                                    if (it.getLiveEco()) {
                                        it.setAmountTo(it.getAmountTo() + a);
                                    }

                                } else {
                                    if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                        if (!Messages.getString("CannotAfford").contains("<Item>")) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford") + ite.getItemMeta().getDisplayName());
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                        }
                                    } else {
                                        if (!Messages.getString("CannotAfford").contains("<Item>")) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                        }
                                    }
                                    p.closeInventory();
                                    return;
                                }
                            } else {
                                if (it.getStock() > 0 || it.getStock() <= 0 && it.isInfinite()) {
                                    if (Core.getEconomy().getBalance(p) >= it.getPrice() * it.getStock()) {
                                        double value;
                                        if (!shop.isServerShop()) {
                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                            Core.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(shop.getOwner().getUniqueId()), bd.doubleValue());
                                            t = t + bd.doubleValue();
                                            value = bd.doubleValue();
                                            if (shop.isNotify()) {
                                                if (shop.getOwner() != null && shop.getOwner().isOnline()) {
                                                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                    shop.getOwner().getPlayer().sendMessage(Messages.getString("Prefix") + Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));

                                                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                        Core.getTitleManager().setTimes(shop.getOwner().getPlayer(), 20, 60, 20);
                                                        Core.getTitleManager().sendTitle(shop.getOwner().getPlayer(), Messages.getString("NotifyBuy").replaceAll("<Player>", p.getDisplayName()));
                                                        Core.getTitleManager().sendSubTitle(shop.getOwner().getPlayer(), Messages.getString("ReceivedAmount").replaceAll("<Amount>", "" + bd.doubleValue()));


                                                    }
                                                }
                                            }
                                        } else {
                                            BigDecimal bd = new BigDecimal((it.getPrice() / it.getAmount()) * amt);
                                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                                            Core.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), bd.doubleValue());
                                            t = t + bd.doubleValue();
                                            value = bd.doubleValue();
                                        }

                                        for (int o = 0; o < amt; o++) {
                                            p.getInventory().addItem(it.getItem());
                                        }

                                        if (!it.isInfinite()) {
                                            if (it.getDisplayName() != null) {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CanOnlyBuy") + it.getStock() + " §cof §d" + it.getDisplayName());
                                            } else {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CanOnlyBuy") + it.getStock() + " §cof §d" + it.getItem().getType().name().replaceAll("_", " "));
                                            }
                                        }

                                        if (!it.isInfinite())
                                            it.setStock(0);

                                        if (shop.getHistory() == null) {
                                            shop.loadTransactions();
                                        }
                                        shop.getHistory().addTransaction(p, new Date(), it, value, amt, false, true);

                                        ShopBuyItemEvent ev = new ShopBuyItemEvent(it, shop);

                                        Bukkit.getPluginManager().callEvent(ev);

                                        if (it.getLiveEco()) {
                                            double a = amt / it.getAmount();
                                            it.setAmountTo(it.getAmountTo() + a);
                                        }

                                    } else {
                                        if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                            if (!Messages.getString("CannotAfford").contains("<Item>")) {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford") + ite.getItemMeta().getDisplayName());
                                            } else {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                            }
                                        } else {
                                            if (!Messages.getString("CannotAfford").contains("<Item>")) {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                            } else {
                                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("CannotAfford").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                            }
                                        }
                                        p.closeInventory();
                                        return;
                                    }
                                } else {

                                    if (ite.getItemMeta() != null && ite.getItemMeta().getDisplayName() != null) {
                                        if (!Messages.getString("OutOfStock").contains("<Item>")) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("OutOfStock") + ite.getItemMeta().getDisplayName());
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("OutOfStock").replaceAll("<Item>", ite.getItemMeta().getDisplayName()));
                                        }
                                    } else {
                                        if (!Messages.getString("OutOfStock").contains("<Item>")) {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("OutOfStock") + WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " ")));
                                        } else {
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("OutOfStock").replaceAll("<Item>", WordsCapitalizer.capitalizeEveryWord(ite.getType().name().replaceAll("_", " "))));
                                        }
                                    }
                                    p.closeInventory();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("BuyItem"));
        p.sendMessage(Messages.getString("Prefix") + Messages.getString("TakenAmount").replaceAll("<Amount>", "" + t));


        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

            p.closeInventory();

            Core.getTitleManager().setTimes(p, 20, 60, 20);
            Core.getTitleManager().sendTitle(p, Messages.getString("BuyItem"));
            Core.getTitleManager().sendSubTitle(p, Messages.getString("TakenAmount").replaceAll("<Amount>", "" + t));

            p.closeInventory();

        } else {
            OpenShop.openShopItems(null, p, shop, 1);
        }
        cart.remove(k);

        if (shop.isHoloShop()) {
            ShopHologram holo = shop.getHolographicShop();
            holo.updateItemLines(holo.getItemLine(), false);
        }
    }
}
